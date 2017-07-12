package com.puresoltechnologies.famility.server.test.contacts;

import com.puresoltechnologies.famility.server.impl.contacts.ContactManager;
import com.puresoltechnologies.famility.server.test.AbstractFamilityTest;

public abstract class AbstractContactsManagerTest extends AbstractFamilityTest {

    private ContactManager peopleManager = new ContactManager();

    protected ContactManager getPeopleManager() {
	return peopleManager;
    }

}
