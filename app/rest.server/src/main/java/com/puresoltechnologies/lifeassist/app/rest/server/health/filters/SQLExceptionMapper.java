package com.puresoltechnologies.lifeassist.app.rest.server.health.filters;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class SQLExceptionMapper implements ExceptionMapper<SQLException> {

    private static final Logger logger = LoggerFactory.getLogger(SQLExceptionMapper.class);

    @Override
    public Response toResponse(SQLException exception) {
	logger.error("SQLException occured during server call.", exception);
	return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
    }

}
