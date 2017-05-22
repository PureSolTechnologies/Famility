package com.puresoltechnologies.lifeassist.app.test.calendar;

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

import com.puresoltechnologies.lifeassist.app.api.calendar.DurationUnit;
import com.puresoltechnologies.lifeassist.app.api.calendar.Entry;
import com.puresoltechnologies.lifeassist.app.api.calendar.EntryType;
import com.puresoltechnologies.lifeassist.app.api.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.api.calendar.Reminder;
import com.puresoltechnologies.lifeassist.app.api.calendar.Series;
import com.puresoltechnologies.lifeassist.app.api.calendar.Turnus;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;

public class CalendarManagerIT extends AbstractCalendarManagerTest {

    @Test
    public void testGetAppointmentTypes() throws SQLException {
	List<EntryType> appointmentTypes = getCalendarManager().getEntryTypes();
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
	Entry original = new Entry("appointment", "Title", "Description", new ArrayList<>(),
		new Reminder(1, ChronoUnit.DAYS), begin, end, OccupancyStatus.OCCUPIED);
	long id = calendarManager.insertEntry(original);
	assertEquals(id, original.getId());

	Entry read = calendarManager.getEntry(id);
	assertEquals(original, read);

	Entry updated = new Entry("appointment", "Title2", "Description2", new ArrayList<>(),
		new Reminder(1, ChronoUnit.DAYS), begin, end, OccupancyStatus.OCCUPIED);
	assertFalse(calendarManager.updateEntry(updated));
	assertTrue(calendarManager.updateEntry(id, updated));

	Entry readUpdated = calendarManager.getEntry(id);
	assertEquals(updated, readUpdated);

	assertTrue(calendarManager.removeEntry(id));

	assertNull(calendarManager.getEntry(id));

	assertFalse(calendarManager.removeEntry(id));
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
