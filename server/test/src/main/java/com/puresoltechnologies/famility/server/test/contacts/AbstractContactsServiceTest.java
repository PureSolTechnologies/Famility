package com.puresoltechnologies.famility.server.test.contacts;

import com.puresoltechnologies.famility.server.rest.api.contacts.ContactsService;
import com.puresoltechnologies.famility.server.rest.impl.services.ContactsServiceImpl;
import com.puresoltechnologies.famility.server.test.AbstractRestTest;

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
