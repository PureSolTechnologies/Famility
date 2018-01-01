package com.puresoltechnologies.famility.server.rest.api.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarDay;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarFactory;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarYear;

public class CalendarFactoryTest {

    @Test
    public void testSerialization() throws IOException {
	CalendarYear calendarYear = CalendarFactory.createYear(2017);
	assertNotNull(calendarYear);
	String string = JsonSerializer.toString(calendarYear);
	System.out.println(string);
	System.out.println(string.length());
	CalendarYear deserialized = JsonSerializer.fromString(string, CalendarYear.class);
	assertEquals(calendarYear, deserialized);
    }

    @Test
    public void testFindWeek() {
	CalendarDay weekStart = CalendarFactory.findWeek(2017, 1);
	assertNotNull(weekStart);
	assertEquals(2017, weekStart.getYear());
	assertEquals(1, weekStart.getMonth());
	assertEquals(2, weekStart.getDayOfMonth());
    }

}
