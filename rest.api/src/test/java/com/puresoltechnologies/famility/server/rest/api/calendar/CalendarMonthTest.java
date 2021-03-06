package com.puresoltechnologies.famility.server.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;

public class CalendarMonthTest {

    @Test
    public void testToString() {
	CalendarMonth calendarMonth = new CalendarMonth(2017, 3, new HashMap<Integer, CalendarDay>());
	String string = calendarMonth.toString();
	System.out.println(string);
	assertEquals("2017-03", string);
    }

    @Test
    public void testSerialization() throws IOException {
	CalendarMonth calendarMonth = new CalendarMonth(2017, 3, new HashMap<Integer, CalendarDay>());
	String serialized = JsonSerializer.toString(calendarMonth);
	System.out.println(serialized);
	CalendarMonth deserialized = JsonSerializer.fromString(serialized, CalendarMonth.class);
	assertEquals(calendarMonth, deserialized);
    }

}
