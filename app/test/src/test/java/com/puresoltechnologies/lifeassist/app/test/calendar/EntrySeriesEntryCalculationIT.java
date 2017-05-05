package com.puresoltechnologies.lifeassist.app.test.calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

    @Parameter(0)
    public Turnus turnus;

    @Parameter(1)
    public int skipping;

    @Parameters(name = "{index}: {0} turnus, skipping {1}")
    public static Collection<Object[]> createParameters() {
	return Arrays.asList(new Object[][] { { Turnus.DAILY, (int) 0 } });
    }

    @Before
    public void calculateEntries() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QEntrySeries.entrySeries.all()).from(QEntrySeries.entrySeries);
	    assertTrue(select.fetchResults().isEmpty());
	    select = queryFactory.select(QEntries.entries.all()).from(QEntries.entries);
	    assertTrue(select.fetchResults().isEmpty());
	}

	CalendarManager calendarManager = getCalendarManager();
	EntrySerie entrySerie = new EntrySerie("appointment", "Title: " + turnus + " skipping " + skipping,
		"Test entry series.", new ArrayList<>(), true, new Reminder(1, ChronoUnit.HOURS),
		new CalendarDay(2017, 3, 1), new CalendarDay(2017, 9, 1), "Europe/Berlin", new CalendarTime(8, 0, 0), 1,
		ChronoUnit.HOURS, OccupancyStatus.OCCUPIED, turnus, skipping);
	calendarManager.insertEntrySerie(entrySerie);

	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QEntrySeries.entrySeries.all()).from(QEntrySeries.entrySeries);
	    assertFalse(select.fetchResults().isEmpty());
	    select = queryFactory.select(QEntries.entries.all()).from(QEntries.entries);
	    assertTrue(select.fetchResults().isEmpty());
	}
    }

    @Test
    public void testBoundaries() throws SQLException {
	CalendarManager calendarManager = getCalendarManager();
	Collection<Entry> yearEntries = calendarManager.getYearEntries(2017, "appointment");
	assertFalse(yearEntries.isEmpty());
    }
}
