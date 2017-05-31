package com.puresoltechnologies.lifeassist.app.rest.server.auth;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.commons.types.Password;
import com.puresoltechnologies.lifeassist.app.impl.accounts.AccountManager;
import com.puresoltechnologies.lifeassist.app.impl.accounts.User;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.passwords.PasswordStoreImpl;
import com.puresoltechnologies.passwordstore.api.PasswordStore;

public class AuthServiceBean {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceBean.class);
    private static final int MINUTES_TO_MILLISECONDS = 60000;
    private static final AuthServiceBean instance = new AuthServiceBean();

    public static AuthServiceBean getInstance() {
	return instance;
    }

    // An authentication token storage which stores <auth_token>.
    private final Map<UUID, EmailAddress> authorizationTokensStorage = new HashMap<>();
    private final Map<UUID, User> users = new HashMap<>();
    private final Map<UUID, Date> sessionStarts = new HashMap<>();
    private final Map<UUID, Date> lastActivities = new HashMap<>();

    private int userInactivityTimeout = 3600;
    private int userSessionTimeout = 3600 * 24 * 7;

    private AuthServiceBean() {
    }

    public String login(EmailAddress email, Password password) throws LoginException {
	try (Connection connection = DatabaseConnector.getConnection()) {
	    PasswordStore passwordStore = new PasswordStoreImpl(connection);
	    if (passwordStore.authenticate(email, password)) {
		/*
		 * Once all parameters are matched, the authToken will be
		 * generated and will be stored in the
		 * authorizationTokensStorage. The authToken will be needed for
		 * every REST API invocation and is only valid within the login
		 * session
		 */
		UUID authToken = UUID.randomUUID();
		authorizationTokensStorage.put(authToken, email);
		Date time = new Date();
		sessionStarts.put(authToken, time);
		lastActivities.put(authToken, time);
		return authToken.toString();
	    }
	    throw new LoginException("Authentication information are invalid.");
	} catch (SQLException e) {
	    logger.error("Authentication failed", e);
	    throw new LoginException("Authentication failed.");
	}
    }

    public void logout(EmailAddress email, UUID authToken) throws LoginException {
	if (authorizationTokensStorage.containsKey(authToken)
		&& (authorizationTokensStorage.get(authToken).equals(email))) {
	    authorizationTokensStorage.remove(authToken);
	    lastActivities.remove(authToken);
	    sessionStarts.remove(authToken);
	    users.remove(authToken);
	} else {
	    throw new LoginException(
		    "User '" + email + "' could not be logged out. User was either not logged in or is unknown.");
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

    public void logout(UUID authToken) throws GeneralSecurityException {
	if (authorizationTokensStorage.containsKey(authToken)) {
	    authorizationTokensStorage.remove(authToken);
	    lastActivities.remove(authToken);
	    sessionStarts.remove(authToken);
	    return;
	}
	throw new GeneralSecurityException("Invalid authorization token.");
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

    public boolean isAuthorized(EmailAddress email, UUID authToken, Set<String> rolesAllowed) throws SQLException {
	User user = findByEmailAndAuthToken(email, authToken);
	if (user != null) {
	    return rolesAllowed.contains(user.getRole().getId());
	} else {
	    return false;
	}
    }

    public boolean isAuthorizedAdministrator(EmailAddress email, UUID authToken) throws SQLException {
	User user = findByEmailAndAuthToken(email, authToken);
	if (user == null) {
	    return false;
	}
	return SupportedRoles.ADMINISTRATOR.getId().equals(user.getRole().getId());
    }

    public boolean isAuthenticated(EmailAddress email, UUID authToken) {
	try {
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
	} catch (GeneralSecurityException e) {
	    return false;
	}
    }

    public void updateActivity(EmailAddress email, UUID authToken) {
	if (isAuthenticated(email, authToken)) {
	    lastActivities.put(authToken, new Date());
	}
    }

}