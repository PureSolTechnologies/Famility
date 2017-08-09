package com.puresoltechnologies.famility.server.test.contacts;

import com.puresoltechnologies.famility.server.impl.contacts.ContactManagerImpl;
import com.puresoltechnologies.famility.server.test.AbstractFamilityTest;

public abstract class AbstractContactsManagerTest extends AbstractFamilityTest {

    private ContactManagerImpl peopleManager = new ContactManagerImpl();

    protected ContactManagerImpl getPeopleManager() {
	return peopleManager;
    }

}
