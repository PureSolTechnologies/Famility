package com.puresoltechnologies.famility.server.test.calendar;

import com.puresoltechnologies.famility.server.impl.calendar.CalendarManager;
import com.puresoltechnologies.famility.server.test.AbstractFamilityTest;

public abstract class AbstractCalendarManagerTest extends AbstractFamilityTest {

    private CalendarManager calendarManager = new CalendarManager();

    protected CalendarManager getCalendarManager() {
	return calendarManager;
    }

}
