package com.puresoltechnologies.famility.server.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarYear;

public class CalendarYearTest {

    @Test
    public void testToString() {
	CalendarYear calendarYear = new CalendarYear(2017, new HashMap<>());
	String string = calendarYear.toString();
	System.out.println(string);
	assertEquals("2017", string);
    }

    @Test
    public void testSerialization() throws IOException {
	CalendarYear calendarYear = new CalendarYear(2017, new HashMap<>());
	String serialized = JsonSerializer.toString(calendarYear);
	System.out.println(serialized);
	CalendarYear deserialized = JsonSerializer.fromString(serialized, CalendarYear.class);
	assertEquals(calendarYear, deserialized);
    }

}
