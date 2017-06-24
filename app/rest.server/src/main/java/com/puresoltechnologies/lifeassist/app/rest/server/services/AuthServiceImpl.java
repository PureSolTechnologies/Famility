package com.puresoltechnologies.lifeassist.app.rest.server.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.commons.types.Password;
import com.puresoltechnologies.lifeassist.app.impl.accounts.AccountManager;
import com.puresoltechnologies.lifeassist.app.impl.accounts.User;
import com.puresoltechnologies.lifeassist.app.impl.passwords.PasswordStoreImpl;
import com.puresoltechnologies.lifeassist.app.rest.api.auth.AuthService;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.AuthElement;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.PermitAll;
import com.puresoltechnologies.lifeassist.app.rest.server.auth.SupportedRoles;
import com.puresoltechnologies.passwordstore.api.PasswordStore;

/**
 * This is a service and singleton to login and logout users and to keep track
 * of auth-tokens.
 */
@Path("/auth")
public class AuthServiceImpl implements AuthService {

    private static final AuthService instance;
    static {
	instance = new AuthServiceImpl();
    }

    public static AuthService getInstance() {
	return instance;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final int MINUTES_TO_MILLISECONDS = 60000;

    // An authentication token storage which stores <auth_token>.
    private final Map<UUID, EmailAddress> authorizationTokensStorage = new HashMap<>();
    private final Map<UUID, User> users = new HashMap<>();
    private final Map<UUID, Date> sessionStarts = new HashMap<>();
    private final Map<UUID, Date> lastActivities = new HashMap<>();

    private int userInactivityTimeout = 3600;
    private int userSessionTimeout = 3600 * 24 * 7;

    /**
     * Private constructor to avoid instantiation.
     */
    private AuthServiceImpl() {
    }

    @Override
    @PUT
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(@HeaderParam("email") String emailString, @HeaderParam("password") String password)
	    throws SQLException {
	UUID authToken = login(new EmailAddress(emailString), new Password(password));
	if (authToken != null) {
	    ResponseBuilder responseBuilder = Response.ok();
	    responseBuilder.header(AuthElement.AUTH_ID_HEADER, emailString);
	    responseBuilder.header(AuthElement.AUTH_TOKEN_HEADER, authToken.toString());
	    return responseBuilder.entity(emailString + " was successfully authenticated.").build();
	} else {
	    return Response.status(Status.UNAUTHORIZED)
		    .entity("Email '" + emailString + "' unknown or password invalid.").build();
	}
    }

    @Override
    public UUID login(EmailAddress email, Password password) throws SQLException {
	PasswordStore passwordStore = new PasswordStoreImpl();
	if (passwordStore.authenticate(email, password)) {
	    /*
	     * Once all parameters are matched, the authToken will be generated
	     * and will be stored in the authorizationTokensStorage. The
	     * authToken will be needed for every REST API invocation and is
	     * only valid within the login session
	     */
	    UUID authToken = UUID.randomUUID();
	    authorizationTokensStorage.put(authToken, email);
	    Date time = new Date();
	    sessionStarts.put(authToken, time);
	    lastActivities.put(authToken, time);
	    return authToken;
	} else {
	    return null;
	}
    }

    @Override
    @PUT
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response logout(@HeaderParam("email") String emailString, @HeaderParam("auth-token") String authToken) {
	if (authorizationTokensStorage.containsKey(authToken)
		&& (authorizationTokensStorage.get(authToken).getAddress().equals(emailString))) {
	    authorizationTokensStorage.remove(authToken);
	    lastActivities.remove(authToken);
	    sessionStarts.remove(authToken);
	    users.remove(authToken);
	    NewCookie authTokenCookie = new NewCookie(AuthElement.AUTH_TOKEN_HEADER, "", "/", "*", "", 0, false, false);
	    return Response.ok().cookie(authTokenCookie).build();
	} else {
	    throw new BadRequestException(
		    "User '" + emailString + "' could not be logged out. User was either not logged in or is unknown.");
	}
    }

    /**
     * The method that pre-validates if the client which invokes the REST API is
     * from a authorized and authenticated source.
     *
     * @param authToken
     *            The authorization token generated after login
     * @return TRUE for acceptance and FALSE for denied.
     */
    public boolean isAuthTokenValid(String authToken) {
	if (authorizationTokensStorage.containsKey(authToken)) {
	    return true;
	}
	return false;
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public void logout(UUID authToken) {
	if (authorizationTokensStorage.containsKey(authToken)) {
	    authorizationTokensStorage.remove(authToken);
	    lastActivities.remove(authToken);
	    sessionStarts.remove(authToken);
	    return;
	}
	throw new BadRequestException("Invalid authorization token.");
    }

    private User findByEmailAndAuthToken(EmailAddress email, UUID authToken) throws SQLException {
	AccountManager accountManager = new AccountManager();
	EmailAddress userId = authorizationTokensStorage.get(authToken);
	if ((userId != null) && (userId.equals(email))) {
	    User user = users.get(authToken);
	    if (user != null) {
		return user;
	    }
	    user = accountManager.getUser(email);
	    users.put(authToken, user);
	    return user;
	}
	return null;
    }

    @Override
    public boolean isAuthorized(EmailAddress email, UUID authToken, Set<String> rolesAllowed) throws SQLException {
	User user = findByEmailAndAuthToken(email, authToken);
	if (user != null) {
	    return rolesAllowed.contains(user.getRole().getId());
	} else {
	    return false;
	}
    }

    @Override
    public boolean isAuthorizedAdministrator(EmailAddress email, UUID authToken) throws SQLException {
	User user = findByEmailAndAuthToken(email, authToken);
	if (user == null) {
	    return false;
	}
	return SupportedRoles.ADMINISTRATOR.getId().equals(user.getRole().getId());
    }

    @Override
    public boolean isAuthenticated(EmailAddress email, UUID authToken) {
	if ((email == null) || (authToken == null)) {
	    return false;
	}
	EmailAddress emailAddress = authorizationTokensStorage.get(authToken);
	if (emailAddress == null) {
	    return false;
	}
	if (!emailAddress.equals(email)) {
	    logout(authToken);
	    return false;
	}
	Date time = new Date();
	Date sessionStart = sessionStarts.get(authToken);
	if ((time.getTime() - sessionStart.getTime()) > (userSessionTimeout * MINUTES_TO_MILLISECONDS)) {
	    logout(authToken);
	    return false;
	}
	Date lastActivity = lastActivities.get(authToken);
	if ((time.getTime() - lastActivity.getTime()) > (userInactivityTimeout * MINUTES_TO_MILLISECONDS)) {
	    logout(authToken);
	    return false;
	}
	return true;
    }

    @Override
    public void updateActivity(EmailAddress email, UUID authToken) {
	if (isAuthenticated(email, authToken)) {
	    lastActivities.put(authToken, new Date());
	}
    }

}
