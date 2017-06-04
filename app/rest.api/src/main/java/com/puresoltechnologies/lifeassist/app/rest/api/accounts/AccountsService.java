package com.puresoltechnologies.lifeassist.app.rest.api.accounts;

import java.sql.SQLException;

import javax.ws.rs.core.Response;

public interface AccountsService {

    public Response createAccount(String email, String password) throws SQLException;

    public Response activateAccount(String email, String activationKey) throws SQLException;

    public Response changePassword(String email, String password, String newPassword) throws SQLException;

    public Response deleteAccount(String email) throws SQLException;

}
