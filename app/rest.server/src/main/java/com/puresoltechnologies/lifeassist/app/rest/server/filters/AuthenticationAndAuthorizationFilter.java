package com.puresoltechnologies.lifeassist.app.rest.server.filters;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.lifeassist.app.rest.api.auth.AuthService;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.AuthElement;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.DenyAll;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.PermitAll;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.RolesAllowed;
import com.puresoltechnologies.lifeassist.app.rest.server.services.AuthServiceImpl;

@Provider
public class AuthenticationAndAuthorizationFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAndAuthorizationFilter.class);

    // 401 - Access denied
    private static final Response ACCESS_UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build();
    // 403 - Forbidden
    private static final Response FORBIDDEN = Response.status(Response.Status.FORBIDDEN).build();

    private static AuthService authService = AuthServiceImpl.getInstance();

    private static boolean anonymousCanRead = false; /*
						      * TODO specify this via
						      * global setting
						      */

    @Context
    private ResourceInfo resourceInfo;
    private EmailAddress email;
    private UUID authToken;

    @PostConstruct
    public void postConstruct() {
	logger.info("Authentication and authorization is enabled.");
    }

    @PreDestroy
    public void preDestroy() {
	logger.info("Authentication and authorization was disabled.");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
	try {
	    if (isServerInternalRequest()) {
		return;
	    }
	    procressAuthToken(requestContext);
	    checkAuthenticationAndAuthorization(requestContext);
	} catch (SQLException e) {
	    throw new IOException("Could not authenticate the user or authorize the request.", e);
	}
    }

    private boolean isServerInternalRequest() {
	if ("org.glassfish.jersey.server.wadl.processor"
		.equals(resourceInfo.getResourceClass().getPackage().getName())) {
	    return true;
	}
	return false;
    }

    private void checkAuthenticationAndAuthorization(ContainerRequestContext requestContext) throws SQLException {
	/* Check for correct authentication data... */
	if (!authService.isAuthenticated(email, authToken)) {
	    if (!isOpenGlobally(requestContext)) {
		/*
		 * Authentication data does not fit, so we do not need to check
		 * further...
		 */
		logger.warn(
			"Invalid email " + email + " and " + AuthElement.AUTH_TOKEN_HEADER + " '" + authToken + "'.");
		requestContext.abortWith(ACCESS_UNAUTHORIZED);
	    }
	    return;
	}
	authService.updateActivity(email, authToken);
	/* Get method invoked. */
	Method methodInvoked = resourceInfo.getResourceMethod();
	/*
	 * Check for certain roles to be allowed...
	 */
	if (isOpenGlobally(requestContext)) {
	    return;
	} else {
	    if (authService.isAuthorizedAdministrator(email, authToken)) {
		/* User is an administrator, so she is allowed. */
		return;
	    } else if (methodInvoked.isAnnotationPresent(RolesAllowed.class)) {
		RolesAllowed rolesAllowedAnnotation = methodInvoked.getAnnotation(RolesAllowed.class);
		Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAllowedAnnotation.roles()));
		if (!authService.isAuthorized(email, authToken, rolesAllowed)) {
		    logger.warn("Insufficient privileges for email " + email + " on function '" + methodInvoked + "'.");
		    requestContext.abortWith(FORBIDDEN);
		}
		return;
	    } else if (methodInvoked.isAnnotationPresent(DenyAll.class)) {
		/* Globally forbidden method. */
		logger.warn("Forbidden for email " + email + " on function '" + methodInvoked + "'.");
		requestContext.abortWith(FORBIDDEN);
		return;
	    }
	}
	/*
	 * ATTENTION(!): DUE TO SECURITY REASONS, ALL REST METHODS ARE DENIED
	 * PER DEFAULT!
	 * 
	 * Functionality needs to be declared permitted or roles added to open
	 * methods into the public.
	 */
	logger.warn("Unspecified authorization " + email + " on function '" + methodInvoked + "'.");
	requestContext.abortWith(FORBIDDEN);
    }

    private void procressAuthToken(ContainerRequestContext requestContext) {
	/* Get AuthId and AuthToken from HTTP-Header. */
	String authIdHeader = requestContext.getHeaderString(AuthElement.AUTH_ID_HEADER);
	String authTokenHeader = requestContext.getHeaderString(AuthElement.AUTH_TOKEN_HEADER);
	if (StringUtils.isEmpty(authTokenHeader) || StringUtils.isEmpty(authIdHeader)) {
	    if (!isOpenGlobally(requestContext)) {
		/*
		 * Method is not globally open and we do not have authentication
		 * data, so we need to ask for it.
		 */
		logger.warn("User not authenticated.");
		requestContext.abortWith(ACCESS_UNAUTHORIZED);
	    }
	    return;
	}
	/* Check email address format and convert it... */
	try {
	    email = new EmailAddress(authIdHeader);
	} catch (IllegalArgumentException e) {
	    if (!isOpenGlobally(requestContext)) {
		logger.warn("Email address '" + authIdHeader + "' is invalid.");
		requestContext.abortWith(ACCESS_UNAUTHORIZED);
	    }
	    return;
	}
	/* Check authentication token and convert it... */
	try {
	    authToken = UUID.fromString(authTokenHeader);
	} catch (IllegalArgumentException e) {
	    if (!isOpenGlobally(requestContext)) {
		logger.warn("Invalid " + AuthElement.AUTH_TOKEN_HEADER + " '" + authTokenHeader + "'.");
		requestContext.abortWith(ACCESS_UNAUTHORIZED);
	    }
	    return;
	}
    }

    private boolean isOpenGlobally(ContainerRequestContext requestContext) {
	/* Get method invoked. */
	Method methodInvoked = resourceInfo.getResourceMethod();
	/* Check if open method... */
	if (methodInvoked.isAnnotationPresent(PermitAll.class)) {
	    /*
	     * Method is globally permitted, so we proceed without interactions.
	     */
	    return true;
	}
	/* Check whether anonymous is allowed to read... */
	if ((anonymousCanRead) && (HttpMethod.GET.equals(requestContext.getMethod()))) {
	    /*
	     * Anonymous is allowed read, so we can open GET methods globally
	     * and skip all other tests for performance reasons.
	     */
	    return true;
	}
	return false;
    }

}
