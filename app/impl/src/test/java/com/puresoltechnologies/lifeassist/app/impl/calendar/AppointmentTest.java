package com.puresoltechnologies.lifeassist.app.impl.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	Appointment appointment = new Appointment(AppointmentType.BIRTHDAY, "Birthday", "description",
		new ArrayList<>(), true, 3, TimeUnit.HOURS, new CalendarDay(2017, 1, 2), "Europe/Rom",
		new CalendarTime(12, 30, 0), new CalendarTime(13, 30, 0), OccupancyStatus.AWAY);
	String serialized = JsonSerializer.toString(appointment);
	System.out.println(serialized);
	Appointment deserialized = JsonSerializer.fromString(serialized, Appointment.class);
	assertEquals(appointment, deserialized);
    }

}
