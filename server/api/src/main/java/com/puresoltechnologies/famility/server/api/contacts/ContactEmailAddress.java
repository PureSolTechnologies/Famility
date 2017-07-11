package com.puresoltechnologies.famility.server.api.contacts;

import com.puresoltechnologies.commons.types.EmailAddress;

public class ContactEmailAddress {

    private final long contactId;
    private final EmailAddress emailAddress;
    private final long typeId;

    public ContactEmailAddress(long contactId, EmailAddress emailAddress, long typeId) {
	super();
	this.contactId = contactId;
	this.emailAddress = emailAddress;
	this.typeId = typeId;
    }

    public long getId() {
	return contactId;
    }

    public EmailAddress getEmailAddress() {
	return emailAddress;
    }

    public long getTypeId() {
	return typeId;
    }

}
