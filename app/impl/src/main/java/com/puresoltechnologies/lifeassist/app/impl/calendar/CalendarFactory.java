package com.puresoltechnologies.lifeassist.app.impl.calendar;

import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.IsoChronology;
import java.util.HashMap;
import java.util.Map;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarMonth;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarYear;

public class CalendarFactory {

    public static CalendarYear createYear(int year) {
	Map<Integer, CalendarMonth> calendarMonths = new HashMap<>();
	for (Month month : Month.values()) {
	    Map<Integer, CalendarDay> calendarDays = new HashMap<>();
	    LocalDate localDate = IsoChronology.INSTANCE.date(year, month.getValue(), 1);
	    for (int i = 1; i <= localDate.lengthOfMonth(); ++i) {
		CalendarDay calendarDay = new CalendarDay(year, month.getValue(), i);
		calendarDays.put(i, calendarDay);
	    }
	    CalendarMonth calendarMonth = new CalendarMonth(year, month.getValue(), calendarDays);
	    calendarMonths.put(month.getValue(), calendarMonth);
	}
	CalendarYear calendarYear = new CalendarYear(year, calendarMonths);
	return calendarYear;
    }

}
