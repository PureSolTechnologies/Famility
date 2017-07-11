package com.puresoltechnologies.famility.server.rest.api.contacts;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarDay;
import com.puresoltechnologies.famility.server.rest.api.contacts.JsonBirthday;

public class BirthdayTest {

    @Test
    public void testSerialization() throws IOException {
	JsonBirthday person = new JsonBirthday(42, "Rick-Rainer Ludwig", new CalendarDay(1978, 5, 16));
	String serialized = JsonSerializer.toString(person);
	System.out.println(serialized);
	JsonBirthday deserialized = JsonSerializer.fromString(serialized, JsonBirthday.class);
	assertEquals(person, deserialized);
    }

}
