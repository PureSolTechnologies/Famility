package com.puresoltechnologies.lifeassist.app.impl.rest;

import java.sql.SQLException;

import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.commons.types.Password;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.passwords.PasswordStoreImpl;
import com.puresoltechnologies.passwordstore.api.PasswordStore;

@Path("/rest/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginServiceResource {

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(@FormParam("email") String email, @FormParam("password") String password)
	    throws SQLException {
	PasswordStore passwordStore = new PasswordStoreImpl(DatabaseConnector.getConnection());
	if (passwordStore.authenticate(new EmailAddress(email), new Password(password))) {
	    return Response.ok().entity(email).build();
	} else {
	    return Response.status(Status.UNAUTHORIZED).build();
	}
    }

}
