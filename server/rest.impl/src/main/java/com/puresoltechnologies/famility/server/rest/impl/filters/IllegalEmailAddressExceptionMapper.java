package com.puresoltechnologies.famility.server.rest.impl.filters;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.puresoltechnologies.commons.types.IllegalEmailAddressException;

@Provider
public class IllegalEmailAddressExceptionMapper implements ExceptionMapper<IllegalEmailAddressException> {

    @Override
    public Response toResponse(IllegalEmailAddressException exception) {
	return Response.status(Status.NOT_ACCEPTABLE).entity(exception.getMessage()).build();
    }

}
