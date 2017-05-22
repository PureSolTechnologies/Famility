package com.puresoltechnologies.lifeassist.app.rest.api.login;

import java.sql.SQLException;

import javax.ws.rs.core.Response;

public interface LoginService {

    public Response authenticate(String email, String password) throws SQLException;

    public Response createLogin(String email, String password) throws SQLException;

    public Response activateLogin(String email, String activationKey) throws SQLException;

    public Response changePassword(String email, String password, String newPassword) throws SQLException;

    public Response createLogin(String email) throws SQLException;

}
