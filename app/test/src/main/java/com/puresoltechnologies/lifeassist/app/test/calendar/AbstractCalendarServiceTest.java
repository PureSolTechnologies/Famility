package com.puresoltechnologies.lifeassist.app.test.calendar;

import com.puresoltechnologies.lifeassist.app.rest.services.CalendarService;
import com.puresoltechnologies.lifeassist.app.test.AbstractRestTest;

/**
 * This is the abstract class for {@link CalendarService} tests.
 * 
 * @author Rick-Rainer Ludwig
 */
public abstract class AbstractCalendarServiceTest extends AbstractRestTest {

    public AbstractCalendarServiceTest() {
	super(CalendarService.class);
    }

}
