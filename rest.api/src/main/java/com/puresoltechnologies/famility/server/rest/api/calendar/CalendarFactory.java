package com.puresoltechnologies.famility.server.rest.api.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.IsoChronology;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

public class CalendarFactory {

    public static CalendarYear createYear(int year) {
	Map<Integer, CalendarMonth> calendarMonths = new HashMap<>();
	for (Month month : Month.values()) {
	    int monthValue = month.getValue();
	    Map<Integer, CalendarDay> calendarDays = new HashMap<>();
	    LocalDate localDate = IsoChronology.INSTANCE.date(year, monthValue, 1);
	    for (int i = 1; i <= localDate.lengthOfMonth(); ++i) {
		CalendarDay calendarDay = new CalendarDay(year, monthValue, i);
		calendarDays.put(i, calendarDay);
	    }
	    CalendarMonth calendarMonth = new CalendarMonth(year, monthValue, calendarDays);
	    calendarMonths.put(monthValue, calendarMonth);
	}
	CalendarYear calendarYear = new CalendarYear(year, calendarMonths);
	return calendarYear;
    }

    public static CalendarDay findWeek(int year, int week) {
	LocalDate desiredDate = LocalDate.now() //
		.with(IsoFields.WEEK_BASED_YEAR, year) //
		.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week) //
		.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	return CalendarDay.of(desiredDate);
    }
}
