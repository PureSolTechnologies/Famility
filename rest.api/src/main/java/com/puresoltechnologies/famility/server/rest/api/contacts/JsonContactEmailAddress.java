package com.puresoltechnologies.famility.server.rest.api.contacts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.puresoltechnologies.commons.types.EmailAddress;

public class JsonContactEmailAddress {

    private final EmailAddress emailAddress;
    private final long typeId;

    @JsonCreator
    public JsonContactEmailAddress( //
	    @JsonProperty("emailAddress") EmailAddress emailAddress, //
	    @JsonProperty("typeId") long typeId //
    ) {
	super();
	this.emailAddress = emailAddress;
	this.typeId = typeId;
    }

    public EmailAddress getEmailAddress() {
	return emailAddress;
    }

    public long getTypeId() {
	return typeId;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
	result = prime * result + (int) (typeId ^ (typeId >>> 32));
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	JsonContactEmailAddress other = (JsonContactEmailAddress) obj;
	if (emailAddress == null) {
	    if (other.emailAddress != null)
		return false;
	} else if (!emailAddress.equals(other.emailAddress))
	    return false;
	if (typeId != other.typeId)
	    return false;
	return true;
    }

}
