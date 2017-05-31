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
import javax.annotation.Priority;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.AuthElement;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.AuthServiceBean;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.DenyAll;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.PermitAll;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.RolesAllowed;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationAndAuthorizationFilter implements ContainerResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAndAuthorizationFilter.class);

    // 401 - Access denied
    private static final Response ACCESS_UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build();
    // 403 - Forbidden
    private static final Response FORBIDDEN = Response.status(Response.Status.FORBIDDEN).build();

    private AuthServiceBean authService = null;

    @PostConstruct
    public void postConstruct() {
	authService = AuthServiceBean.getInstance();
	logger.info("Authentication and authorization is enabled.");
    }

    @PreDestroy
    public void preDestroy() {
	logger.info("Authentication and authorization was disabled.");
    }

    @Context
    private ResourceInfo resourceInfo;

    private boolean anonymousCanRead = true; // TODO specify this via global
					     // setting

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
	    throws IOException {
	/* Get AuthId and AuthToken from HTTP-Header. */
	String authIdString = requestContext.getHeaderString(AuthElement.AUTH_ID_HEADER);
	String authTokenString = requestContext.getHeaderString(AuthElement.AUTH_TOKEN_HEADER);
	if ((authIdString == null) || (authIdString.isEmpty()) || (authTokenString == null)
		|| (authTokenString.isEmpty())) {
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
	EmailAddress email;
	try {
	    email = new EmailAddress(authIdString);
	} catch (IllegalArgumentException e) {
	    if (!isOpenGlobally(requestContext)) {
		logger.warn("Email address '" + authIdString + "' is invalid.");
		requestContext.abortWith(ACCESS_UNAUTHORIZED);
	    }
	    return;
	}
	/* Check authentication token and convert it... */
	UUID authToken;
	try {
	    authToken = UUID.fromString(authTokenString);
	} catch (IllegalArgumentException e) {
	    if (!isOpenGlobally(requestContext)) {
		logger.warn("Invalid auth-token '" + authTokenString + "'.");
		requestContext.abortWith(ACCESS_UNAUTHORIZED);
	    }
	    return;
	}
	/* Check for correct authentication data... */
	if (authService.isAuthenticated(email, authToken)) {
	    authService.updateActivity(email, authToken);
	} else {
	    if (!isOpenGlobally(requestContext)) {
		/*
		 * Authentication data does not fit, so we do not need to check
		 * further...
		 */
		logger.warn("Invalid email " + email + " and auth-token '" + authTokenString + "'.");
		requestContext.abortWith(ACCESS_UNAUTHORIZED);
	    }
	    return;
	}
	/* Get method invoked. */
	Method methodInvoked = resourceInfo.getResourceMethod();
	/*
	 * Check for certain roles to be allowed...
	 */
	if (isOpenGlobally(requestContext)) {
	    return;
	} else
	    try {
		if (authService.isAuthorizedAdministrator(email, authToken)) {
		    /* User is an administrator, so she is allowed. */
		    return;
		} else if (methodInvoked.isAnnotationPresent(RolesAllowed.class)) {
		    RolesAllowed rolesAllowedAnnotation = methodInvoked.getAnnotation(RolesAllowed.class);
		    Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAllowedAnnotation.roles()));
		    if (!authService.isAuthorized(email, authToken, rolesAllowed)) {
			logger.warn(
				"Insufficient privileges for email " + email + " on function '" + methodInvoked + "'.");
			requestContext.abortWith(FORBIDDEN);
		    }
		    return;
		} else if (methodInvoked.isAnnotationPresent(DenyAll.class)) {
		    /* Globally forbidden method. */
		    logger.warn("Forbidden for email " + email + " on function '" + methodInvoked + "'.");
		    requestContext.abortWith(FORBIDDEN);
		    return;
		}
	    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
