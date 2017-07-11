package com.puresoltechnologies.famility.server.test.contacts;

import com.puresoltechnologies.famility.server.impl.contacts.ContactManager;
import com.puresoltechnologies.famility.server.test.AbstractLifeAssistantTest;

public abstract class AbstractContactsManagerTest extends AbstractLifeAssistantTest {

    private ContactManager peopleManager = new ContactManager();

    protected ContactManager getPeopleManager() {
	return peopleManager;
    }

}
