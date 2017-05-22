package com.puresoltechnologies.lifeassist.app.rest.api.people;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarDay;

/**
 * A single birthday information with additional information to be shown in UI.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Birthday implements Comparable<Birthday> {

    public static Birthday of(Person person) {
	return new Birthday(person.getId(), person.getName(), person.getBirthday());
    }

    private final long id;
    private final String name;
    private final CalendarDay birthday;
    private final boolean stillThisYear;
    private final CalendarDay nextAnniversary;
    private final int nextAge;

    @JsonCreator
    public Birthday(@JsonProperty("id") long id, @JsonProperty("name") String name,
	    @JsonProperty("birthday") CalendarDay birthday) {
	super();
	this.id = id;
	this.name = name;
	this.birthday = birthday;
	LocalDate today = LocalDate.now();
	if (birthday.getMonth() < today.getMonthValue()) {
	    stillThisYear = false;
	} else if ((birthday.getMonth() == today.getDayOfMonth())
		&& (birthday.getDayOfMonth() < today.getDayOfMonth())) {
	    stillThisYear = false;
	} else {
	    stillThisYear = true;
	}
	if (isStillThisYear()) {
	    nextAnniversary = new CalendarDay(today.getYear(), birthday.getMonth(), birthday.getDayOfMonth());
	} else {
	    nextAnniversary = new CalendarDay(today.getYear() + 1, birthday.getMonth(), birthday.getDayOfMonth());
	}
	nextAge = today.getYear() - birthday.getYear() + (stillThisYear ? 0 : 1);
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

    public boolean isStillThisYear() {
	return stillThisYear;
    }

    public CalendarDay getNextAnniversary() {
	return nextAnniversary;
    }

    public int getNextAge() {
	return nextAge;
    }

    @Override
    public int compareTo(Birthday other) {
	return getNextAnniversary().compareTo(other.getNextAnniversary());
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
	Birthday other = (Birthday) obj;
	if (id != other.id)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return name + " (" + birthday + ")";
    }
}
