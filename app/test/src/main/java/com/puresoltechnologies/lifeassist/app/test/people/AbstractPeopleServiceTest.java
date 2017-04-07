package com.puresoltechnologies.lifeassist.app.test.people;

import com.puresoltechnologies.lifeassist.app.rest.services.PeopleService;
import com.puresoltechnologies.lifeassist.app.test.AbstractRestTest;

/**
 * This is the abstract class for {@link PeopleService} tests.
 * 
 * @author Rick-Rainer Ludwig
 */
public abstract class AbstractPeopleServiceTest extends AbstractRestTest {

    public AbstractPeopleServiceTest() {
	super(PeopleService.class);
    }

}
