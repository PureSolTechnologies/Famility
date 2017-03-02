package com.puresoltechnologies.lifeassist.app.impl.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarYear;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarFactory;

@Path("/calendar")
public class CalendarServiceResource {

    @GET
    @Path("/year/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public CalendarYear getCalendarYear(@PathParam("year") int year) {
	return CalendarFactory.createYear(year);
    }

}
