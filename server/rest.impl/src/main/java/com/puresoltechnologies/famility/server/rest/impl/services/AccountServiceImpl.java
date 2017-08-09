package com.puresoltechnologies.famility.server.rest.impl.services;

import java.sql.SQLException;

import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.commons.types.Password;
import com.puresoltechnologies.famility.server.impl.passwords.PasswordStoreImpl;
import com.puresoltechnologies.famility.server.rest.api.accounts.AccountsService;
import com.puresoltechnologies.famility.server.rest.impl.auth.PermitAll;
import com.puresoltechnologies.famility.server.rest.impl.auth.RolesAllowed;
import com.puresoltechnologies.passwordstore.api.PasswordStore;
import com.puresoltechnologies.passwordstore.domain.PasswordActivationException;
import com.puresoltechnologies.passwordstore.domain.PasswordChangeException;
import com.puresoltechnologies.passwordstore.domain.PasswordCreationException;

@Timed
@ExceptionMetered
@Path("/accounts")
public class AccountServiceImpl implements AccountsService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    @POST
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(roles = { "Administrator" })
    public Response createAccount(@PathParam("email") String email, @HeaderParam("password") String password)
	    throws SQLException {
	try {
	    PasswordStore passwordStore = new PasswordStoreImpl();
	    passwordStore.createPassword(new EmailAddress(email), new Password(password));
	    return Response.ok().entity(email).build();
	} catch (PasswordCreationException e) {
	    logger.warn("Could not create login for '" + email + "'.", e);
	    return Response.status(Status.CONFLICT).entity(e.getMessage()).build();
	}
    }

    @Override
    @POST
    @Path("/{email}/activate/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response activateAccount(@PathParam("email") String email, @PathParam("key") String activationKey)
	    throws SQLException {
	try {
	    PasswordStore passwordStore = new PasswordStoreImpl();
	    passwordStore.activatePassword(new EmailAddress(email), activationKey);
	    return Response.ok().entity(email).build();
	} catch (PasswordActivationException e) {
	    logger.warn("Could not activate login for '" + email + "'.", e);
	    return Response.status(Status.CONFLICT).entity(e.getMessage()).build();
	}
    }

    @Override
    @POST
    @Path("/{email}/change_password")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@PathParam("email") String email, @HeaderParam("password") String password,
	    @HeaderParam("new-password") String newPassword) throws SQLException {
	try {
	    PasswordStore passwordStore = new PasswordStoreImpl();
	    passwordStore.changePassword(new EmailAddress(email), new Password(password), new Password(newPassword));
	    return Response.ok().entity(email).build();
	} catch (PasswordChangeException e) {
	    logger.warn("Could not change password for '" + email + "'.", e);
	    return Response.status(Status.CONFLICT).entity(e.getMessage()).build();
	}
    }

    @Override
    @DELETE
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(roles = { "Administrator" })
    public Response deleteAccount(@PathParam("email") String email) throws SQLException {
	PasswordStore passwordStore = new PasswordStoreImpl();
	passwordStore.deletePassword(new EmailAddress(email));
	return Response.ok().entity(email).build();
    }

}
