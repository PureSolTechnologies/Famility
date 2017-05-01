package com.puresoltechnologies.lifeassist.app.test.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.api.calendar.EntryType;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.impl.calendar.DurationUnit;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Entry;
import com.puresoltechnologies.lifeassist.app.impl.calendar.EntrySerie;
import com.puresoltechnologies.lifeassist.app.impl.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Reminder;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Turnus;

public class CalendarManagerIT extends AbstractCalendarManagerTest {

    @Test
    public void testGetAppointmentTypes() throws SQLException {
	List<EntryType> appointmentTypes = getCalendarManager().getEntryTypes();
	assertNotNull(appointmentTypes);
	assertEquals(4, appointmentTypes.size());
    }

    @Test
    public void testGetTimeUnits() {
	List<DurationUnit> timeUnits = getCalendarManager().getDurationUnits();
	assertNotNull(timeUnits);
	assertEquals(3, timeUnits.size());
    }

    @Test
    public void testAppointmentCRUD() throws SQLException {
	CalendarManager calendarManager = getCalendarManager();
	Entry original = new Entry("appointment", "Title", "Description", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED);
	long id = calendarManager.insertEntry(original);
	assertEquals(id, original.getId());

	Entry read = calendarManager.getEntry(id);
	assertEquals(original, read);

	Entry updated = new Entry("appointment", "Title2", "Description2", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED);
	assertFalse(calendarManager.updateEntry(updated));
	assertTrue(calendarManager.updateEntry(id, updated));

	Entry readUpdated = calendarManager.getEntry(id);
	assertEquals(updated, readUpdated);

	assertTrue(calendarManager.removeEntry(id));

	assertNull(calendarManager.getEntry(id));

	assertFalse(calendarManager.removeEntry(id));
    }

    @Test
    public void testAppointmentSerieCRUD() throws SQLException {
	CalendarManager calendarManager = getCalendarManager();
	EntrySerie original = new EntrySerie("appointment", "Title", "Description", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED, Turnus.WEEKLY, 2);
	long id = calendarManager.insertEntrySerie(original);
	assertEquals(id, original.getId());

	EntrySerie read = calendarManager.getEntrySerie(id);
	assertEquals(original, read);

	EntrySerie updated = new EntrySerie("appointment", "Title2", "Description2", new ArrayList<>(), true,
		new Reminder(1, ChronoUnit.DAYS), new CalendarDay(1978, 5, 16), "Europe/Stockholm",
		new CalendarTime(13, 35, 0), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED, Turnus.DAILY, 2);
	assertFalse(calendarManager.updateEntrySerie(updated));
	assertTrue(calendarManager.updateEntrySerie(id, updated));

	EntrySerie readUpdated = calendarManager.getEntrySerie(id);
	assertEquals(updated, readUpdated);

	assertTrue(calendarManager.removeEntrySerie(id));

	assertNull(calendarManager.getEntrySerie(id));

	assertFalse(calendarManager.removeEntrySerie(id));
    }

}
