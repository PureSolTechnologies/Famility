package com.puresoltechnologies.famility.server.test.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.puresoltechnologies.famility.server.api.calendar.DurationUnit;
import com.puresoltechnologies.famility.server.api.calendar.Event;
import com.puresoltechnologies.famility.server.api.calendar.EventType;
import com.puresoltechnologies.famility.server.api.calendar.OccupancyStatus;
import com.puresoltechnologies.famility.server.api.calendar.Reminder;
import com.puresoltechnologies.famility.server.api.calendar.Series;
import com.puresoltechnologies.famility.server.api.calendar.Turnus;
import com.puresoltechnologies.famility.server.impl.calendar.CalendarManager;
import com.puresoltechnologies.famility.server.test.calendar.AbstractCalendarManagerTest;

public class CalendarManagerIT extends AbstractCalendarManagerTest {

    @Test
    public void testGetAppointmentTypes() throws SQLException {
	List<EventType> appointmentTypes = getCalendarManager().getEventTypes();
	assertNotNull(appointmentTypes);
	assertEquals(5, appointmentTypes.size());
    }

    @Test
    public void testGetTimeUnits() {
	List<DurationUnit> timeUnits = getCalendarManager().getDurationUnits();
	assertNotNull(timeUnits);
	assertEquals(3, timeUnits.size());
    }

    @Test
    public void testEntryCRUD() throws SQLException {
	ZonedDateTime begin = ZonedDateTime.of(1978, 5, 16, 13, 35, 0, 0, ZoneId.of("Europe/Stockholm"));
	ZonedDateTime end = ZonedDateTime.of(1978, 5, 16, 14, 35, 0, 0, ZoneId.of("Europe/Stockholm"));
	CalendarManager calendarManager = getCalendarManager();
	Event original = new Event("appointment", "Title", "Description", new ArrayList<>(),
		new Reminder(1, ChronoUnit.DAYS), begin, end, OccupancyStatus.OCCUPIED);
	long id = calendarManager.insertEvent(original);
	assertEquals(id, original.getId());

	Event read = calendarManager.getEvent(id);
	assertEquals(original, read);

	Event updated = new Event("appointment", "Title2", "Description2", new ArrayList<>(),
		new Reminder(1, ChronoUnit.DAYS), begin, end, OccupancyStatus.OCCUPIED);
	assertFalse(calendarManager.updateEvent(updated));
	assertTrue(calendarManager.updateEvent(id, updated));

	Event readUpdated = calendarManager.getEvent(id);
	assertEquals(updated, readUpdated);

	assertTrue(calendarManager.removeEvent(id));

	assertNull(calendarManager.getEvent(id));

	assertFalse(calendarManager.removeEvent(id));
    }

    @Test
    public void testSeriesCRUD() throws SQLException {
	ZonedDateTime firstOccurence = ZonedDateTime.of(1978, 5, 16, 13, 35, 0, 0, ZoneId.of("Europe/Stockholm"));

	CalendarManager calendarManager = getCalendarManager();
	Series original = new Series("appointment", "Title", "Description", new ArrayList<>(),
		new Reminder(1, ChronoUnit.DAYS), firstOccurence, null, 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED,
		Turnus.WEEKLY, 2);
	long id = calendarManager.insertSeries(original);
	assertEquals(id, original.getId());

	Series read = calendarManager.getSeries(id);
	assertEquals(original, read);

	Series updated = new Series("appointment", "Title2", "Description2", new ArrayList<>(),
		new Reminder(1, ChronoUnit.DAYS), firstOccurence, null, 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED,
		Turnus.DAILY, 2);
	assertFalse(calendarManager.updateSeries(updated));
	assertTrue(calendarManager.updateSeries(id, updated));

	Series readUpdated = calendarManager.getSeries(id);
	assertEquals(updated, readUpdated);

	assertTrue(calendarManager.removeSeries(id));

	assertNull(calendarManager.getSeries(id));

	assertFalse(calendarManager.removeSeries(id));
    }

}
