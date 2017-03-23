package com.puresoltechnologies.lifeassist.app.rest.services;

import java.sql.SQLException;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.commons.types.Password;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.passwords.PasswordStoreImpl;
import com.puresoltechnologies.passwordstore.api.PasswordStore;
import com.puresoltechnologies.passwordstore.domain.PasswordActivationException;
import com.puresoltechnologies.passwordstore.domain.PasswordChangeException;
import com.puresoltechnologies.passwordstore.domain.PasswordCreationException;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @PUT
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(@HeaderParam("email") String email, @HeaderParam("password") String password)
	    throws SQLException {
	PasswordStore passwordStore = new PasswordStoreImpl(DatabaseConnector.getConnection());
	if (passwordStore.authenticate(new EmailAddress(email), new Password(password))) {
	    return Response.ok().entity(email).build();
	} else {
	    return Response.status(Status.UNAUTHORIZED).build();
	}
    }

    @PUT
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLogin(@HeaderParam("email") String email, @HeaderParam("password") String password)
	    throws SQLException {
	try {
	    PasswordStore passwordStore = new PasswordStoreImpl(DatabaseConnector.getConnection());
	    passwordStore.createPassword(new EmailAddress(email), new Password(password));
	    return Response.ok().entity(email).build();
	} catch (PasswordCreationException e) {
	    logger.warn("Could not create login for '" + email + "'.", e);
	    return Response.status(Status.CONFLICT).entity(e.getMessage()).build();
	}
    }

    @PUT
    @Path("/activate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateLogin(@HeaderParam("email") String email,
	    @HeaderParam("activation-key") String activationKey) throws SQLException {
	try {
	    PasswordStore passwordStore = new PasswordStoreImpl(DatabaseConnector.getConnection());
	    passwordStore.activatePassword(new EmailAddress(email), activationKey);
	    return Response.ok().entity(email).build();
	} catch (PasswordActivationException e) {
	    logger.warn("Could not activate login for '" + email + "'.", e);
	    return Response.status(Status.CONFLICT).entity(e.getMessage()).build();
	}
    }

    @PUT
    @Path("/change_password")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@HeaderParam("email") String email, @HeaderParam("password") String password,
	    @HeaderParam("new-password") String newPassword) throws SQLException {
	try {
	    PasswordStore passwordStore = new PasswordStoreImpl(DatabaseConnector.getConnection());
	    passwordStore.changePassword(new EmailAddress(email), new Password(password), new Password(newPassword));
	    return Response.ok().entity(email).build();
	} catch (PasswordChangeException e) {
	    logger.warn("Could not change password for '" + email + "'.", e);
	    return Response.status(Status.CONFLICT).entity(e.getMessage()).build();
	}
    }

    @PUT
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLogin(@HeaderParam("email") String email) throws SQLException {
	PasswordStore passwordStore = new PasswordStoreImpl(DatabaseConnector.getConnection());
	passwordStore.deletePassword(new EmailAddress(email));
	return Response.ok().entity(email).build();
    }

}
