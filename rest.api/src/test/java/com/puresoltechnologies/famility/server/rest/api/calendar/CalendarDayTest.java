package com.puresoltechnologies.famility.server.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarDay;

public class CalendarDayTest {

    @Test
    public void testToString() {
	CalendarDay calendarDay = new CalendarDay(2017, 3, 2);
	String string = calendarDay.toString();
	System.out.println(string);
	assertEquals("2017-03-02", string);
    }

    @Test
    public void testSerialization() throws IOException {
	CalendarDay calendarDay = new CalendarDay(2017, 3, 2);
	String serialized = JsonSerializer.toString(calendarDay);
	System.out.println(serialized);
	CalendarDay deserialized = JsonSerializer.fromString(serialized, CalendarDay.class);
	assertEquals(calendarDay, deserialized);
    }

}
