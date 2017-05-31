package com.puresoltechnologies.lifeassist.app.rest.server.services;

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

import javax.ws.rs.BadRequestException;
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

import com.puresoltechnologies.lifeassist.app.api.calendar.Event;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarEvent;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarFactory;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarSeries;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarService;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarYear;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.DurationUnit;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.EventType;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.TimeZoneInformation;

@Path("/calendar")
public class CalendarServiceImpl implements CalendarService {

    private static final Field eventIdField;
    private static final Field seriesIdField;
    static {
	try {
	    eventIdField = CalendarEvent.class.getDeclaredField("id");
	    eventIdField.setAccessible(true);
	    seriesIdField = CalendarSeries.class.getDeclaredField("id");
	    seriesIdField.setAccessible(true);
	} catch (NoSuchFieldException | SecurityException e) {
	    throw new RuntimeException(e);
	}
    }

    private static final CalendarManager calendarManager = new CalendarManager();

    @Override
    @GET
    @Path("/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public CalendarYear getYear(@PathParam("year") int year) {
	return CalendarFactory.createYear(year);
    }

    @Override
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
	return RestConverter.convertTimezones(calendarManager.getTimezones(instant, new Locale(language)));
    }

    @Override
    @GET
    @Path("/event/types")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventType> getEventTypes() throws SQLException {
	return RestConverter.convertEventTypes(calendarManager.getEventTypes());
    }

    @Override
    @GET
    @Path("/events/duration-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getDurationUnits() {
	return RestConverter.convertDurationUnits(calendarManager.getDurationUnits());
    }

    @Override
    @GET
    @Path("/events/reminder-duration-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getReminderDurationUnits() {
	return RestConverter.convertDurationUnits(calendarManager.getReminderDurationUnits());
    }

    @Override
    @GET
    @Path("/events/turnus-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getTurnusUnits() {
	return RestConverter.convertDurationUnits(calendarManager.getTurnusUnits());
    }

    @Override
    @GET
    @Path("/events/today")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEvent> getEntriesToday(@QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getEntriesToday(type));
    }

    @Override
    @GET
    @Path("/events/tomorrow")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEvent> getEntriesTomorrow(@QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getEntriesTomorrow(type));
    }

    @Override
    @GET
    @Path("/events")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEvent> getEntries(@QueryParam("type") String type, @QueryParam("from") String from,
	    @QueryParam("to") String to) throws SQLException {
	if ((from == null) || (to == null)) {
	    throw new BadRequestException("Query parameters 'from' and 'to' are mendatory.");
	}
	LocalDateTime fromTimestamp = LocalDateTime.parse(from);
	LocalDateTime toTimestamp = LocalDateTime.parse(to);
	return RestConverter.convert(calendarManager.getEntriesBetween(type, fromTimestamp, toTimestamp));
    }

    @Override
    @PUT
    @Path("/events")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertEvent(@Context UriInfo uriInfo, CalendarEvent event)
	    throws SQLException, IllegalArgumentException, IllegalAccessException {
	long id = calendarManager
		.insertEvent(com.puresoltechnologies.lifeassist.app.rest.server.services.RestConverter.convert(event));
	eventIdField.set(event, id);
	ResponseBuilder created = Response.created(uriInfo.getRequestUri().resolve(String.valueOf(id)));
	created.entity(event);
	return created.build();
    }

    @Override
    @POST
    @Path("/events/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateEvent(@PathParam("id") long id, CalendarEvent event) throws SQLException {
	boolean updated = calendarManager.updateEvent(id, RestConverter.convert(event));
	if (!updated) {
	    throw new NotFoundException("Event id '" + id + "' was not found.");
	}
	return updated;
    }

    @Override
    @GET
    @Path("/events/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CalendarEvent getEvent(@PathParam("id") long id) throws SQLException {
	Event event = calendarManager.getEvent(id);
	if (event == null) {
	    throw new NotFoundException("Event id '" + id + "' was not found.");
	}
	return RestConverter.convert(event);
    }

    @Override
    @DELETE
    @Path("/events/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeEvent(@PathParam("id") long id) throws SQLException {
	boolean deleted = calendarManager.removeEvent(id);
	if (!deleted) {
	    throw new NotFoundException("Event id '" + id + "' was not found.");
	}
	return deleted;
    }

    @Override
    @PUT
    @Path("/series")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertSeries(@Context UriInfo uriInfo, CalendarSeries series)
	    throws SQLException, IllegalArgumentException, IllegalAccessException {
	long id = calendarManager.insertSeries(RestConverter.convert(series));
	seriesIdField.set(series, id);
	ResponseBuilder created = Response.created(uriInfo.getRequestUri().resolve(String.valueOf(id)));
	created.entity(series);
	return created.build();
    }

    @Override
    @POST
    @Path("/series/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateSeries(@PathParam("id") long id, CalendarSeries series) throws SQLException {
	boolean updated = calendarManager.updateSeries(id,
		com.puresoltechnologies.lifeassist.app.rest.server.services.RestConverter.convert(series));
	if (!updated) {
	    throw new NotFoundException("Series id '" + id + "' was not found.");
	}
	return updated;
    }

    @Override
    @GET
    @Path("/series/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CalendarSeries getSeries(@PathParam("id") long id) throws SQLException {
	CalendarSeries series = com.puresoltechnologies.lifeassist.app.rest.server.services.RestConverter
		.convert(calendarManager.getSeries(id));
	if (series == null) {
	    throw new NotFoundException("Series id '" + id + "' was not found.");
	}
	return series;
    }

    @Override
    @DELETE
    @Path("/series/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeSeries(@PathParam("id") long id) throws SQLException {
	boolean deleted = calendarManager.removeSeries(id);
	if (!deleted) {
	    throw new NotFoundException("Series id '" + id + "' was not found.");
	}
	return deleted;
    }

    @Override
    @GET
    @Path("/events/year/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEvent> getYearEntries(@PathParam("year") int year, @QueryParam("type") String type)
	    throws SQLException {
	return RestConverter.convert(calendarManager.getYearEntries(year, type));
    }

    @Override
    @GET
    @Path("/events/year/{year}/month/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEvent> getMonthEntries(@PathParam("year") int year, @PathParam("month") int month,
	    @QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getMonthEntries(year, month, type));
    }

    @Override
    @GET
    @Path("/events/year/{year}/month/{month}/day/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEvent> getDayEntries(@PathParam("year") int year, @PathParam("month") int month,
	    @PathParam("day") int day, @QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getDayEntries(year, month, day, type));
    }

    @Override
    @GET
    @Path("/events/year/{year}/week/{week}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEvent> getDayEntries(@PathParam("year") int year, @PathParam("week") int week,
	    @QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getWeekEntries(year, week, type));
    }

}
