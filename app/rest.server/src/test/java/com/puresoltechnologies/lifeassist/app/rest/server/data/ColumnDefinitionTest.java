package com.puresoltechnologies.lifeassist.app.rest.server.data;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class ColumnDefinitionTest {

    @Test
    public void testSerialization() throws IOException {
	ColumnDefinition original = new ColumnDefinition("Name", ColumnType.BOOLEAN);
	String serialized = JsonSerializer.toString(original);
	System.out.println(serialized);
	ColumnDefinition deserialized = JsonSerializer.fromString(serialized, ColumnDefinition.class);
	assertEquals(original, deserialized);
    }

}
