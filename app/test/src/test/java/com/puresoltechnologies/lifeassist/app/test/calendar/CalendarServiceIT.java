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

import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Appointment;
import com.puresoltechnologies.lifeassist.app.impl.calendar.AppointmentType;
import com.puresoltechnologies.lifeassist.app.impl.calendar.OccupancyStatus;

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
	Appointment createdAppointment = convertEntity(response, Appointment.class);
	assertNotNull(createdAppointment);
	assertTrue(createdAppointment.getId() > 0);

	// Appointment read = calendarManager.getAppointment(id);
	// assertEquals(original, read);
	//
	// Appointment updated = new Appointment(AppointmentType.GENERAL,
	// "Title2", "Description2", new ArrayList<>(),
	// true, 1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16), new
	// CalendarTime(13, 35, 0),
	// new CalendarTime(14, 0, 0), OccupancyStatus.OCCUPIED);
	// assertFalse(calendarManager.updateAppointment(updated));
	// assertTrue(calendarManager.updateAppointment(id, updated));
	//
	// Appointment readUpdated = calendarManager.getAppointment(id);
	// assertEquals(updated, readUpdated);
	//
	// assertTrue(calendarManager.removeAppointment(id));
	//
	// assertNull(calendarManager.getAppointment(id));
	//
	// assertFalse(calendarManager.removeAppointment(id));
    }
    //
    // @Test
    // public void testAppointmentSerieCRUD() throws SQLException {
    // CalendarManager calendarManager = getCalendarManager();
    // AppointmentSerie original = new AppointmentSerie(AppointmentType.GENERAL,
    // "Title", "Description",
    // new ArrayList<>(), true, 1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16),
    // new CalendarTime(13, 35, 0),
    // new CalendarTime(14, 0, 0), OccupancyStatus.OCCUPIED, Turnus.WEEKLY, 2);
    // long id = calendarManager.createAppointmentSerie(original);
    // assertEquals(id, original.getId());
    //
    // AppointmentSerie read = calendarManager.getAppointmentSerie(id);
    // assertEquals(original, read);
    //
    // AppointmentSerie updated = new AppointmentSerie(AppointmentType.GENERAL,
    // "Title2", "Description2",
    // new ArrayList<>(), true, 1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16),
    // new CalendarTime(13, 35, 0),
    // new CalendarTime(14, 0, 0), OccupancyStatus.OCCUPIED, Turnus.DAILY, 2);
    // assertFalse(calendarManager.updateAppointmentSerie(updated));
    // assertTrue(calendarManager.updateAppointmentSerie(id, updated));
    //
    // AppointmentSerie readUpdated = calendarManager.getAppointmentSerie(id);
    // assertEquals(updated, readUpdated);
    //
    // assertTrue(calendarManager.removeAppointmentSerie(id));
    //
    // assertNull(calendarManager.getAppointmentSerie(id));
    //
    // assertFalse(calendarManager.removeAppointmentSerie(id));
    // }

}
