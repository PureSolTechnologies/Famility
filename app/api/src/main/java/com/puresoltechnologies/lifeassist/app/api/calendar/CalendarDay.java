package com.puresoltechnologies.lifeassist.app.api.calendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CalendarDay {

    private final LocalDate localDate;
    private final int year;
    private final int month;
    private final int dayOfMonth;
    private final int dayOfWeek;
    private final int weekOfYear;
    private final int quarterOfYear;

    @JsonCreator
    public CalendarDay(//
	    @JsonProperty("year") int year, //
	    @JsonProperty("month") int month, //
	    @JsonProperty("dayOfMonth") int dayOfMonth) {
	super();
	this.year = year;
	this.month = month;
	this.dayOfMonth = dayOfMonth;
	localDate = LocalDate.of(year, month, dayOfMonth);
	this.dayOfWeek = localDate.getDayOfWeek().getValue();
	this.weekOfYear = localDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
	this.quarterOfYear = localDate.get(IsoFields.QUARTER_OF_YEAR);
    }

    @JsonIgnore
    public LocalDate getLocalDate() {
	return localDate;
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
	result = prime * result + dayOfMonth;
	result = prime * result + month;
	result = prime * result + year;
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
	CalendarDay other = (CalendarDay) obj;
	if (dayOfMonth != other.dayOfMonth)
	    return false;
	if (month != other.month)
	    return false;
	if (year != other.year)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return DateTimeFormatter.ISO_LOCAL_DATE.format(localDate);
    }

}
