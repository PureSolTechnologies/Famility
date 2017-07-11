package com.puresoltechnologies.lifeassist.app.rest.server.data;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class TableRowTest {

    @Test
    public void testSerialization() throws IOException {
	List<Object> values = new ArrayList<>();
	values.add(1);
	values.add(1.23);
	values.add("String");
	values.add(false);
	TableRow original = new TableRow(values);
	String serialized = JsonSerializer.toString(original);
	System.out.println(serialized);
	TableRow deserialized = JsonSerializer.fromString(serialized, TableRow.class);
	assertEquals(original, deserialized);
    }

}
