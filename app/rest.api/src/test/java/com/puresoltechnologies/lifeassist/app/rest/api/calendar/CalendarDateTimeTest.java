package com.puresoltechnologies.lifeassist.app.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class CalendarDateTimeTest {

    @Test
    public void testToString() {
	CalendarDateTime calendarDay = new CalendarDateTime(2017, 3, 2, 12, 13, 14);
	String string = calendarDay.toString();
	System.out.println(string);
	assertEquals("2017-03-02T12:13:14", string);
    }

    @Test
    public void testSerialization() throws IOException {
	CalendarDateTime calendarDay = new CalendarDateTime(2017, 3, 2, 12, 13, 14);
	String serialized = JsonSerializer.toString(calendarDay);
	System.out.println(serialized);
	CalendarDateTime deserialized = JsonSerializer.fromString(serialized, CalendarDateTime.class);
	assertEquals(calendarDay, deserialized);
    }

    @Test
    public void test() {
	TemporalAccessor localDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse("1978-05-16T13:35:12");
	System.out.println(localDateTime);
	LocalDateTime from = LocalDateTime.from(localDateTime);
	System.out.println(from);
	ZonedDateTime zoned = ZonedDateTime.of(from, ZoneId.systemDefault());
	System.out.println(Instant.from(zoned));
    }

}
