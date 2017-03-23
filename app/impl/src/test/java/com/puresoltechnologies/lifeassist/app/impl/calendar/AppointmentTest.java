package com.puresoltechnologies.lifeassist.app.impl.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.people.Person;
import com.puresoltechnologies.lifeassist.common.utils.JsonSerializer;

public class AppointmentTest {

    @Test
    public void testSerialization() throws IOException {
	List<Person> arrayList = new ArrayList<>();
	arrayList.add(new Person(12345, "name", new CalendarDay(2015, 2, 3)));
	Appointment appointment = new Appointment(AppointmentType.BIRTHDAY, "Birthday", "description", arrayList, true,
		3, TimeUnit.DAYS, true);
	String serialized = JsonSerializer.toString(appointment);
	System.out.println(serialized);
	Appointment deserialized = JsonSerializer.fromString(serialized, Appointment.class);
	assertEquals(appointment, deserialized);
    }

}
