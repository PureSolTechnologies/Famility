package com.puresoltechnologies.lifeassist.app.api.people;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;

/**
 * A single person.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Person {

    private final String name;
    private final CalendarDay birthday;

    @JsonCreator
    public Person(@JsonProperty("name") String name, @JsonProperty("birthday") CalendarDay birthday) {
	super();
	this.name = name;
	this.birthday = birthday;
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
	Person other = (Person) obj;
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

    @Override
    public String toString() {
	return name + " (" + birthday + ")";
    }
}
