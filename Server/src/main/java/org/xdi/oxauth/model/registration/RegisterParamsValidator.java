/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.xdi.oxauth.model.registration;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import javax.enterprise.context.ApplicationScoped;
import org.jboss.seam.annotations.AutoCreate;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import javax.inject.Named;
import org.jboss.seam.annotations.Scope;

import org.xdi.oxauth.model.common.SubjectType;
import org.xdi.oxauth.model.config.ConfigurationFactory;
import org.xdi.oxauth.model.configuration.AppConfiguration;
import org.xdi.oxauth.model.error.ErrorResponseFactory;
import org.xdi.oxauth.model.register.ApplicationType;
import org.xdi.oxauth.model.register.RegisterErrorResponseType;
import org.xdi.oxauth.model.util.URLPatternList;
import org.xdi.oxauth.model.util.Util;
import org.xdi.oxauth.util.ServerUtil;

/**
 * Validates the parameters received for the register web service.
 *
 * @author Javier Rojas Blum
 * @version September 21, 2016
 */
@Stateless
@Named("registerParamsValidator")
@AutoCreate
public class RegisterParamsValidator {

	@Inject
    private Logger log;

	@Inject
    private AppConfiguration appConfiguration;

    //private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String LOCALHOST = "localhost";

    /**
     * Validates the parameters for a register request.
     *
     * @param applicationType     The Application Type: native or web.
     * @param subjectType         The subject_type requested for responses to this Client.
     * @param redirectUris        Space-separated list of redirect URIs.
     * @param sectorIdentifierUrl A HTTPS scheme URL to be used in calculating Pseudonymous Identifiers by the OP.
     *                            The URL contains a file with a single JSON array of redirect_uri values.
     * @return Whether the parameters of client register is valid or not.
     */
    public boolean validateParamsClientRegister(ApplicationType applicationType, SubjectType subjectType,
                                                       List<String> redirectUris, String sectorIdentifierUrl) {
        boolean valid = applicationType != null && redirectUris != null && !redirectUris.isEmpty();

        if (subjectType == null || !appConfiguration.getSubjectTypesSupported().contains(subjectType.toString())) {
            log.debug("Parameter subject_type is not valid.");
            valid = false;
        }

        return valid;
    }

    /**
     * Validates the parameters for a client read request.
     *
     * @param clientId    Unique Client identifier.
     * @param accessToken Access Token obtained out of band to authorize the registrant.
     * @return Whether the parameters of client read is valid or not.
     */
    public boolean validateParamsClientRead(String clientId, String accessToken) {
        return StringUtils.isNotBlank(clientId) && StringUtils.isNotBlank(accessToken);
    }

    /**
     * @param applicationType     The Application Type: native or web.
     * @param subjectType         Subject Type requested for responses to this Client.
     * @param redirectUris        Redirection URI values used by the Client.
     * @param sectorIdentifierUrl A HTTPS scheme URL to be used in calculating Pseudonymous Identifiers by the OP.
     *                            The URL contains a file with a single JSON array of redirect_uri values.
     * @return Whether the Redirect URI parameters are valid or not.
     */
    public boolean validateRedirectUris(ApplicationType applicationType, SubjectType subjectType,
                                               List<String> redirectUris, String sectorIdentifierUrl) {
        boolean valid = true;
        Set<String> redirectUriHosts = new HashSet<String>();

        try {
            if (redirectUris != null && !redirectUris.isEmpty()) {
                for (String redirectUri : redirectUris) {
                    if (redirectUri == null || redirectUri.contains("#")) {
                        valid = false;
                    } else {
                        URI uri = new URI(redirectUri);
                        redirectUriHosts.add(uri.getHost());
                        switch (applicationType) {
                            case WEB:
                                if (!HTTPS.equalsIgnoreCase(uri.getScheme())) {
                                    log.error("Invalid protocol for redirect_uri: " + redirectUri + " (only https protocol is allowed for application_type=web)");
                                    valid = false;
                                } else if (LOCALHOST.equalsIgnoreCase(uri.getHost())) {
                                    valid = false;
                                }
                                break;
                            case NATIVE:
                                // to conform "OAuth 2.0 for Native Apps" https://tools.ietf.org/html/draft-wdenniss-oauth-native-apps-00
                                // we allow registration with custom schema for native apps.
//                                if (!HTTP.equalsIgnoreCase(uri.getScheme())) {
//                                    valid = false;
//                                } else if (!LOCALHOST.equalsIgnoreCase(uri.getHost())) {
//                                    valid = false;
//                                }
                                break;
                        }
                    }
                }
            } else {
                valid = false;
            }
        } catch (URISyntaxException e) {
            valid = false;
        }

        /*
         * Providers that use pairwise sub (subject) values SHOULD utilize the sector_identifier_uri value
         * provided in the Subject Identifier calculation for pairwise identifiers.
         *
         * If the Client has not provided a value for sector_identifier_uri in Dynamic Client Registration,
         * the Sector Identifier used for pairwise identifier calculation is the host component of the
         * registered redirect_uri.
         *
         * If there are multiple hostnames in the registered redirect_uris, the Client MUST register a
         * sector_identifier_uri.
         */
        if (subjectType != null && subjectType.equals(SubjectType.PAIRWISE) && StringUtils.isBlank(sectorIdentifierUrl)) {
            if (redirectUriHosts.size() > 1) {
                valid = false;
            }
        }

        // Validate Sector Identifier URL
        if (valid && StringUtils.isNotBlank(sectorIdentifierUrl)) {
            try {
                URI uri = new URI(sectorIdentifierUrl);
                if (!HTTPS.equalsIgnoreCase(uri.getScheme())) {
                    valid = false;
                }

                ClientRequest clientRequest = new ClientRequest(sectorIdentifierUrl);
                clientRequest.setHttpMethod(HttpMethod.GET);

                ClientResponse<String> clientResponse = clientRequest.get(String.class);
                int status = clientResponse.getStatus();

                if (status == 200) {
                    String entity = clientResponse.getEntity(String.class);

                    JSONArray sectorIdentifierJsonArray = new JSONArray(entity);
                    valid = Util.asList(sectorIdentifierJsonArray).containsAll(redirectUris);
                }
            } catch (URISyntaxException e) {
                log.trace(e.getMessage(), e);
                valid = false;
            } catch (UnknownHostException e) {
                log.trace(e.getMessage(), e);
                valid = false;
            } catch (ConnectException e) {
                log.trace(e.getMessage(), e);
                valid = false;
            } catch (JSONException e) {
                log.trace(e.getMessage(), e);
                valid = false;
            } catch (Exception e) {
                log.trace(e.getMessage(), e);
                valid = false;
            }
        }

        // Validate Redirect Uris checking the white list and black list
        if (valid) {
            valid = checkWhiteListRedirectUris(redirectUris) && checkBlackListRedirectUris(redirectUris);
        }

        return valid;
    }

    /**
     * All the Redirect Uris must match to return true.
     */
    private boolean checkWhiteListRedirectUris(List<String> redirectUris) {
        boolean valid = true;
        List<String> whiteList = appConfiguration.getClientWhiteList();
        URLPatternList urlPatternList = new URLPatternList(whiteList);

        for (String redirectUri : redirectUris) {
            valid &= urlPatternList.isUrlListed(redirectUri);
        }

        return valid;
    }

    /**
     * None of the Redirect Uris must match to return true.
     */
    private boolean checkBlackListRedirectUris(List<String> redirectUris) {
        boolean valid = true;
        List<String> blackList = appConfiguration.getClientBlackList();
        URLPatternList urlPatternList = new URLPatternList(blackList);

        for (String redirectUri : redirectUris) {
            valid &= !urlPatternList.isUrlListed(redirectUri);
        }

        return valid;
    }

    public void validateLogoutUri(List<String> logoutUris, List<String> redirectUris, ErrorResponseFactory errorResponseFactory) {
        if (logoutUris == null || logoutUris.isEmpty()) { // logout uri is optional so null or empty list is valid
            return;
        }
        for (String logoutUri : logoutUris) {
            validateLogoutUri(logoutUri, redirectUris, errorResponseFactory);
        }
    }

    public void validateLogoutUri(String logoutUri, List<String> redirectUris, ErrorResponseFactory errorResponseFactory) {
        if (Util.isNullOrEmpty(logoutUri)) { // logout uri is optional so null or empty string is valid
            return;
        }

        // preconditions
        if (redirectUris == null || redirectUris.isEmpty()) {
            log.error("Preconditions of logout uri validation are failed.");
            throwInvalidLogoutUri(errorResponseFactory);
            return;
        }

        try {
            Set<String> redirectUriHosts = collectUriHosts(redirectUris);

            URI uri = new URI(logoutUri);

            if (!redirectUriHosts.contains(uri.getHost())) {
                log.error("logout uri host is not within redirect_uris, logout_uri: {0}, redirect_uris: {1}", logoutUri, redirectUris);
                throwInvalidLogoutUri(errorResponseFactory);
                return;
            }

            if (!HTTPS.equalsIgnoreCase(uri.getScheme())) {
                log.error("logout uri schema is not https, logout_uri: {0}", logoutUri);
                throwInvalidLogoutUri(errorResponseFactory);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throwInvalidLogoutUri(errorResponseFactory);
        }
    }

    private void throwInvalidLogoutUri(ErrorResponseFactory errorResponseFactory) throws WebApplicationException {
        throw new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST.getStatusCode()).
                        entity(errorResponseFactory.getErrorAsJson(RegisterErrorResponseType.INVALID_LOGOUT_URI)).
                        cacheControl(ServerUtil.cacheControl(true, false)).
                        header("Pragma", "no-cache").
                        build());
    }

    private Set<String> collectUriHosts(List<String> uriList) throws URISyntaxException {
        Set<String> hosts = new HashSet<String>();

        for (String redirectUri : uriList) {
            URI uri = new URI(redirectUri);
            hosts.add(uri.getHost());
        }
        return hosts;
    }
}