package com.puresoltechnologies.famility.server.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;
import com.puresoltechnologies.famility.server.rest.api.calendar.EventType;

public class EntryTypeTest {

    @Test
    public void testSerialization() throws IOException {
	EventType entryType = new EventType("type", "name");
	String serialized = JsonSerializer.toString(entryType);
	System.out.println(serialized);
	EventType deserialized = JsonSerializer.fromString(serialized, EventType.class);
	assertEquals(entryType, deserialized);
    }

}
