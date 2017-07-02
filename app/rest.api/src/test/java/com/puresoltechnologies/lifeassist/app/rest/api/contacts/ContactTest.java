package com.puresoltechnologies.lifeassist.app.rest.api.contacts;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.rest.api.contacts.JsonContact;
import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class ContactTest {

    @Test
    public void testSerialization() throws IOException {
	JsonContact person = new JsonContact(42, "Rick-Rainer Ludwig", new CalendarDay(1978, 5, 16));
	String serialized = JsonSerializer.toString(person);
	System.out.println(serialized);
	JsonContact deserialized = JsonSerializer.fromString(serialized, JsonContact.class);
	assertEquals(person, deserialized);
    }

}
