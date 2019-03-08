/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.xdi.oxauth.userinfo.ws.rs;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.gluu.persist.exception.EntryPersistenceException;
import org.slf4j.Logger;
import org.xdi.model.GluuAttribute;
import org.xdi.model.GluuAttributeDataType;
import org.xdi.oxauth.audit.ApplicationAuditLogger;
import org.xdi.oxauth.model.audit.Action;
import org.xdi.oxauth.model.audit.OAuth2AuditLog;
import org.xdi.oxauth.model.authorize.Claim;
import org.xdi.oxauth.model.common.*;
import org.xdi.oxauth.model.config.WebKeysConfiguration;
import org.xdi.oxauth.model.configuration.AppConfiguration;
import org.xdi.oxauth.model.crypto.AbstractCryptoProvider;
import org.xdi.oxauth.model.crypto.CryptoProviderFactory;
import org.xdi.oxauth.model.crypto.encryption.BlockEncryptionAlgorithm;
import org.xdi.oxauth.model.crypto.encryption.KeyEncryptionAlgorithm;
import org.xdi.oxauth.model.crypto.signature.SignatureAlgorithm;
import org.xdi.oxauth.model.error.ErrorResponseFactory;
import org.xdi.oxauth.model.exception.InvalidClaimException;
import org.xdi.oxauth.model.exception.InvalidJweException;
import org.xdi.oxauth.model.jwe.Jwe;
import org.xdi.oxauth.model.jwe.JweEncrypter;
import org.xdi.oxauth.model.jwe.JweEncrypterImpl;
import org.xdi.oxauth.model.jwk.Algorithm;
import org.xdi.oxauth.model.jwk.JSONWebKeySet;
import org.xdi.oxauth.model.jwk.Use;
import org.xdi.oxauth.model.jwt.Jwt;
import org.xdi.oxauth.model.jwt.JwtClaims;
import org.xdi.oxauth.model.jwt.JwtSubClaimObject;
import org.xdi.oxauth.model.jwt.JwtType;
import org.xdi.oxauth.model.ldap.PairwiseIdentifier;
import org.xdi.oxauth.model.registration.Client;
import org.xdi.oxauth.model.token.JsonWebResponse;
import org.xdi.oxauth.model.userinfo.UserInfoErrorResponseType;
import org.xdi.oxauth.model.userinfo.UserInfoParamsValidator;
import org.xdi.oxauth.model.util.JwtUtil;
import org.xdi.oxauth.model.util.Util;
import org.xdi.oxauth.service.*;
import org.xdi.oxauth.service.external.ExternalDynamicScopeService;
import org.xdi.oxauth.service.external.context.DynamicScopeExternalContext;
import org.xdi.oxauth.util.ServerUtil;
import org.xdi.util.security.StringEncrypter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Provides interface for User Info REST web services
 *
 * @author Javier Rojas Blum
 * @version March 8, 2019
 */
@Path("/")
public class UserInfoRestWebServiceImpl implements UserInfoRestWebService {

    @Inject
    private Logger log;

    @Inject
    private ApplicationAuditLogger applicationAuditLogger;

    @Inject
    private ErrorResponseFactory errorResponseFactory;

    @Inject
    private AuthorizationGrantList authorizationGrantList;

    @Inject
    private ClientService clientService;

    @Inject
    private ScopeService scopeService;

    @Inject
    private AttributeService attributeService;

    @Inject
    private UserService userService;

    @Inject
    private ExternalDynamicScopeService externalDynamicScopeService;

    @Inject
    private PairwiseIdentifierService pairwiseIdentifierService;

    @Inject
    private AppConfiguration appConfiguration;

    @Inject
    private WebKeysConfiguration webKeysConfiguration;

    @Override
    public Response requestUserInfoGet(String accessToken, String authorization, HttpServletRequest request, SecurityContext securityContext) {
        return requestUserInfo(accessToken, authorization, request, securityContext);
    }

    @Override
    public Response requestUserInfoPost(String accessToken, String authorization, HttpServletRequest request, SecurityContext securityContext) {
        return requestUserInfo(accessToken, authorization, request, securityContext);
    }

    public Response requestUserInfo(String accessToken, String authorization, HttpServletRequest request, SecurityContext securityContext) {
        if (authorization != null && !authorization.isEmpty() && authorization.startsWith("Bearer ")) {
            accessToken = authorization.substring(7);
        }
        log.debug("Attempting to request User Info, Access token = {}, Is Secure = {}",
                accessToken, securityContext.isSecure());
        Response.ResponseBuilder builder = Response.ok();

        OAuth2AuditLog oAuth2AuditLog = new OAuth2AuditLog(ServerUtil.getIpAddress(request), Action.USER_INFO);

        try {
            if (!UserInfoParamsValidator.validateParams(accessToken)) {
                builder = Response.status(400);
                builder.entity(errorResponseFactory.getErrorAsJson(UserInfoErrorResponseType.INVALID_REQUEST));
            } else {
                AuthorizationGrant authorizationGrant = authorizationGrantList.getAuthorizationGrantByAccessToken(accessToken);

                if (authorizationGrant == null) {
                    log.trace("Failed to find authorization grant by access_token: " + accessToken);
                    return response(400, UserInfoErrorResponseType.INVALID_TOKEN);
                }

                final AbstractToken accessTokenObject = authorizationGrant.getAccessToken(accessToken);
                if (accessTokenObject == null || !accessTokenObject.isValid()) {
                    log.trace("Invalid access token object, access_token: {}, isNull: {}, isValid: {}", accessToken, accessTokenObject == null, accessTokenObject.isValid());
                    return response(400, UserInfoErrorResponseType.INVALID_TOKEN);
                }

                if (authorizationGrant.getAuthorizationGrantType() == AuthorizationGrantType.CLIENT_CREDENTIALS) {
                    builder = Response.status(403);
                    builder.entity(errorResponseFactory.getErrorAsJson(UserInfoErrorResponseType.INSUFFICIENT_SCOPE));
                } else if (appConfiguration.getOpenidScopeBackwardCompatibility()
                        && !authorizationGrant.getScopes().contains(DefaultScope.OPEN_ID.toString())
                        && !authorizationGrant.getScopes().contains(DefaultScope.PROFILE.toString())) {
                    builder = Response.status(403);
                    builder.entity(errorResponseFactory.getErrorAsJson(UserInfoErrorResponseType.INSUFFICIENT_SCOPE));
                    oAuth2AuditLog.updateOAuth2AuditLog(authorizationGrant, false);
                } else if (!appConfiguration.getOpenidScopeBackwardCompatibility()
                        && !authorizationGrant.getScopes().contains(DefaultScope.OPEN_ID.toString())) {
                    builder = Response.status(403);
                    builder.entity(errorResponseFactory.getErrorAsJson(UserInfoErrorResponseType.INSUFFICIENT_SCOPE));
                    oAuth2AuditLog.updateOAuth2AuditLog(authorizationGrant, false);
                } else {
                    oAuth2AuditLog.updateOAuth2AuditLog(authorizationGrant, true);
                    CacheControl cacheControl = new CacheControl();
                    cacheControl.setPrivate(true);
                    cacheControl.setNoTransform(false);
                    cacheControl.setNoStore(true);
                    builder.cacheControl(cacheControl);
                    builder.header("Pragma", "no-cache");

                    User currentUser = authorizationGrant.getUser();
                    try {
                        currentUser = userService.getUserByDn(authorizationGrant.getUserDn());
                    } catch (EntryPersistenceException ex) {
                        log.warn("Failed to reload user entry: '{}'", authorizationGrant.getUserDn());
                    }

                    if (authorizationGrant.getClient() != null
                            && authorizationGrant.getClient().getUserInfoEncryptedResponseAlg() != null
                            && authorizationGrant.getClient().getUserInfoEncryptedResponseEnc() != null) {
                        KeyEncryptionAlgorithm keyEncryptionAlgorithm = KeyEncryptionAlgorithm.fromName(authorizationGrant.getClient().getUserInfoEncryptedResponseAlg());
                        BlockEncryptionAlgorithm blockEncryptionAlgorithm = BlockEncryptionAlgorithm.fromName(authorizationGrant.getClient().getUserInfoEncryptedResponseEnc());
                        builder.type("application/jwt");
                        builder.entity(getJweResponse(
                                keyEncryptionAlgorithm,
                                blockEncryptionAlgorithm,
                                currentUser,
                                authorizationGrant,
                                authorizationGrant.getScopes()));
                    } else if (authorizationGrant.getClient() != null
                            && authorizationGrant.getClient().getUserInfoSignedResponseAlg() != null) {
                        SignatureAlgorithm algorithm = SignatureAlgorithm.fromString(authorizationGrant.getClient().getUserInfoSignedResponseAlg());
                        builder.type("application/jwt");
                        builder.entity(getJwtResponse(algorithm,
                                currentUser,
                                authorizationGrant,
                                authorizationGrant.getScopes()));
                    } else {
                        builder.type((MediaType.APPLICATION_JSON + ";charset=UTF-8"));
                        builder.entity(getJSonResponse(currentUser,
                                authorizationGrant,
                                authorizationGrant.getScopes()));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); // 500
        } finally {
            applicationAuditLogger.sendMessage(oAuth2AuditLog);
        }

        return builder.build();
    }

    private Response response(int status, UserInfoErrorResponseType errorResponseType) {
        return Response.status(status).entity(errorResponseFactory.getErrorAsJson(errorResponseType)).build();
    }

    public String getJwtResponse(SignatureAlgorithm signatureAlgorithm, User user, AuthorizationGrant authorizationGrant,
                                 Collection<String> scopes) throws Exception {
        Jwt jwt = new Jwt();
        AbstractCryptoProvider cryptoProvider = CryptoProviderFactory.getCryptoProvider(appConfiguration);

        // Header
        jwt.getHeader().setType(JwtType.JWT);
        jwt.getHeader().setAlgorithm(signatureAlgorithm);

        String keyId = cryptoProvider.getKeyId(webKeysConfiguration, Algorithm.fromString(signatureAlgorithm.getName()), Use.SIGNATURE);
        if (keyId != null) {
            jwt.getHeader().setKeyId(keyId);
        }

        // Claims
        String claimsString = getJSonResponse(user, authorizationGrant, scopes);
        JwtClaims claims = new JwtClaims(new JSONObject(claimsString));
        jwt.setClaims(claims);

        // If signed, the UserInfo Response SHOULD contain the Claims iss (issuer) and aud (audience) as members.
        // The iss value should be the OP's Issuer Identifier URL.
        // The aud value should be or include the RP's Client ID value.
        jwt.getClaims().setIssuer(appConfiguration.getIssuer());
        jwt.getClaims().setAudience(authorizationGrant.getClientId());

        // Signature
        String sharedSecret = clientService.decryptSecret(authorizationGrant.getClient().getClientSecret());
        String signature = cryptoProvider.sign(jwt.getSigningInput(), jwt.getHeader().getKeyId(), sharedSecret, signatureAlgorithm);
        jwt.setEncodedSignature(signature);

        return jwt.toString();
    }

    public String getJweResponse(
            KeyEncryptionAlgorithm keyEncryptionAlgorithm, BlockEncryptionAlgorithm blockEncryptionAlgorithm,
            User user, AuthorizationGrant authorizationGrant, Collection<String> scopes) throws Exception {
        Jwe jwe = new Jwe();

        // Header
        jwe.getHeader().setType(JwtType.JWT);
        jwe.getHeader().setAlgorithm(keyEncryptionAlgorithm);
        jwe.getHeader().setEncryptionMethod(blockEncryptionAlgorithm);

        // Claims
        String claimsString = getJSonResponse(user, authorizationGrant, scopes);
        JwtClaims claims = new JwtClaims(new JSONObject(claimsString));
        jwe.setClaims(claims);

        // If encrypted, the UserInfo Response SHOULD contain the Claims iss (issuer) and aud (audience) as members.
        // The iss value should be the OP's Issuer Identifier URL.
        // The aud value should be or include the RP's Client ID value.
        jwe.getClaims().setIssuer(appConfiguration.getIssuer());
        jwe.getClaims().setAudience(authorizationGrant.getClientId());

        // Encryption
        if (keyEncryptionAlgorithm == KeyEncryptionAlgorithm.RSA_OAEP
                || keyEncryptionAlgorithm == KeyEncryptionAlgorithm.RSA1_5) {
            JSONObject jsonWebKeys = JwtUtil.getJSONWebKeys(authorizationGrant.getClient().getJwksUri());
            AbstractCryptoProvider cryptoProvider = CryptoProviderFactory.getCryptoProvider(appConfiguration);
            String keyId = cryptoProvider.getKeyId(JSONWebKeySet.fromJSONObject(jsonWebKeys),
                    Algorithm.fromString(keyEncryptionAlgorithm.getName()),
                    Use.ENCRYPTION);
            PublicKey publicKey = cryptoProvider.getPublicKey(keyId, jsonWebKeys);

            if (publicKey != null) {
                JweEncrypter jweEncrypter = new JweEncrypterImpl(keyEncryptionAlgorithm, blockEncryptionAlgorithm, publicKey);
                jwe = jweEncrypter.encrypt(jwe);
            } else {
                throw new InvalidJweException("The public key is not valid");
            }
        } else if (keyEncryptionAlgorithm == KeyEncryptionAlgorithm.A128KW
                || keyEncryptionAlgorithm == KeyEncryptionAlgorithm.A256KW) {
            try {
                byte[] sharedSymmetricKey = clientService.decryptSecret(authorizationGrant.getClient().getClientSecret()).getBytes(Util.UTF8_STRING_ENCODING);
                JweEncrypter jweEncrypter = new JweEncrypterImpl(keyEncryptionAlgorithm, blockEncryptionAlgorithm, sharedSymmetricKey);
                jwe = jweEncrypter.encrypt(jwe);
            } catch (UnsupportedEncodingException e) {
                throw new InvalidJweException(e);
            } catch (StringEncrypter.EncryptionException e) {
                throw new InvalidJweException(e);
            } catch (Exception e) {
                throw new InvalidJweException(e);
            }
        }

        return jwe.toString();
    }

    /**
     * Builds a JSon String with the response parameters.
     */
    public String getJSonResponse(User user, AuthorizationGrant authorizationGrant, Collection<String> scopes)
            throws Exception {
        JsonWebResponse jsonWebResponse = new JsonWebResponse();

        // Claims
        List<Scope> dynamicScopes = new ArrayList<Scope>();
        for (String scopeName : scopes) {
            org.xdi.oxauth.model.common.Scope scope = scopeService.getScopeByDisplayName(scopeName);
            if ((scope != null) && (org.xdi.oxauth.model.common.ScopeType.DYNAMIC == scope.getScopeType())) {
                dynamicScopes.add(scope);
                continue;
            }

            Map<String, Object> claims = getClaims(user, scope);

            if (scope.getIsOxAuthGroupClaims()) {
                JwtSubClaimObject groupClaim = new JwtSubClaimObject();
                groupClaim.setName(scope.getDisplayName());
                for (Map.Entry<String, Object> entry : claims.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof List) {
                        groupClaim.setClaim(key, (List<String>) value);
                    } else {
                        groupClaim.setClaim(key, (String) value);
                    }
                }

                jsonWebResponse.getClaims().setClaim(scope.getDisplayName(), groupClaim);
            } else {
                for (Map.Entry<String, Object> entry : claims.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof List) {
                        jsonWebResponse.getClaims().setClaim(key, (List<String>) value);
                    } else if (value instanceof Boolean) {
                        jsonWebResponse.getClaims().setClaim(key, (Boolean) value);
                    } else if (value instanceof Date) {
                        jsonWebResponse.getClaims().setClaim(key, ((Date) value).getTime());
                    } else {
                        jsonWebResponse.getClaims().setClaim(key, (String) value);
                    }
                }
            }

            jsonWebResponse.getClaims().setSubjectIdentifier(authorizationGrant.getUser().getAttribute("inum"));
        }

        if (authorizationGrant.getClaims() != null) {
            JSONObject claimsObj = new JSONObject(authorizationGrant.getClaims());
            if (claimsObj.has("userinfo")) {
                JSONObject userInfoObj = claimsObj.getJSONObject("userinfo");
                for (Iterator<String> it = userInfoObj.keys(); it.hasNext(); ) {
                    String claimName = it.next();
                    boolean optional = true; // ClaimValueType.OPTIONAL.equals(claim.getClaimValue().getClaimValueType());
                    GluuAttribute gluuAttribute = attributeService.getByClaimName(claimName);

                    if (gluuAttribute != null) {
                        String ldapClaimName = gluuAttribute.getName();

                        Object attribute = user.getAttribute(ldapClaimName, optional);
                        if (attribute != null) {
                            if (attribute instanceof JSONArray) {
                                JSONArray jsonArray = (JSONArray) attribute;
                                List<String> values = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String value = jsonArray.optString(i);
                                    if (value != null) {
                                        values.add(value);
                                    }
                                }
                                jsonWebResponse.getClaims().setClaim(claimName, values);
                            } else {
                                String value = (String) attribute;
                                jsonWebResponse.getClaims().setClaim(claimName, value);
                            }
                        }
                    }
                }
            }
        }

        if (authorizationGrant.getJwtAuthorizationRequest() != null
                && authorizationGrant.getJwtAuthorizationRequest().getUserInfoMember() != null) {
            for (Claim claim : authorizationGrant.getJwtAuthorizationRequest().getUserInfoMember().getClaims()) {
                boolean optional = true; // ClaimValueType.OPTIONAL.equals(claim.getClaimValue().getClaimValueType());
                GluuAttribute gluuAttribute = attributeService.getByClaimName(claim.getName());

                if (gluuAttribute != null) {
                    Client client = authorizationGrant.getClient();

                    if (validateRequesteClaim(gluuAttribute, client.getClaims(), scopes)) {
                        String ldapClaimName = gluuAttribute.getName();
                        Object attribute = user.getAttribute(ldapClaimName, optional);
                        if (attribute != null) {
                            if (attribute instanceof JSONArray) {
                                JSONArray jsonArray = (JSONArray) attribute;
                                List<String> values = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String value = jsonArray.optString(i);
                                    if (value != null) {
                                        values.add(value);
                                    }
                                }
                                jsonWebResponse.getClaims().setClaim(claim.getName(), values);
                            } else {
                                String value = (String) attribute;
                                jsonWebResponse.getClaims().setClaim(claim.getName(), value);
                            }
                        }
                    }
                }
            }
        }

        // Check for Subject Identifier Type
        if (authorizationGrant.getClient().getSubjectType() != null &&
                SubjectType.fromString(authorizationGrant.getClient().getSubjectType()).equals(SubjectType.PAIRWISE) &&
                (StringUtils.isNotBlank(authorizationGrant.getClient().getSectorIdentifierUri()) || authorizationGrant.getClient().getRedirectUris() != null)) {
            String sectorIdentifierUri = null;
            if (StringUtils.isNotBlank(authorizationGrant.getClient().getSectorIdentifierUri())) {
                sectorIdentifierUri = authorizationGrant.getClient().getSectorIdentifierUri();
            } else {
                sectorIdentifierUri = authorizationGrant.getClient().getRedirectUris()[0];
            }

            String userInum = authorizationGrant.getUser().getAttribute("inum");
            String clientId = authorizationGrant.getClientId();
            PairwiseIdentifier pairwiseIdentifier = pairwiseIdentifierService.findPairWiseIdentifier(
                    userInum, sectorIdentifierUri, clientId);
            if (pairwiseIdentifier == null) {
                pairwiseIdentifier = new PairwiseIdentifier(sectorIdentifierUri, clientId);
                pairwiseIdentifier.setId(UUID.randomUUID().toString());
                pairwiseIdentifier.setDn(pairwiseIdentifierService.getDnForPairwiseIdentifier(
                        pairwiseIdentifier.getId(),
                        userInum));
                pairwiseIdentifierService.addPairwiseIdentifier(userInum, pairwiseIdentifier);
            }
            jsonWebResponse.getClaims().setSubjectIdentifier(pairwiseIdentifier.getId());
        } else {
            if (authorizationGrant.getClient().getSubjectType() != null && SubjectType.fromString(authorizationGrant.getClient().getSubjectType()).equals(SubjectType.PAIRWISE)) {
                log.warn("Unable to calculate the pairwise subject identifier because the client hasn't a redirect uri. A public subject identifier will be used instead.");
            }

            String openidSubAttribute = appConfiguration.getOpenidSubAttribute();
            jsonWebResponse.getClaims().setSubjectIdentifier(authorizationGrant.getUser().getAttribute(openidSubAttribute));
        }

        if ((dynamicScopes.size() > 0) && externalDynamicScopeService.isEnabled()) {
            final UnmodifiableAuthorizationGrant unmodifiableAuthorizationGrant = new UnmodifiableAuthorizationGrant(authorizationGrant);
            DynamicScopeExternalContext dynamicScopeContext = new DynamicScopeExternalContext(dynamicScopes, jsonWebResponse, unmodifiableAuthorizationGrant);
            externalDynamicScopeService.executeExternalUpdateMethods(dynamicScopeContext);
        }

        return jsonWebResponse.toString();
    }

    public boolean validateRequesteClaim(GluuAttribute gluuAttribute, String[] clientAllowedClaims, Collection<String> scopes) {
        if (gluuAttribute != null) {
            if (clientAllowedClaims != null) {
                for (int i = 0; i < clientAllowedClaims.length; i++) {
                    if (gluuAttribute.getDn().equals(clientAllowedClaims[i])) {
                        return true;
                    }
                }
            }

            for (String scopeName : scopes) {
                org.xdi.oxauth.model.common.Scope scope = scopeService.getScopeByDisplayName(scopeName);

                if (scope != null && scope.getOxAuthClaims() != null) {
                    for (String claimDn : scope.getOxAuthClaims()) {
                        if (gluuAttribute.getDisplayName().equals(attributeService.getAttributeByDn(claimDn).getDisplayName())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public Map<String, Object> getClaims(User user, Scope scope) throws InvalidClaimException, ParseException {
        Map<String, Object> claims = new HashMap<String, Object>();

        if (scope != null && scope.getOxAuthClaims() != null) {
            for (String claimDn : scope.getOxAuthClaims()) {
                GluuAttribute gluuAttribute = attributeService.getAttributeByDn(claimDn);

                String claimName = gluuAttribute.getOxAuthClaimName();
                String ldapName = gluuAttribute.getName();
                Object attribute = null;

                if (StringUtils.isNotBlank(claimName) && StringUtils.isNotBlank(ldapName)) {
                    if (ldapName.equals("uid")) {
                        attribute = user.getUserId();
                    } else if (GluuAttributeDataType.BOOLEAN.equals(gluuAttribute.getDataType())) {
                        attribute = Boolean.parseBoolean((String) user.getAttribute(gluuAttribute.getName(), true));
                    } else if (GluuAttributeDataType.DATE.equals(gluuAttribute.getDataType())) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'");
                        Object attributeValue = user.getAttribute(gluuAttribute.getName(), true);
                        if (attributeValue != null) {
                            attribute = format.parse(attributeValue.toString());
                        }
                    } else {
                        attribute = user.getAttribute(gluuAttribute.getName(), true);
                    }

                    if (attribute != null) {
                        if (attribute instanceof JSONArray) {
                            JSONArray jsonArray = (JSONArray) attribute;
                            List<String> values = new ArrayList<String>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String value = jsonArray.optString(i);
                                if (value != null) {
                                    values.add(value);
                                }
                            }
                            claims.put(claimName, values);
                        } else {
                            claims.put(claimName, attribute);
                        }
                    }
                }
            }
        }

        return claims;
    }

}