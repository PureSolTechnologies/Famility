package com.puresoltechnologies.lifeassist.app.impl.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.api.people.Person;
import com.puresoltechnologies.lifeassist.common.utils.JsonSerializer;

public class AppointmentTest {

    @Test
    public void testSerialization() throws IOException {
	List<Person> arrayList = new ArrayList<>();
	arrayList.add(new Person(12345, "name", new CalendarDay(2015, 2, 3)));
	Entry appointment = new Entry("birthday", "Birthday", "description", new ArrayList<>(), true,
		new Reminder(3, ChronoUnit.HOURS), new CalendarDay(2017, 1, 2), "Europe/Rome",
		new CalendarTime(12, 30, 0), 30, ChronoUnit.MINUTES, OccupancyStatus.AWAY);
	String serialized = JsonSerializer.toString(appointment);
	System.out.println(serialized);
	Entry deserialized = JsonSerializer.fromString(serialized, Entry.class);
	assertEquals(appointment, deserialized);
    }

}
