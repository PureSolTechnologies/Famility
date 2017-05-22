package com.puresoltechnologies.lifeassist.app.rest.api.calendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CalendarDateTime implements Comparable<CalendarDateTime> {

    public static CalendarDateTime of(LocalDateTime localDate) {
	return new CalendarDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(),
		localDate.getHour(), localDate.getMinute(), localDate.getSecond());
    }

    public static CalendarDateTime of(TemporalAccessor date) {
	int year = date.get(ChronoField.YEAR);
	int month = date.get(ChronoField.MONTH_OF_YEAR);
	int day = date.get(ChronoField.DAY_OF_MONTH);
	int hour = date.get(ChronoField.HOUR_OF_DAY);
	int minute = date.get(ChronoField.MINUTE_OF_HOUR);
	int second = date.get(ChronoField.SECOND_OF_MINUTE);
	return new CalendarDateTime(year, month, day, hour, minute, second);
    }

    public static LocalDateTime toLocalDateTime(CalendarDateTime calendarDay) {
	return LocalDateTime.of(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDayOfMonth(),
		calendarDay.getHour(), calendarDay.getMinute(), calendarDay.getSecond());
    }

    private final LocalDateTime localDateTime;
    private final int year;
    private final int month;
    private final int dayOfMonth;
    private final int dayOfWeek;
    private final int weekOfYear;
    private final int quarterOfYear;
    private final int hour;
    private final int minute;
    private final int second;

    @JsonCreator
    public CalendarDateTime(//
	    @JsonProperty("year") int year, //
	    @JsonProperty("month") int month, //
	    @JsonProperty("dayOfMonth") int dayOfMonth, //
	    @JsonProperty("hour") int hour, //
	    @JsonProperty("minute") int minute, //
	    @JsonProperty("second") int second //
    ) {
	super();
	this.year = year;
	this.month = month;
	this.dayOfMonth = dayOfMonth;
	this.hour = hour;
	this.minute = minute;
	this.second = second;
	localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
	this.dayOfWeek = localDateTime.getDayOfWeek().getValue();
	int woy = localDateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
	if ((month == 1) && (woy > 50)) {
	    this.weekOfYear = 0;
	} else {
	    this.weekOfYear = woy;
	}
	this.quarterOfYear = localDateTime.get(IsoFields.QUARTER_OF_YEAR);
    }

    @JsonIgnore
    public LocalDateTime getLocalDateTime() {
	return localDateTime;
    }

    public int getYear() {
	return year;
    }

    public int getMonth() {
	return month;
    }

    public int getDayOfMonth() {
	return dayOfMonth;
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

    public int getDayOfWeek() {
	return dayOfWeek;
    }

    public int getWeekOfYear() {
	return weekOfYear;
    }

    public int getQuarterOfYear() {
	return quarterOfYear;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + year;
	result = prime * result + month;
	result = prime * result + dayOfMonth;
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
	CalendarDateTime other = (CalendarDateTime) obj;
	if (year != other.year)
	    return false;
	if (month != other.month)
	    return false;
	if (dayOfMonth != other.dayOfMonth)
	    return false;
	if (hour != other.hour)
	    return false;
	if (minute != other.minute)
	    return false;
	if (second != other.second)
	    return false;
	return true;
    }

    @Override
    public int compareTo(CalendarDateTime o) {
	if (this.year < o.year) {
	    return -1;
	}
	if (this.year > o.year) {
	    return 1;
	}
	if (this.month < o.month) {
	    return -1;
	}
	if (this.month > o.month) {
	    return 1;
	}
	if (this.dayOfMonth < o.dayOfMonth) {
	    return -1;
	}
	if (this.dayOfMonth > o.dayOfMonth) {
	    return 1;
	}
	if (this.hour < o.hour) {
	    return -1;
	}
	if (this.hour > o.hour) {
	    return 1;
	}
	if (this.minute < o.minute) {
	    return -1;
	}
	if (this.minute > o.minute) {
	    return 1;
	}
	if (this.second < o.second) {
	    return -1;
	}
	if (this.second > o.second) {
	    return 1;
	}
	return 0;
    }

    @Override
    public String toString() {
	return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime);
    }

}
