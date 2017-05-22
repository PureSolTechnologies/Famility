package com.puresoltechnologies.lifeassist.app.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class ReminderTest {

    @Test
    public void testSerialization() throws IOException {
	Reminder reminder = new Reminder(3, ChronoUnit.HOURS);
	String serialized = JsonSerializer.toString(reminder);
	System.out.println(serialized);
	Reminder deserialized = JsonSerializer.fromString(serialized, Reminder.class);
	assertEquals(reminder, deserialized);
    }

}
