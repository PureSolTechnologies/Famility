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

    private final long id;
    private final String name;
    private final CalendarDay birthday;

    @JsonCreator
    public Person(@JsonProperty("id") long id, @JsonProperty("name") String name,
	    @JsonProperty("birthday") CalendarDay birthday) {
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

    public CalendarDay getBirthday() {
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
	Person other = (Person) obj;
	if (id != other.id)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return name + " (" + birthday + ")";
    }
}
