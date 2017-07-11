package com.puresoltechnologies.famility.server.test.calendar;

import com.puresoltechnologies.famility.server.impl.calendar.CalendarManager;
import com.puresoltechnologies.famility.server.test.AbstractLifeAssistantTest;

public abstract class AbstractCalendarManagerTest extends AbstractLifeAssistantTest {

    private CalendarManager calendarManager = new CalendarManager();

    protected CalendarManager getCalendarManager() {
	return calendarManager;
    }

}
