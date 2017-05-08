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

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarYear;
import com.puresoltechnologies.lifeassist.app.api.calendar.EntryType;
import com.puresoltechnologies.lifeassist.app.api.calendar.TimeZoneInformation;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarFactory;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.impl.calendar.DurationUnit;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Entry;
import com.puresoltechnologies.lifeassist.app.impl.calendar.EntrySerie;

@Path("/calendar")
public class CalendarService {

    private static final Field entryIdField;
    private static final Field entrySerieIdField;
    static {
	try {
	    entryIdField = Entry.class.getDeclaredField("id");
	    entryIdField.setAccessible(true);
	    entrySerieIdField = EntrySerie.class.getDeclaredField("id");
	    entrySerieIdField.setAccessible(true);
	} catch (NoSuchFieldException | SecurityException e) {
	    throw new RuntimeException(e);
	}
    }

    private static final CalendarManager calendarManager = new CalendarManager();

    @GET
    @Path("/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public CalendarYear getYear(@PathParam("year") int year) {
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
    @Path("/entries/types")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EntryType> getEntryTypes() throws SQLException {
	return calendarManager.getEntryTypes();
    }

    @GET
    @Path("/entries/duration-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getDurationUnits() {
	return calendarManager.getDurationUnits();
    }

    @GET
    @Path("/entries/reminder-duration-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getReminderDurationUnits() {
	return calendarManager.getReminderDurationUnits();
    }

    @GET
    @Path("/entries/turnus-units")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DurationUnit> getTurnusUnits() {
	return calendarManager.getTurnusUnits();
    }

    @GET
    @Path("/entries/today")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Entry> getEntriesToday(@QueryParam("type") String type) throws SQLException {
	return calendarManager.getEntriesToday(type);
    }

    @GET
    @Path("/entries/tomorrow")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Entry> getEntriesTomorrow(@QueryParam("type") String type) throws SQLException {
	return calendarManager.getEntriesTomorrow(type);
    }

    @GET
    @Path("/entries")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Entry> getEntries(@QueryParam("type") String type, @QueryParam("from") String from,
	    @QueryParam("to") String to) throws SQLException {
	if ((from == null) || (to == null)) {
	    throw new BadRequestException("Query parameters 'from' and 'to' are mendatory.");
	}
	LocalDateTime fromTimestamp = LocalDateTime.parse(from);
	LocalDateTime toTimestamp = LocalDateTime.parse(to);
	return calendarManager.getEntriesBetween(type, fromTimestamp, toTimestamp);
    }

    @PUT
    @Path("/entries")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertEntry(@Context UriInfo uriInfo, Entry entry)
	    throws SQLException, IllegalArgumentException, IllegalAccessException {
	long id = calendarManager.insertEntry(entry);
	entryIdField.set(entry, id);
	ResponseBuilder created = Response.created(uriInfo.getRequestUri().resolve(String.valueOf(id)));
	created.entity(entry);
	return created.build();
    }

    @POST
    @Path("/entries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateEntry(@PathParam("id") long id, Entry entry) throws SQLException {
	boolean updated = calendarManager.updateEntry(id, entry);
	if (!updated) {
	    throw new NotFoundException("Entry id '" + id + "' was not found.");
	}
	return updated;
    }

    @GET
    @Path("/entries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Entry getEntry(@PathParam("id") long id) throws SQLException {
	Entry entry = calendarManager.getEntry(id);
	if (entry == null) {
	    throw new NotFoundException("Entry id '" + id + "' was not found.");
	}
	return entry;
    }

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

    @PUT
    @Path("/entrieseries")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertEntrySerie(@Context UriInfo uriInfo, EntrySerie entrySerie)
	    throws SQLException, IllegalArgumentException, IllegalAccessException {
	long id = calendarManager.insertEntrySerie(entrySerie);
	entrySerieIdField.set(entrySerie, id);
	ResponseBuilder created = Response.created(uriInfo.getRequestUri().resolve(String.valueOf(id)));
	created.entity(entrySerie);
	return created.build();
    }

    @POST
    @Path("/entrieseries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateEntrySerie(@PathParam("id") long id, EntrySerie entrySerie) throws SQLException {
	boolean updated = calendarManager.updateEntrySerie(id, entrySerie);
	if (!updated) {
	    throw new NotFoundException("Entry id '" + id + "' was not found.");
	}
	return updated;
    }

    @GET
    @Path("/entrieseries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EntrySerie getEntrySerie(@PathParam("id") long id) throws SQLException {
	EntrySerie entrySerie = calendarManager.getEntrySerie(id);
	if (entrySerie == null) {
	    throw new NotFoundException("EntrySerie id '" + id + "' was not found.");
	}
	return entrySerie;
    }

    @DELETE
    @Path("/entrieseries/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeEntrySeries(@PathParam("id") long id) throws SQLException {
	boolean deleted = calendarManager.removeEntrySerie(id);
	if (!deleted) {
	    throw new NotFoundException("EntrySerie id '" + id + "' was not found.");
	}
	return deleted;
    }

    @GET
    @Path("/entries/year/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Entry> getYearEntries(@PathParam("year") int year, @QueryParam("type") String type)
	    throws SQLException {
	return calendarManager.getYearEntries(year, type);
    }

    @GET
    @Path("/entries/year/{year}/month/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Entry> getMonthEntries(@PathParam("year") int year, @PathParam("month") int month,
	    @QueryParam("type") String type) throws SQLException {
	return calendarManager.getMonthEntries(year, month, type);
    }

    @GET
    @Path("/entries/year/{year}/month/{month}/day/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Entry> getDayEntries(@PathParam("year") int year, @PathParam("month") int month,
	    @PathParam("day") int day, @QueryParam("type") String type) throws SQLException {
	return calendarManager.getDayEntries(year, month, day, type);
    }

    @GET
    @Path("/entries/year/{year}/week/{week}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Entry> getDayEntries(@PathParam("year") int year, @PathParam("week") int week,
	    @QueryParam("type") String type) throws SQLException {
	return calendarManager.getWeekEntries(year, week, type);
    }

}
