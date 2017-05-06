package com.puresoltechnologies.lifeassist.app.test.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Entry;
import com.puresoltechnologies.lifeassist.app.impl.calendar.EntrySerie;
import com.puresoltechnologies.lifeassist.app.impl.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Reminder;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Turnus;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.model.QEntries;
import com.puresoltechnologies.lifeassist.app.model.QEntrySeries;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;

@RunWith(Parameterized.class)
public class EntrySeriesEntryCalculationIT extends AbstractCalendarManagerTest {

    private static final CalendarDay startDate = new CalendarDay(2017, 3, 1);
    private static final CalendarDay endDate = new CalendarDay(2017, 9, 1);
    private static final CalendarTime time = new CalendarTime(8, 0, 0);

    @Parameter(0)
    public Turnus turnus;

    @Parameter(1)
    public int skipping;

    @Parameters(name = "{index}: {0} turnus, skipping {1}")
    public static Collection<Object[]> createParameters() {
	return Arrays.asList(new Object[][] { //
		{ Turnus.DAILY, (int) 0 }, //
		{ Turnus.DAILY, (int) 1 }, //
		{ Turnus.DAILY, (int) 2 }, //
		{ Turnus.WEEKLY, (int) 0 }, //
		{ Turnus.WEEKLY, (int) 1 }, //
		{ Turnus.WEEKLY, (int) 2 }, //
		{ Turnus.MONTHLY, (int) 0 }, //
		{ Turnus.MONTHLY, (int) 1 }, //
		{ Turnus.MONTHLY, (int) 2 }, //
		{ Turnus.QUARTERLY, (int) 0 }, //
		{ Turnus.QUARTERLY, (int) 1 }, //
		{ Turnus.QUARTERLY, (int) 2 }, //
		{ Turnus.YEARLY, (int) 0 }, //
		{ Turnus.YEARLY, (int) 1 }, //
		{ Turnus.YEARLY, (int) 2 }, //
	});
    }

    @Before
    public void calculateEntries() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    // no entry series was created, yet
	    SQLQuery<Tuple> select = queryFactory.select(QEntrySeries.entrySeries.all()).from(QEntrySeries.entrySeries);
	    assertTrue(select.fetchResults().isEmpty());
	    // one entry series was created, yet, and therefore no entries are
	    // to be expected
	    select = queryFactory.select(QEntries.entries.all()).from(QEntries.entries);
	    assertTrue(select.fetchResults().isEmpty());
	}
	// create new entry series
	CalendarManager calendarManager = getCalendarManager();
	EntrySerie entrySerie = new EntrySerie("appointment", "Title: " + turnus + " skipping " + skipping,
		"Test entry series.", new ArrayList<>(), true, new Reminder(1, ChronoUnit.HOURS), startDate, endDate,
		"Europe/Berlin", time, 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED, turnus, skipping);
	calendarManager.insertEntrySerie(entrySerie);
	// checks after series creation
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    // one entry series was created
	    SQLQuery<Tuple> select = queryFactory.select(QEntrySeries.entrySeries.all()).from(QEntrySeries.entrySeries);
	    assertEquals(1, select.fetchResults().getTotal());
	    // The first occurrence is created
	    select = queryFactory.select(QEntries.entries.all()).from(QEntries.entries);
	    assertEquals(1, select.fetchResults().getTotal());
	}
    }

    @Test
    public void testBoundaries() throws SQLException {
	CalendarManager calendarManager = getCalendarManager();
	Collection<Entry> yearEntries = calendarManager.getYearEntries(2017, "appointment");
	assertFalse(yearEntries.isEmpty());
	List<Entry> entries = new ArrayList<>(yearEntries);
	Collections.sort(entries);
	for (Entry entry : entries) {
	    ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDate.getLocalDate(), time.getLocalTime(),
		    ZoneId.of(entry.getTimezone()));
	    ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDate.getLocalDate(), time.getLocalTime(),
		    ZoneId.of(entry.getTimezone()));
	    assertTrue(entry.getZonedDateTime() + " not after or at " + startZonedDateTime,
		    entry.getZonedDateTime().compareTo(startZonedDateTime) >= 0);
	    assertTrue(entry.getZonedDateTime() + " not before or at " + endZonedDateTime,
		    entry.getZonedDateTime().compareTo(endZonedDateTime) <= 0);
	}

    }
}
