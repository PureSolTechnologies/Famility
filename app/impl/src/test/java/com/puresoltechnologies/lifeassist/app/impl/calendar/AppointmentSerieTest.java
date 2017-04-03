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

public class AppointmentSerieTest {

    @Test
    public void testSerialization() throws IOException {
	List<Person> arrayList = new ArrayList<>();
	arrayList.add(new Person(12345, "name", new CalendarDay(2015, 2, 3)));
	AppointmentSerie appointment = new AppointmentSerie(AppointmentType.BIRTHDAY, "Birthday", "description",
		new CalendarDay(2017, 1, 2), new CalendarTime(12, 30, 0), new CalendarTime(13, 30, 0),
		new ArrayList<>(), true, 3, TimeUnit.HOURS);
	String serialized = JsonSerializer.toString(appointment);
	System.out.println(serialized);
	AppointmentSerie deserialized = JsonSerializer.fromString(serialized, AppointmentSerie.class);
	assertEquals(appointment, deserialized);
    }

}
