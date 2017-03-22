package com.puresoltechnologies.lifeassist.app.rest.services;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @GET
    @Path("/timezones")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getTimezones() {
	List<String> timezones = new ArrayList<>();
	for (String zoneId : ZoneId.getAvailableZoneIds()) {
	    timezones.add(zoneId);
	}
	Collections.sort(timezones);
	return timezones;
    }

}
