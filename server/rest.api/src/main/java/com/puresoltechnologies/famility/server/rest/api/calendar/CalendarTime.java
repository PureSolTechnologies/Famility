package com.puresoltechnologies.famility.server.rest.api.calendar;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a single day time.
 * 
 * @author Rick-Rainer Ludwig
 */
public class CalendarTime {

    public static CalendarTime of(LocalTime localTime) {
	return new CalendarTime(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
    }

    public static CalendarTime of(TemporalAccessor date) {
	int hour = date.get(ChronoField.HOUR_OF_DAY);
	int minute = date.get(ChronoField.MINUTE_OF_HOUR);
	int second = date.get(ChronoField.SECOND_OF_MINUTE);
	return new CalendarTime(hour, minute, second);
    }

    public static LocalTime toLocalTime(CalendarTime calendarTime) {
	return LocalTime.of(calendarTime.getHour(), calendarTime.getMinute(), calendarTime.getSecond());
    }

    private final int hour;
    private final int minute;
    private final int second;
    private final LocalTime localTime;

    @JsonCreator
    public CalendarTime(@JsonProperty("hour") int hour, //
	    @JsonProperty("minute") int minute, //
	    @JsonProperty("second") int second) {
	super();
	this.hour = hour;
	this.minute = minute;
	this.second = second;
	localTime = LocalTime.of(getHour(), getMinute(), getSecond());
    }

    public int getHour() {
	return hour;
    }

    public int getMinute() {
	return minute;
    }

    public int getSecond() {
	return second;
    }

    @JsonIgnore
    public LocalTime getLocalTime() {
	return localTime;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + hour;
	result = prime * result + minute;
	result = prime * result + second;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	CalendarTime other = (CalendarTime) obj;
	if (hour != other.hour)
	    return false;
	if (minute != other.minute)
	    return false;
	if (second != other.second)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return DateTimeFormatter.ISO_LOCAL_TIME.format(localTime);
    }
}
