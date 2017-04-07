package com.puresoltechnologies.lifeassist.app.test.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Appointment;
import com.puresoltechnologies.lifeassist.app.impl.calendar.AppointmentSerie;
import com.puresoltechnologies.lifeassist.app.impl.calendar.AppointmentType;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.impl.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Turnus;

public class CalendarManagerIT extends AbstractCalendarManagerTest {

    @Test
    public void testGetAppointmentTypes() {
	AppointmentType[] appointmentTypes = getCalendarManager().getAppointmentTypes();
	assertNotNull(appointmentTypes);
	assertEquals(AppointmentType.values().length, appointmentTypes.length);
    }

    @Test
    public void testGetTimeUnits() {
	List<TimeUnit> timeUnits = getCalendarManager().getTimeUnits();
	assertNotNull(timeUnits);
	assertEquals(3, timeUnits.size());
    }

    @Test
    public void testAppointmentCRUD() throws SQLException {
	CalendarManager calendarManager = getCalendarManager();
	Appointment original = new Appointment(AppointmentType.GENERAL, "Title", "Description", new ArrayList<>(), true,
		1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0), new CalendarTime(14, 0, 0),
		OccupancyStatus.OCCUPIED);
	long id = calendarManager.createAppointment(original);
	assertEquals(id, original.getId());

	Appointment read = calendarManager.getAppointment(id);
	assertEquals(original, read);

	Appointment updated = new Appointment(AppointmentType.GENERAL, "Title2", "Description2", new ArrayList<>(),
		true, 1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0),
		new CalendarTime(14, 0, 0), OccupancyStatus.OCCUPIED);
	assertFalse(calendarManager.updateAppointment(updated));
	assertTrue(calendarManager.updateAppointment(id, updated));

	Appointment readUpdated = calendarManager.getAppointment(id);
	assertEquals(updated, readUpdated);

	assertTrue(calendarManager.removeAppointment(id));

	assertNull(calendarManager.getAppointment(id));

	assertFalse(calendarManager.removeAppointment(id));
    }

    @Test
    public void testAppointmentSerieCRUD() throws SQLException {
	CalendarManager calendarManager = getCalendarManager();
	AppointmentSerie original = new AppointmentSerie(AppointmentType.GENERAL, "Title", "Description",
		new ArrayList<>(), true, 1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0),
		new CalendarTime(14, 0, 0), OccupancyStatus.OCCUPIED, Turnus.WEEKLY, 2);
	long id = calendarManager.createAppointmentSerie(original);
	assertEquals(id, original.getId());

	AppointmentSerie read = calendarManager.getAppointmentSerie(id);
	assertEquals(original, read);

	AppointmentSerie updated = new AppointmentSerie(AppointmentType.GENERAL, "Title2", "Description2",
		new ArrayList<>(), true, 1, TimeUnit.DAYS, new CalendarDay(1978, 5, 16), new CalendarTime(13, 35, 0),
		new CalendarTime(14, 0, 0), OccupancyStatus.OCCUPIED, Turnus.DAILY, 2);
	assertFalse(calendarManager.updateAppointmentSerie(updated));
	assertTrue(calendarManager.updateAppointmentSerie(id, updated));

	AppointmentSerie readUpdated = calendarManager.getAppointmentSerie(id);
	assertEquals(updated, readUpdated);

	assertTrue(calendarManager.removeAppointmentSerie(id));

	assertNull(calendarManager.getAppointmentSerie(id));

	assertFalse(calendarManager.removeAppointmentSerie(id));
    }

}
