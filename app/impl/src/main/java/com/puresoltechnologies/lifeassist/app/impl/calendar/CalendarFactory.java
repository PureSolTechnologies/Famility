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

}
