package com.puresoltechnologies.lifeassist.app.impl.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RestPerson {

    private final String name;
    private final String birthday;

    @JsonCreator
    public RestPerson(@JsonProperty("name") String name, @JsonProperty("birthday") String birthday) {
	super();
	this.name = name;
	this.birthday = birthday;
    }

    public String getName() {
	return name;
    }

    public String getBirthday() {
	return birthday;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	RestPerson other = (RestPerson) obj;
	if (birthday == null) {
	    if (other.birthday != null)
		return false;
	} else if (!birthday.equals(other.birthday))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

}
