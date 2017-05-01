package com.puresoltechnologies.lifeassist.app.test.calendar;

import com.puresoltechnologies.lifeassist.app.impl.people.PeopleManager;
import com.puresoltechnologies.lifeassist.app.test.AbstractLifeAssistantTest;

public abstract class AbstractPeopleManagerTest extends AbstractLifeAssistantTest {

    private PeopleManager peopleManager = new PeopleManager();

    protected PeopleManager getPeopleManager() {
	return peopleManager;
    }

}
