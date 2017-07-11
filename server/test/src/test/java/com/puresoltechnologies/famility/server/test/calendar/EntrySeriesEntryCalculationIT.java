package com.puresoltechnologies.famility.server.test.calendar;

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

import com.puresoltechnologies.famility.server.api.calendar.Event;
import com.puresoltechnologies.famility.server.api.calendar.OccupancyStatus;
import com.puresoltechnologies.famility.server.api.calendar.Reminder;
import com.puresoltechnologies.famility.server.api.calendar.Series;
import com.puresoltechnologies.famility.server.api.calendar.Turnus;
import com.puresoltechnologies.famility.server.impl.calendar.CalendarManager;
import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;
import com.puresoltechnologies.famility.server.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.famility.server.model.calendar.QEvents;
import com.puresoltechnologies.famility.server.model.calendar.QSeries;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarDay;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarTime;
import com.puresoltechnologies.famility.server.test.calendar.AbstractCalendarManagerTest;
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
	    SQLQuery<Tuple> select = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertTrue(select.fetchResults().isEmpty());
	    // one entry series was created, yet, and therefore no entries are
	    // to be expected
	    select = queryFactory.select(QEvents.events.all()).from(QEvents.events);
	    assertTrue(select.fetchResults().isEmpty());
	}
	// create new entry series
	CalendarManager calendarManager = getCalendarManager();
	ZonedDateTime firstOccurence = ZonedDateTime.of(CalendarDay.toLocalDate(startDate),
		CalendarTime.toLocalTime(time), ZoneId.of("Europe/Berlin"));
	Series entrySerie = new Series("appointment", "Title: " + turnus + " skipping " + skipping,
		"Test entry series.", new ArrayList<>(), new Reminder(1, ChronoUnit.HOURS), firstOccurence,
		CalendarDay.toLocalDate(endDate), 1, ChronoUnit.HOURS, OccupancyStatus.OCCUPIED, turnus, skipping);
	calendarManager.insertSeries(entrySerie);
	// checks after series creation
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    // one entry series was created
	    SQLQuery<Tuple> select = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertEquals(1, select.fetchResults().getTotal());
	    // The first occurrence is created
	    select = queryFactory.select(QEvents.events.all()).from(QEvents.events);
	    assertEquals(1, select.fetchResults().getTotal());
	}
    }

    @Test
    public void testBoundaries() throws SQLException {
	CalendarManager calendarManager = getCalendarManager();
	Collection<Event> yearEntries = calendarManager.getYearEntries(2017, "appointment");
	assertFalse(yearEntries.isEmpty());
	List<Event> entries = new ArrayList<>(yearEntries);
	Collections.sort(entries);
	for (Event entry : entries) {
	    ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDate.getLocalDate(), time.getLocalTime(),
		    entry.getBegin().getZone());
	    ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDate.getLocalDate(), time.getLocalTime(),
		    entry.getBegin().getZone());
	    assertTrue(entry.getBegin() + " not after or at " + startZonedDateTime,
		    entry.getBegin().compareTo(startZonedDateTime) >= 0);
	    assertTrue(entry.getBegin() + " not before or at " + endZonedDateTime,
		    entry.getBegin().compareTo(endZonedDateTime) <= 0);
	}

    }
}
