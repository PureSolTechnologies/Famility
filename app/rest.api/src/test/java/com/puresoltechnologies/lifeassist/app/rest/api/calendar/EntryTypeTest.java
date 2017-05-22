package com.puresoltechnologies.lifeassist.app.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class EntryTypeTest {

    @Test
    public void testSerialization() throws IOException {
	EntryType entryType = new EntryType("type", "name");
	String serialized = JsonSerializer.toString(entryType);
	System.out.println(serialized);
	EntryType deserialized = JsonSerializer.fromString(serialized, EntryType.class);
	assertEquals(entryType, deserialized);
    }

}
