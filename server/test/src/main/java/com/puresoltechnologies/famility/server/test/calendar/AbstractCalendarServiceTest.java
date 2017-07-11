package com.puresoltechnologies.famility.server.test.calendar;

import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarService;
import com.puresoltechnologies.famility.server.rest.impl.services.CalendarServiceImpl;
import com.puresoltechnologies.famility.server.test.AbstractRestTest;

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
