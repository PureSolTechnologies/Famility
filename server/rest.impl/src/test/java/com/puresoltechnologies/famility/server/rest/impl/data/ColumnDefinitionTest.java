package com.puresoltechnologies.famility.server.rest.impl.data;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;
import com.puresoltechnologies.famility.server.rest.impl.data.ColumnDefinition;
import com.puresoltechnologies.famility.server.rest.impl.data.ColumnType;

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
