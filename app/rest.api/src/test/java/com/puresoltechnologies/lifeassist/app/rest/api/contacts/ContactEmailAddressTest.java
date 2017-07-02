package com.puresoltechnologies.lifeassist.app.rest.api.contacts;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class ContactEmailAddressTest {

    @Test
    public void testSerialization() throws IOException {
	JsonContactEmailAddress contactEmailAddress = new JsonContactEmailAddress(new EmailAddress("a@a.de"), 1);
	String serialized = JsonSerializer.toString(contactEmailAddress);
	System.out.println(serialized);
	JsonContactEmailAddress deserialized = JsonSerializer.fromString(serialized, JsonContactEmailAddress.class);
	assertEquals(contactEmailAddress, deserialized);
    }

}
