package com.puresoltechnologies.lifeassist.app.impl.rest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.common.utils.JsonSerializer;

public class RestPersonTest {

    @Test
    public void testSerialization() throws IOException {
	RestPerson person = new RestPerson("Rick-Rainer Ludwig", "1978-05-16");
	String serialized = JsonSerializer.toString(person);
	System.out.println(serialized);
	RestPerson deserialized = JsonSerializer.fromString(serialized, RestPerson.class);
	assertEquals(person, deserialized);
    }

}
