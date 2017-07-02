package com.puresoltechnologies.lifeassist.app.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.rest.api.contacts.JsonContact;
import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class CalendarEntryTest {

    @Test
    public void testSerialization() throws IOException {
	List<JsonContact> participants = new ArrayList<>();
	participants.add(new JsonContact(12345, "name", new CalendarDay(2015, 2, 3)));
	CalendarEvent appointment = new CalendarEvent("birthday", "Birthday", "description", participants, true,
		new Reminder(3, ChronoUnit.HOURS), new CalendarDay(2017, 1, 2), new CalendarTime(12, 30, 0),
		"Europe/Rome", new CalendarDay(2017, 1, 2), new CalendarTime(13, 00, 0), "Europe/Rome", "AWAY");
	String serialized = JsonSerializer.toString(appointment);
	System.out.println(serialized);
	CalendarEvent deserialized = JsonSerializer.fromString(serialized, CalendarEvent.class);
	assertEquals(appointment, deserialized);
    }

}
