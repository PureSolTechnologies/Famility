package com.puresoltechnologies.lifeassist.app.test.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
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
import com.puresoltechnologies.lifeassist.app.impl.calendar.Appointment;
import com.puresoltechnologies.lifeassist.app.impl.calendar.AppointmentSerie;
import com.puresoltechnologies.lifeassist.app.impl.calendar.AppointmentType;
import com.puresoltechnologies.lifeassist.app.impl.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Turnus;

public class CalendarServiceIT extends AbstractCalendarServiceTest {

    @Test
    public void testGetAppointmentTypes()
	    throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
	JerseyWebTarget client = getRestClient("/appointments/types");
	Response response = client.request().get();
	assertEquals(200, response.getStatus());
	MappingIterator<AppointmentType> appointmentTypes = convertCollectionEntity(response, AppointmentType.class);
	assertNotNull(appointmentTypes);
	assertEquals(AppointmentType.values().length, appointmentTypes.readAll().size());
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
	Appointment original = new Appointment(AppointmentType.GENERAL, "Title", "Description", new ArrayList<>(), true,
		1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0), new CalendarTime(14, 0, 0),
		OccupancyStatus.OCCUPIED);
	Entity<Appointment> entity = Entity.entity(original, MediaType.APPLICATION_JSON);
	Response response = client.request().put(entity);
	assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
	Appointment createdAppointment = convertEntity(response, Appointment.class);
	assertNotNull(createdAppointment);
	assertTrue(createdAppointment.getId() > 0);

	client = getRestClient("/appointments/" + createdAppointment.getId());
	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	Appointment read = convertEntity(response, Appointment.class);
	assertEquals(createdAppointment, read);

	Appointment updated = new Appointment(AppointmentType.GENERAL, "Title2", "Description2", new ArrayList<>(),
		true, 1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0),
		new CalendarTime(14, 0, 0), OccupancyStatus.OCCUPIED);
	entity = Entity.entity(updated, MediaType.APPLICATION_JSON);
	response = client.request().post(entity);
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	Appointment readUpdated = convertEntity(response, Appointment.class);
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
	AppointmentSerie original = new AppointmentSerie(AppointmentType.GENERAL, "Title", "Description",
		new ArrayList<>(), true, 1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0),
		new CalendarTime(14, 0, 0), OccupancyStatus.OCCUPIED, Turnus.WEEKLY, 2);
	Entity<AppointmentSerie> entity = Entity.entity(original, MediaType.APPLICATION_JSON);
	Response response = client.request().put(entity);
	assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
	AppointmentSerie createdAppointmentSerie = convertEntity(response, AppointmentSerie.class);
	assertNotNull(createdAppointmentSerie);
	assertTrue(createdAppointmentSerie.getId() > 0);

	client = getRestClient("/appointmentSeries/" + createdAppointmentSerie.getId());
	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	AppointmentSerie read = convertEntity(response, AppointmentSerie.class);
	assertEquals(createdAppointmentSerie, read);

	AppointmentSerie updated = new AppointmentSerie(AppointmentType.GENERAL, "Title2", "Description2",
		new ArrayList<>(), true, 1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0),
		new CalendarTime(14, 0, 0), OccupancyStatus.OCCUPIED, Turnus.DAILY, 2);
	entity = Entity.entity(updated, MediaType.APPLICATION_JSON);
	response = client.request().post(entity);
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());
	AppointmentSerie readUpdated = convertEntity(response, AppointmentSerie.class);
	assertEquals(updated, readUpdated);

	response = client.request().delete();
	assertEquals(Status.OK.getStatusCode(), response.getStatus());

	response = client.request().get();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());

	response = client.request().delete();
	assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

}
