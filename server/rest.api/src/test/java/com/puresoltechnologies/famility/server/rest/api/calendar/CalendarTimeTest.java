package com.puresoltechnologies.famility.server.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarTime;

public class CalendarTimeTest {

    @Test
    public void testToString() {
	CalendarTime calendarTime = new CalendarTime(12, 13, 14);
	String string = calendarTime.toString();
	System.out.println(string);
	assertEquals("12:13:14", string);
    }

    @Test
    public void testSerialization() throws IOException {
	CalendarTime calendarTime = new CalendarTime(14, 15, 16);
	String serialized = JsonSerializer.toString(calendarTime);
	System.out.println(serialized);
	CalendarTime deserialized = JsonSerializer.fromString(serialized, CalendarTime.class);
	assertEquals(calendarTime, deserialized);
    }
}
