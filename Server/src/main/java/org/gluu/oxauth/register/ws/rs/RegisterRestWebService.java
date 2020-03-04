/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxauth.register.ws.rs;

import com.wordnik.swagger.annotations.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Provides interface for register REST web services.
 *
 * @author Javier Rojas Blum
 * @author Yuriy Zabrovarnyy
 * @version 0.1, 01.11.2012
 */
@Api(value = "/", description = "The Client Registration Endpoint is an OAuth 2.0 Protected Resource through which a new Client registration can be requested. The OpenID Provider MAY require an Initial Access Token that is provisioned out-of-band (in a manner that is out of scope for this specification) to restrict registration requests to only authorized Clients or developers.")
public interface RegisterRestWebService {

    /**
     * In order for an OpenID Connect client to utilize OpenID services for a user, the client needs to register with
     * the OpenID Provider to acquire a client ID and shared secret.
     *
     * @param requestParams   request parameters
     * @param httpRequest     http request object
     * @param securityContext An injectable interface that provides access to security related information.
     * @return response
     */
    @POST
    @Path("/register")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
            value = "Registers new client dynamically.",
            notes = "Registers new client dynamically.",
            response = Response.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "invalid_request\n" +
                    "The request is missing a required parameter, includes an unsupported parameter or parameter value, repeats the same parameter, uses more than one method for including an access token, or is otherwise malformed.  The resource server SHOULD respond with the HTTP 400 (Bad Request) status code."),
            @ApiResponse(code = 400, message = "invalid_redirect_uri\n" +
                    "The value of one or more redirect_uris is invalid. "),
            @ApiResponse(code = 400, message = "invalid_client_metadata\n" +
                    "The value of one of the Client Metadata fields is invalid and the server has rejected this request. Note that an Authorization Server MAY choose to substitute a valid value for any requested parameter of a Client's Metadata."),
            @ApiResponse(code = 302, message = "access_denies\n" +
                    "The authorization server denied the request.")

    })
    Response requestRegister(
            @ApiParam(value = "Request parameters as JSON object with data described by Connect Client Registration Specification. ", required = true)
            String requestParams,
            @Context HttpServletRequest httpRequest,
            @Context SecurityContext securityContext);

    /**
     * This operation updates the Client Metadata for a previously registered client.
     *
     * @param requestParams   request parameters
     * @param clientId        client id
     * @param authorization   Access Token that is used at the Client Configuration Endpoint
     * @param httpRequest     http request object
     * @param securityContext An injectable interface that provides access to security related information.
     * @return response
     */

    @PUT
    @Path("register")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
            value = "Updates client info.",
            notes = "Updates client info.",
            response = Response.class,
            responseContainer = "JSON"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "invalid_request\n" +
                    "The request is missing a required parameter, includes an unsupported parameter or parameter value, repeats the same parameter, uses more than one method for including an access token, or is otherwise malformed.  The resource server SHOULD respond with the HTTP 400 (Bad Request) status code."),
            @ApiResponse(code = 400, message = "invalid_redirect_uri\n" +
                    "The value of one or more redirect_uris is invalid. "),
            @ApiResponse(code = 400, message = "invalid_client_metadata\n" +
                    "The value of one of the Client Metadata fields is invalid and the server has rejected this request. Note that an Authorization Server MAY choose to substitute a valid value for any requested parameter of a Client's Metadata."),
            @ApiResponse(code = 302, message = "access_denies\n" +
                    "The authorization server denied the request.")
    })
    Response requestClientUpdate(
            @ApiParam(value = "Request parameters as JSON object with data described by Connect Client Registration Specification. ", required = true)
            String requestParams,
            @QueryParam("client_id")
            @ApiParam(value = "Client ID that identifies client that must be updated by this request.", required = true)
            String clientId,
            @HeaderParam("Authorization") String authorization,
            @Context HttpServletRequest httpRequest,
            @Context SecurityContext securityContext);

    /**
     * This operation retrieves the Client Metadata for a previously registered client.
     *
     * @param clientId        Unique Client identifier.
     * @param securityContext An injectable interface that provides access to security related information.
     * @return response
     */
    @GET
    @Path("/register")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
            value = "Reads client info.",
            notes = "Reads client info.",
            response = Response.class,
            responseContainer = "JSON"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "invalid_request\n" +
                    "The request is missing a required parameter, includes an unsupported parameter or parameter value, repeats the same parameter, uses more than one method for including an access token, or is otherwise malformed.  The resource server SHOULD respond with the HTTP 400 (Bad Request) status code."),
            @ApiResponse(code = 400, message = "invalid_redirect_uri\n" +
                    "The value of one or more redirect_uris is invalid. "),
            @ApiResponse(code = 400, message = "invalid_client_metadata\n" +
                    "The value of one of the Client Metadata fields is invalid and the server has rejected this request. Note that an Authorization Server MAY choose to substitute a valid value for any requested parameter of a Client's Metadata."),
            @ApiResponse(code = 302, message = "access_denies\n" +
                    "The authorization server denied the request.")
    })
    Response requestClientRead(
            @QueryParam("client_id")
            @ApiParam(value = "Client ID that identifies client.", required = true)
            String clientId,
            @HeaderParam("Authorization") String authorization,
            @Context HttpServletRequest httpRequest,
            @Context SecurityContext securityContext);

    /**
     * This operation removes the Client Metadata for a previously registered client.
     *
     * @param clientId        Unique Client identifier.
     * @param securityContext An injectable interface that provides access to security related information.
     * @return If a client has been successfully deprovisioned, the authorization
     * server responds with an HTTP 204 No Content message.
     * <p>
     * If the registration access token used to make this request is not
     * valid, the server responds with HTTP 401 Unauthorized.
     * <p>
     * If the client does not exist on this server, the server responds
     * with HTTP 401 Unauthorized.
     * <p>
     * If the client is not allowed to delete itself, the server
     * responds with HTTP 403 Forbidden.
     */
    @DELETE
    @Path("/register")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
            value = "Deletes client info.",
            notes = "Deletes client info.",
            response = Response.class,
            responseContainer = "JSON"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "invalid_token\n" +
                    "The registration access token used to make this request is not valid"),
            @ApiResponse(code = 401, message = "invalid_client_id\n" +
                    "The client does not exist on this server "),
            @ApiResponse(code = 403, message = "not_allowed\n" +
                    "The client is not allowed to delete itself")
    })
    Response delete(
            @QueryParam("client_id")
            @ApiParam(value = "Client ID that identifies client.", required = true) String clientId,
            @HeaderParam("Authorization") String authorization,
            @Context HttpServletRequest httpRequest,
            @Context SecurityContext securityContext);
}