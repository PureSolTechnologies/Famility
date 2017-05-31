package com.puresoltechnologies.lifeassist.app.test.contacts;

import com.puresoltechnologies.lifeassist.app.impl.contacts.ContactManager;
import com.puresoltechnologies.lifeassist.app.test.AbstractLifeAssistantTest;

public abstract class AbstractContactsManagerTest extends AbstractLifeAssistantTest {

    private ContactManager peopleManager = new ContactManager();

    protected ContactManager getPeopleManager() {
	return peopleManager;
    }

}
