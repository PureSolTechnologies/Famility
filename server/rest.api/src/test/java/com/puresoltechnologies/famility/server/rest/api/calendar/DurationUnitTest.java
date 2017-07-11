package com.puresoltechnologies.famility.server.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import com.puresoltechnologies.famility.common.rest.JsonSerializer;
import com.puresoltechnologies.famility.server.rest.api.calendar.DurationUnit;

public class DurationUnitTest {

    @Test
    public void testSerialization() throws IOException {
	DurationUnit durationUnit = new DurationUnit(ChronoUnit.HOURS, "Hours");
	String serialized = JsonSerializer.toString(durationUnit);
	System.out.println(serialized);
	DurationUnit deserialized = JsonSerializer.fromString(serialized, DurationUnit.class);
	assertEquals(durationUnit, deserialized);
    }

}
