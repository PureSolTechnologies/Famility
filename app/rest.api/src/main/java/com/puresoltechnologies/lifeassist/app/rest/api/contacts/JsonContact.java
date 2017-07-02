package com.puresoltechnologies.lifeassist.app.rest.api.contacts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarDay;

/**
 * A single person.
 * 
 * @author Rick-Rainer Ludwig
 */
public class JsonContact {

    private final long id;
    private final String name;
    private final CalendarDay birthday;

    @JsonCreator
    public JsonContact(@JsonProperty("id") long id, @JsonProperty("name") String name,
	    @JsonProperty("birthday") CalendarDay birthday) {
	super();
	this.id = id;
	this.name = name;
	this.birthday = birthday;
    }

    public JsonContact(String name, CalendarDay birthday) {
	this(-1, name, birthday);
    }

    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public CalendarDay getBirthday() {
	return birthday;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
	result = prime * result + (int) (id ^ (id >>> 32));
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
	JsonContact other = (JsonContact) obj;
	if (birthday == null) {
	    if (other.birthday != null)
		return false;
	} else if (!birthday.equals(other.birthday))
	    return false;
	if (id != other.id)
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return name + " (" + birthday + ")";
    }
}
