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

import com.puresoltechnologies.lifeassist.app.api.calendar.Entry;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarEntry;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarFactory;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarSeries;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarService;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarYear;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.DurationUnit;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.EntryType;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.TimeZoneInformation;

@Path("/calendar")
public class CalendarServiceImpl implements CalendarService {

    private static final Field entryIdField;
    private static final Field entrySerieIdField;
    static {
	try {
	    entryIdField = CalendarEntry.class.getDeclaredField("id");
	    entryIdField.setAccessible(true);
	    entrySerieIdField = CalendarSeries.class.getDeclaredField("id");
	    entrySerieIdField.setAccessible(true);
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
    @Path("/entries/types")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EntryType> getEntryTypes() throws SQLException {
	return RestConverter.convertEntryTypes(calendarManager.getEntryTypes());
    }

    @Override
    @GET
    @Path("/entries/duration-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getDurationUnits() {
	return RestConverter.convertDurationUnits(calendarManager.getDurationUnits());
    }

    @Override
    @GET
    @Path("/entries/reminder-duration-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getReminderDurationUnits() {
	return RestConverter.convertDurationUnits(calendarManager.getReminderDurationUnits());
    }

    @Override
    @GET
    @Path("/entries/turnus-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getTurnusUnits() {
	return RestConverter.convertDurationUnits(calendarManager.getTurnusUnits());
    }

    @Override
    @GET
    @Path("/entries/today")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEntry> getEntriesToday(@QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getEntriesToday(type));
    }

    @Override
    @GET
    @Path("/entries/tomorrow")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEntry> getEntriesTomorrow(@QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getEntriesTomorrow(type));
    }

    @Override
    @GET
    @Path("/entries")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEntry> getEntries(@QueryParam("type") String type, @QueryParam("from") String from,
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
    @Path("/entries")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertEntry(@Context UriInfo uriInfo, CalendarEntry entry)
	    throws SQLException, IllegalArgumentException, IllegalAccessException {
	long id = calendarManager
		.insertEntry(com.puresoltechnologies.lifeassist.app.rest.server.services.RestConverter.convert(entry));
	entryIdField.set(entry, id);
	ResponseBuilder created = Response.created(uriInfo.getRequestUri().resolve(String.valueOf(id)));
	created.entity(entry);
	return created.build();
    }

    @Override
    @POST
    @Path("/entries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateEntry(@PathParam("id") long id, CalendarEntry entry) throws SQLException {
	boolean updated = calendarManager.updateEntry(id, RestConverter.convert(entry));
	if (!updated) {
	    throw new NotFoundException("Entry id '" + id + "' was not found.");
	}
	return updated;
    }

    @Override
    @GET
    @Path("/entries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CalendarEntry getEntry(@PathParam("id") long id) throws SQLException {
	Entry entry = calendarManager.getEntry(id);
	if (entry == null) {
	    throw new NotFoundException("Entry id '" + id + "' was not found.");
	}
	return RestConverter.convert(entry);
    }

    @Override
    @DELETE
    @Path("/entries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeEntry(@PathParam("id") long id) throws SQLException {
	boolean deleted = calendarManager.removeEntry(id);
	if (!deleted) {
	    throw new NotFoundException("Entry id '" + id + "' was not found.");
	}
	return deleted;
    }

    @Override
    @PUT
    @Path("/entrieseries")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertSeries(@Context UriInfo uriInfo, CalendarSeries entrySerie)
	    throws SQLException, IllegalArgumentException, IllegalAccessException {
	long id = calendarManager.insertSeries(RestConverter.convert(entrySerie));
	entrySerieIdField.set(entrySerie, id);
	ResponseBuilder created = Response.created(uriInfo.getRequestUri().resolve(String.valueOf(id)));
	created.entity(entrySerie);
	return created.build();
    }

    @Override
    @POST
    @Path("/entrieseries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateSeries(@PathParam("id") long id, CalendarSeries entrySerie) throws SQLException {
	boolean updated = calendarManager.updateSeries(id,
		com.puresoltechnologies.lifeassist.app.rest.server.services.RestConverter.convert(entrySerie));
	if (!updated) {
	    throw new NotFoundException("Entry id '" + id + "' was not found.");
	}
	return updated;
    }

    @Override
    @GET
    @Path("/entrieseries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CalendarSeries getEntrySerie(@PathParam("id") long id) throws SQLException {
	CalendarSeries entrySerie = com.puresoltechnologies.lifeassist.app.rest.server.services.RestConverter
		.convert(calendarManager.getSeries(id));
	if (entrySerie == null) {
	    throw new NotFoundException("EntrySerie id '" + id + "' was not found.");
	}
	return entrySerie;
    }

    @Override
    @DELETE
    @Path("/entrieseries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeSeries(@PathParam("id") long id) throws SQLException {
	boolean deleted = calendarManager.removeSeries(id);
	if (!deleted) {
	    throw new NotFoundException("EntrySerie id '" + id + "' was not found.");
	}
	return deleted;
    }

    @Override
    @GET
    @Path("/entries/year/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEntry> getYearEntries(@PathParam("year") int year, @QueryParam("type") String type)
	    throws SQLException {
	return RestConverter.convert(calendarManager.getYearEntries(year, type));
    }

    @Override
    @GET
    @Path("/entries/year/{year}/month/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEntry> getMonthEntries(@PathParam("year") int year, @PathParam("month") int month,
	    @QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getMonthEntries(year, month, type));
    }

    @Override
    @GET
    @Path("/entries/year/{year}/month/{month}/day/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEntry> getDayEntries(@PathParam("year") int year, @PathParam("month") int month,
	    @PathParam("day") int day, @QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getDayEntries(year, month, day, type));
    }

    @Override
    @GET
    @Path("/entries/year/{year}/week/{week}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEntry> getDayEntries(@PathParam("year") int year, @PathParam("week") int week,
	    @QueryParam("type") String type) throws SQLException {
	return RestConverter.convert(calendarManager.getWeekEntries(year, week, type));
    }

}
