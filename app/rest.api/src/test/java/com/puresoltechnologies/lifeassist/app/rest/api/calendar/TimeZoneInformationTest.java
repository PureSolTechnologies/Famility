package com.puresoltechnologies.lifeassist.app.rest.api.calendar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.common.rest.JsonSerializer;

public class TimeZoneInformationTest {

    @Test
    public void testSerialization() throws IOException {
	String zoneId = "Europe/Berlin";
	ZoneId zone = ZoneId.of(zoneId);
	TimeZoneInformation calendarTime = new TimeZoneInformation(zoneId,
		zone.getDisplayName(TextStyle.FULL, Locale.US),
		zone.getRules().getOffset(Instant.now()).getTotalSeconds() / 3600);
	String serialized = JsonSerializer.toString(calendarTime);
	System.out.println(serialized);
	TimeZoneInformation deserialized = JsonSerializer.fromString(serialized, TimeZoneInformation.class);
	assertEquals(calendarTime, deserialized);
    }
}
