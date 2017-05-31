package com.puresoltechnologies.lifeassist.app.test.contacts;

import com.puresoltechnologies.lifeassist.app.rest.api.contacts.ContactsService;
import com.puresoltechnologies.lifeassist.app.rest.server.services.ContactsServiceImpl;
import com.puresoltechnologies.lifeassist.app.test.AbstractRestTest;

/**
 * This is the abstract class for {@link ContactsService} tests.
 * 
 * @author Rick-Rainer Ludwig
 */
public abstract class AbstractContactsServiceTest extends AbstractRestTest {

    public AbstractContactsServiceTest() {
	super(ContactsServiceImpl.class);
    }

}
