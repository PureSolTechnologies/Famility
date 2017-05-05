package com.puresoltechnologies.lifeassist.app.test.calendar;

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
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Entry;
import com.puresoltechnologies.lifeassist.app.impl.calendar.EntrySerie;
import com.puresoltechnologies.lifeassist.app.impl.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Reminder;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Turnus;

public class CalendarServiceIT extends AbstractCalendarServiceTest {

    @Test
    public void testGetAppointmentTypes()
	    throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
	JerseyWebTarget client = getRestClient("/appointments/types");
	Response response = client.request().get();
	assertEquals(200, response.getStatus());
	MappingIterator<String> appointmentTypes = convertCollectionEntity(response, String.class);
	assertNotNull(appointmentTypes);
	assertEquals(4, appointmentTypes.readAll().size());
    }

    @Test
    public void testGetTimeUnits() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
	JerseyWebTarget client = getRestClient("/appointments/time-units");
	Response response = client.request().get();
	assertEquals(200, response.getStatus());
	MappingIterator<TimeUnit> timeUnits = convertCollectionEntity(response, TimeUnit.class);
	assertNotNull(timeUnits);
	assertEquals(3, timeUnits.readAll().size());
    }

    @Test
    public void testAppointmentCRUD() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
	JerseyWebTarget client = getRestClient("/appointments");
	Entry original = new Entry("appointment", "Title", "Description", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED);
	Entity<Entry> entity = Entity.entity(original, MediaType.APPLICATION_JSON);
	Response response = client.request().put(entity);
	assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
	Entry createdAppointment = convertEntity(response, Entry.class);
	assertNotNull(createdAppointment);
	assertTrue(createdAppointment.getId() > 0);

	client = getRestClient("/appointments/" + createdAppointment.getId());
	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	Entry read = convertEntity(response, Entry.class);
	assertEquals(createdAppointment, read);

	Entry updated = new Entry("appointment", "Title2", "Description2", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED);
	entity = Entity.entity(updated, MediaType.APPLICATION_JSON);
	response = client.request().post(entity);
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	Entry readUpdated = convertEntity(response, Entry.class);
	assertEquals(updated, readUpdated);

	response = client.request().delete();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());

	response = client.request().delete();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testAppointmentSerieCRUD()
	    throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
	JerseyWebTarget client = getRestClient("/appointmentSeries");
	EntrySerie original = new EntrySerie("appointment", "Title", "Description", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), null, "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED, Turnus.WEEKLY, 2);
	Entity<EntrySerie> entity = Entity.entity(original, MediaType.APPLICATION_JSON);
	Response response = client.request().put(entity);
	assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
	EntrySerie createdAppointmentSerie = convertEntity(response, EntrySerie.class);
	assertNotNull(createdAppointmentSerie);
	assertTrue(createdAppointmentSerie.getId() > 0);

	client = getRestClient("/appointmentSeries/" + createdAppointmentSerie.getId());
	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	EntrySerie read = convertEntity(response, EntrySerie.class);
	assertEquals(createdAppointmentSerie, read);

	EntrySerie updated = new EntrySerie("appointment", "Title2", "Description2", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), null, "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED, Turnus.DAILY, 2);
	entity = Entity.entity(updated, MediaType.APPLICATION_JSON);
	response = client.request().post(entity);
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	EntrySerie readUpdated = convertEntity(response, EntrySerie.class);
	assertEquals(updated, readUpdated);

	response = client.request().delete();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());

	response = client.request().delete();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

}
