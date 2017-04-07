package com.puresoltechnologies.lifeassist.app.test.calendar;

import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.test.AbstractLifeAssistantTest;

public abstract class AbstractCalendarManagerTest extends AbstractLifeAssistantTest {

    private CalendarManager calendarManager = new CalendarManager();

    protected CalendarManager getCalendarManager() {
	return calendarManager;
    }

}
