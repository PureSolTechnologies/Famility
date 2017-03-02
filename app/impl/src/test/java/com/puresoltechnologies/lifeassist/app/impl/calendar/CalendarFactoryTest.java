package com.puresoltechnologies.lifeassist.app.impl.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarYear;
import com.puresoltechnologies.lifeassist.common.utils.JsonSerializer;

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

}
