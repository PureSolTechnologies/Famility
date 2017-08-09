package com.puresoltechnologies.famility.server.rest.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;

public class TypesTest {

    @Test
    public void test() throws IOException {
	Calendar calendar = Calendar.getInstance();
	String serialized = JsonSerializer.toString(calendar);
	System.out.println(serialized);
	Calendar deserialized = JsonSerializer.fromString(serialized, Calendar.class);
	assertEquals(calendar, deserialized);
    }

}
