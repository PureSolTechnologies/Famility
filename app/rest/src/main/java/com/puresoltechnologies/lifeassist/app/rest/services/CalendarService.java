package com.puresoltechnologies.lifeassist.app.rest.services;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarYear;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Appointment;
import com.puresoltechnologies.lifeassist.app.impl.calendar.AppointmentType;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarFactory;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;

@Path("/calendar")
public class CalendarService {

    private static final CalendarManager calendarManager = new CalendarManager();

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

    @GET
    @Path("/appointments/types")
    @Produces(MediaType.APPLICATION_JSON)
    public AppointmentType[] getAppointmentTypes() {
	return calendarManager.getAppointmentTypes();
    }

    @GET
    @Path("/appointments/time-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TimeUnit> getTimeUnits() {
	return calendarManager.getTimeUnits();
    }

    @PUT
    @Path("/appointments")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createAppointment(Appointment appointment) throws SQLException {
	calendarManager.createAppointment(appointment);
    }

    @GET
    @Path("/appointments/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Appointment> getYearAppointments(@PathParam("year") int year) {
	return calendarManager.getYearAppointments(year);
    }

    @GET
    @Path("/appointments/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Appointment> getMonthAppointments(@PathParam("year") int year, @PathParam("month") int month) {
	return calendarManager.getMonthAppointments(year, month);
    }

    @GET
    @Path("/appointments/{year}/{month}/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Appointment> getDayAppointments(@PathParam("year") int year, @PathParam("month") int month,
	    @PathParam("day") int day) {
	return calendarManager.getDayAppointments(year, month, day);
    }

}
