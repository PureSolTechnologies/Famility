package com.puresoltechnologies.lifeassist.app.api.people;

import java.time.LocalDate;

/**
 * A single person.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Person {

    private final long id;
    private final String name;
    private final LocalDate birthday;

    public Person(long id, //
	    String name, //
	    LocalDate birthday) {
	super();
	this.id = id;
	this.name = name;
	this.birthday = birthday;
    }

    public Person(String name, LocalDate birthday) {
	this(-1, name, birthday);
    }

    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public LocalDate getBirthday() {
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
	Person other = (Person) obj;
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
