package com.puresoltechnologies.famility.server.rest.api.calendar;

import java.time.Year;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CalendarYear {

    private final int year;
    private final Map<Integer, CalendarMonth> months;
    private final boolean leap;

    @JsonCreator
    public CalendarYear(//
	    @JsonProperty("year") int year, //
	    @JsonProperty("months") Map<Integer, CalendarMonth> months) {
	super();
	this.year = year;
	this.months = months;
	this.leap = Year.isLeap(year);
    }

    public int getYear() {
	return year;
    }

    public Map<Integer, CalendarMonth> getMonths() {
	return months;
    }

    public boolean isLeap() {
	return leap;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((months == null) ? 0 : months.hashCode());
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
	CalendarYear other = (CalendarYear) obj;
	if (months == null) {
	    if (other.months != null)
		return false;
	} else if (!months.equals(other.months))
	    return false;
	if (year != other.year)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return String.valueOf(year);
    }
}
