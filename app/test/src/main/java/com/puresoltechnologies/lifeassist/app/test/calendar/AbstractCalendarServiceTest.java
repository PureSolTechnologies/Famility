package com.puresoltechnologies.lifeassist.app.test.calendar;

import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarService;
import com.puresoltechnologies.lifeassist.app.rest.server.services.CalendarServiceImpl;
import com.puresoltechnologies.lifeassist.app.test.AbstractRestTest;

/**
 * This is the abstract class for {@link CalendarService} tests.
 * 
 * @author Rick-Rainer Ludwig
 */
public abstract class AbstractCalendarServiceTest extends AbstractRestTest {

    public AbstractCalendarServiceTest() {
	super(CalendarServiceImpl.class);
    }

}
