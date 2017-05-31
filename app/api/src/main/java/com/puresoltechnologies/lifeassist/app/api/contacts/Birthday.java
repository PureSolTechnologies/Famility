package com.puresoltechnologies.lifeassist.app.api.contacts;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * A single birthday information with additional information to be shown in UI.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Birthday implements Comparable<Birthday> {

    public static Birthday of(Contact person) {
	return new Birthday(person.getId(), person.getName(), person.getBirthday());
    }

    private final long id;
    private final String name;
    private final LocalDate birthday;
    private final boolean stillThisYear;
    private final LocalDate nextAnniversary;
    private final int nextAge;

    public Birthday(long id, String name, LocalDate birthday) {
	super();
	this.id = id;
	this.name = name;
	this.birthday = birthday;
	LocalDate today = LocalDate.now();
	LocalDate nextAnniversary = today;
	nextAnniversary.withMonth(birthday.getMonth().getValue());
	nextAnniversary.withDayOfMonth(birthday.getDayOfMonth());
	if (nextAnniversary.isBefore(today)) {
	    nextAnniversary.plus(1, ChronoUnit.YEARS);
	    stillThisYear = false;
	} else {
	    stillThisYear = true;
	}
	this.nextAnniversary = nextAnniversary;
	this.nextAge = (int) Duration.between(birthday, nextAnniversary).get(ChronoUnit.YEARS);
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

    public boolean isStillThisYear() {
	return stillThisYear;
    }

    public LocalDate getNextAnniversary() {
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
