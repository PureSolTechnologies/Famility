package com.puresoltechnologies.famility.server.rest.api.auth;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.Response;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.commons.types.Password;

public interface AuthService {

    public Response login(String emailString, String password) throws SQLException;

    public UUID login(EmailAddress email, Password password) throws SQLException;

    public Response logout(String emailString, String authToken);

    public boolean isAuthenticated(EmailAddress email, UUID authToken);

    public void updateActivity(EmailAddress email, UUID authToken);

    public boolean isAuthorizedAdministrator(EmailAddress email, UUID authToken) throws SQLException;

    public boolean isAuthorized(EmailAddress email, UUID authToken, Set<String> rolesAllowed) throws SQLException;

}
