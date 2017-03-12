package com.puresoltechnologies.lifeassist.app.impl.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RestPerson {

    private final long id;
    private final String name;
    private final String birthday;

    @JsonCreator
    public RestPerson(@JsonProperty("id") long id, @JsonProperty("name") String name,
	    @JsonProperty("birthday") String birthday) {
	super();
	this.id = id;
	this.name = name;
	this.birthday = birthday;
    }

    public long getId() {
	return id;
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
	result = prime * result + (int) (id ^ (id >>> 32));
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
	if (id != other.id)
	    return false;
	return true;
    }

}
