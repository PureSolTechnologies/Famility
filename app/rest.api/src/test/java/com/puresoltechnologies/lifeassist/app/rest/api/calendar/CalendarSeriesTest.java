package com.puresoltechnologies.lifeassist.app.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.rest.api.contacts.Contact;
import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class CalendarSeriesTest {

    @Test
    public void testSerialization() throws IOException {
	List<Contact> participants = new ArrayList<>();
	participants.add(new Contact(12345, "name", new CalendarDay(2015, 2, 3)));
	CalendarSeries appointment = new CalendarSeries("birthday", "Birthday", "description", participants, true,
		new Reminder(3, ChronoUnit.HOURS), new CalendarDay(2017, 1, 2), new CalendarDay(2017, 10, 2),
		"Europe/Berlin", new CalendarTime(12, 30, 0), 2, ChronoUnit.HOURS, "AWAY", "WEEKLY", 1);
	String serialized = JsonSerializer.toString(appointment);
	System.out.println(serialized);
	CalendarSeries deserialized = JsonSerializer.fromString(serialized, CalendarSeries.class);
	assertEquals(appointment, deserialized);
    }

}
