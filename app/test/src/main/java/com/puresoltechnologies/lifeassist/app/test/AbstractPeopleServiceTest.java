package com.puresoltechnologies.lifeassist.app.test;

import com.puresoltechnologies.lifeassist.app.rest.services.PeopleService;

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
