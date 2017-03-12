package com.puresoltechnologies.lifeassist.app.api.people;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.common.utils.JsonSerializer;

public class BirthdayTest {

    @Test
    public void testSerialization() throws IOException {
	Birthday person = new Birthday(42, "Rick-Rainer Ludwig", new CalendarDay(1978, 5, 16));
	String serialized = JsonSerializer.toString(person);
	System.out.println(serialized);
	Birthday deserialized = JsonSerializer.fromString(serialized, Birthday.class);
	assertEquals(person, deserialized);
    }

}
