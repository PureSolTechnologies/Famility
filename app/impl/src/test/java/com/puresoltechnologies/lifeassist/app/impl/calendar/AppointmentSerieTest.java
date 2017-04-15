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

public class AppointmentSerieTest {

    @Test
    public void testSerialization() throws IOException {
	List<Person> arrayList = new ArrayList<>();
	arrayList.add(new Person(12345, "name", new CalendarDay(2015, 2, 3)));
	AppointmentSerie appointment = new AppointmentSerie(AppointmentType.BIRTHDAY, "Birthday", "description",
		new ArrayList<>(), true, new Reminder(3, ChronoUnit.HOURS), new CalendarDay(2017, 1, 2),
		"Europe/Berlin", new CalendarTime(12, 30, 0), 2, ChronoUnit.HOURS, OccupancyStatus.AWAY, Turnus.WEEKLY,
		1);
	String serialized = JsonSerializer.toString(appointment);
	System.out.println(serialized);
	AppointmentSerie deserialized = JsonSerializer.fromString(serialized, AppointmentSerie.class);
	assertEquals(appointment, deserialized);
    }

}
