package com.puresoltechnologies.famility.server.test.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.puresoltechnologies.famility.server.api.calendar.OccupancyStatus;
import com.puresoltechnologies.famility.server.api.calendar.Turnus;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarDay;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarEvent;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarSeries;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarTime;
import com.puresoltechnologies.famility.server.rest.api.calendar.EventType;
import com.puresoltechnologies.famility.server.rest.api.calendar.Reminder;

public class CalendarServiceIT extends AbstractCalendarServiceTest {

    @Test
    public void testGetAppointmentTypes()
	    throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
	JerseyWebTarget client = getRestClient("/entries/types");
	Response response = client.request().get();
	assertEquals(200, response.getStatus());
	MappingIterator<EventType> appointmentTypes = convertCollectionEntity(response, EventType.class);
	assertNotNull(appointmentTypes);
	assertEquals(5, appointmentTypes.readAll().size());
    }

    @Test
    public void testGetTimeUnits() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
	JerseyWebTarget client = getRestClient("/entries/time-units");
	Response response = client.request().get();
	assertEquals(200, response.getStatus());
	MappingIterator<TimeUnit> timeUnits = convertCollectionEntity(response, TimeUnit.class);
	assertNotNull(timeUnits);
	assertEquals(3, timeUnits.readAll().size());
    }

    @Test
    public void testEntryCRUD() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
	JerseyWebTarget client = getRestClient("/entries");
	CalendarEvent original = new CalendarEvent("appointment", "Title", "Description", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0),
		"Europe/Stockholm", new CalendarDay(1978, 5, 16), new CalendarTime(14, 35, 0), "Europe/Stockholm",
		OccupancyStatus.OCCUPIED.name());
	Entity<CalendarEvent> entity = Entity.entity(original, MediaType.APPLICATION_JSON);
	Response response = client.request().put(entity);
	assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
	CalendarEvent createdAppointment = convertEntity(response, CalendarEvent.class);
	assertNotNull(createdAppointment);
	assertTrue(createdAppointment.getId() > 0);

	client = getRestClient("/entries/" + createdAppointment.getId());
	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	CalendarEvent read = convertEntity(response, CalendarEvent.class);
	assertEquals(createdAppointment, read);

	CalendarEvent updated = new CalendarEvent("appointment", "Title2", "Description2", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0),
		"Europe/Stockholm", new CalendarDay(1978, 5, 16), new CalendarTime(14, 35, 0), "Europe/Stockholm",
		OccupancyStatus.OCCUPIED.name());
	entity = Entity.entity(updated, MediaType.APPLICATION_JSON);
	response = client.request().post(entity);
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	CalendarEvent readUpdated = convertEntity(response, CalendarEvent.class);
	assertEquals(updated, readUpdated);

	response = client.request().delete();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());

	response = client.request().delete();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testSeriesCRUD() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
	JerseyWebTarget client = getRestClient("/series");
	CalendarSeries original = new CalendarSeries("appointment", "Title", "Description", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), null, "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED.name(), Turnus.WEEKLY.name(),
		2);
	Entity<CalendarSeries> entity = Entity.entity(original, MediaType.APPLICATION_JSON);
	Response response = client.request().put(entity);
	assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
	CalendarSeries createdAppointmentSerie = convertEntity(response, CalendarSeries.class);
	assertNotNull(createdAppointmentSerie);
	assertTrue(createdAppointmentSerie.getId() > 0);

	client = getRestClient("/series/" + createdAppointmentSerie.getId());
	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	CalendarSeries read = convertEntity(response, CalendarSeries.class);
	assertEquals(createdAppointmentSerie, read);

	CalendarSeries updated = new CalendarSeries("appointment", "Title2", "Description2", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), null, "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED.name(), Turnus.DAILY.name(),
		2);
	entity = Entity.entity(updated, MediaType.APPLICATION_JSON);
	response = client.request().post(entity);
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	CalendarSeries readUpdated = convertEntity(response, CalendarSeries.class);
	assertEquals(updated, readUpdated);

	response = client.request().delete();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());

	response = client.request().delete();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

}
