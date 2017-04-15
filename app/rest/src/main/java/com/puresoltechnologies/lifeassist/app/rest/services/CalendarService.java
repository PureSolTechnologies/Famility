package com.puresoltechnologies.lifeassist.app.rest.services;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarYear;
import com.puresoltechnologies.lifeassist.app.api.calendar.TimeZoneInformation;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Appointment;
import com.puresoltechnologies.lifeassist.app.impl.calendar.AppointmentSerie;
import com.puresoltechnologies.lifeassist.app.impl.calendar.AppointmentType;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarFactory;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.impl.calendar.DurationUnit;

@Path("/calendar")
public class CalendarService {

    private static final Field appointmentIdField;
    private static final Field appointmentSerieIdField;
    static {
	try {
	    appointmentIdField = Appointment.class.getDeclaredField("id");
	    appointmentIdField.setAccessible(true);
	    appointmentSerieIdField = AppointmentSerie.class.getDeclaredField("id");
	    appointmentSerieIdField.setAccessible(true);
	} catch (NoSuchFieldException | SecurityException e) {
	    throw new RuntimeException(e);
	}
    }

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
    public List<TimeZoneInformation> getTimezones(@QueryParam("dateTime") String dateTime,
	    @QueryParam("language") String language, @QueryParam("zoneId") String zoneId) {
	Instant instant;
	if (dateTime == null) {
	    instant = Instant.now();
	} else {
	    LocalDateTime localDateTime = LocalDateTime.from(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(dateTime));
	    ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime,
		    zoneId == null ? ZoneId.systemDefault() : ZoneId.of(zoneId));
	    instant = Instant.from(zonedDateTime);
	}
	if (language == null) {
	    language = Locale.ENGLISH.getLanguage();
	}
	return calendarManager.getTimezones(instant, new Locale(language));
    }

    @GET
    @Path("/appointments/types")
    @Produces(MediaType.APPLICATION_JSON)
    public AppointmentType[] getAppointmentTypes() {
	return calendarManager.getAppointmentTypes();
    }

    @GET
    @Path("/appointments/duration-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getDurationUnits() {
	return calendarManager.getDurationUnits();
    }

    @GET
    @Path("/appointments/reminder-duration-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getReminderDurationUnits() {
	return calendarManager.getReminderDurationUnits();
    }

    @PUT
    @Path("/appointments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAppointment(@Context UriInfo uriInfo, Appointment appointment)
	    throws SQLException, IllegalArgumentException, IllegalAccessException {
	long id = calendarManager.createAppointment(appointment);
	appointmentIdField.set(appointment, id);
	ResponseBuilder created = Response.created(uriInfo.getRequestUri().resolve(String.valueOf(id)));
	created.entity(appointment);
	return created.build();
    }

    @POST
    @Path("/appointments/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateAppointment(@PathParam("id") long id, Appointment appointment) throws SQLException {
	boolean updated = calendarManager.updateAppointment(id, appointment);
	if (!updated) {
	    throw new NotFoundException("Appointment id '" + id + "' was not found.");
	}
	return updated;
    }

    @GET
    @Path("/appointments/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Appointment getAppointment(@PathParam("id") long id) throws SQLException {
	Appointment appointment = calendarManager.getAppointment(id);
	if (appointment == null) {
	    throw new NotFoundException("Appointment id '" + id + "' was not found.");
	}
	return appointment;
    }

    @DELETE
    @Path("/appointments/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeAppointment(@PathParam("id") long id) throws SQLException {
	boolean deleted = calendarManager.removeAppointment(id);
	if (!deleted) {
	    throw new NotFoundException("Appointment id '" + id + "' was not found.");
	}
	return deleted;
    }

    @PUT
    @Path("/appointmentSeries")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAppointmentSeries(@Context UriInfo uriInfo, AppointmentSerie appointmentSerie)
	    throws SQLException, IllegalArgumentException, IllegalAccessException {
	long id = calendarManager.createAppointmentSerie(appointmentSerie);
	appointmentSerieIdField.set(appointmentSerie, id);
	ResponseBuilder created = Response.created(uriInfo.getRequestUri().resolve(String.valueOf(id)));
	created.entity(appointmentSerie);
	return created.build();
    }

    @POST
    @Path("/appointmentSeries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateAppointmentSeries(@PathParam("id") long id, AppointmentSerie appointmentSerie)
	    throws SQLException {
	boolean updated = calendarManager.updateAppointmentSerie(id, appointmentSerie);
	if (!updated) {
	    throw new NotFoundException("Appointment id '" + id + "' was not found.");
	}
	return updated;
    }

    @GET
    @Path("/appointmentSeries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AppointmentSerie getAppointmentSeries(@PathParam("id") long id) throws SQLException {
	AppointmentSerie appointmentSerie = calendarManager.getAppointmentSerie(id);
	if (appointmentSerie == null) {
	    throw new NotFoundException("AppointmentSerie id '" + id + "' was not found.");
	}
	return appointmentSerie;
    }

    @DELETE
    @Path("/appointmentSeries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeAppointmentSeries(@PathParam("id") long id) throws SQLException {
	boolean deleted = calendarManager.removeAppointmentSerie(id);
	if (!deleted) {
	    throw new NotFoundException("AppointmentSerie id '" + id + "' was not found.");
	}
	return deleted;
    }

    @GET
    @Path("/appointments/year/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Appointment> getYearAppointments(@PathParam("year") int year) throws SQLException {
	return calendarManager.getYearAppointments(year);
    }

    @GET
    @Path("/appointments/year/{year}/month/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Appointment> getMonthAppointments(@PathParam("year") int year, @PathParam("month") int month)
	    throws SQLException {
	return calendarManager.getMonthAppointments(year, month);
    }

    @GET
    @Path("/appointments/year/{year}/month/{month}/day/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Appointment> getDayAppointments(@PathParam("year") int year, @PathParam("month") int month,
	    @PathParam("day") int day) throws SQLException {
	return calendarManager.getDayAppointments(year, month, day);
    }

}
