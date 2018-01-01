package com.puresoltechnologies.famility.server.rest.api.calendar;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CalendarMonth {

    private final int year;
    private final int month;
    private final Map<Integer, CalendarDay> days;
    private final YearMonth yearMonth;
    private final int quarterOfYear;
    private final String name;

    @JsonCreator
    public CalendarMonth(//
	    @JsonProperty("year") int year, //
	    @JsonProperty("month") int month, //
	    @JsonProperty("days") Map<Integer, CalendarDay> days) {
	super();
	this.year = year;
	this.month = month;
	this.days = days;
	yearMonth = YearMonth.of(year, month);
	quarterOfYear = yearMonth.get(IsoFields.QUARTER_OF_YEAR);
	name = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    public int getYear() {
	return year;
    }

    public int getMonth() {
	return month;
    }

    public int getQuarterOfYear() {
	return quarterOfYear;
    }

    public Map<Integer, CalendarDay> getDays() {
	return days;
    }

    @JsonIgnore
    public YearMonth getYearMonth() {
	return yearMonth;
    }

    public String getName() {
	return name;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((days == null) ? 0 : days.hashCode());
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
	CalendarMonth other = (CalendarMonth) obj;
	if (days == null) {
	    if (other.days != null)
		return false;
	} else if (!days.equals(other.days))
	    return false;
	if (month != other.month)
	    return false;
	if (year != other.year)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return yearMonth.toString();
    }
}
