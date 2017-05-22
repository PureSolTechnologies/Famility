package com.puresoltechnologies.lifeassist.app.api.calendar;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import com.puresoltechnologies.lifeassist.app.api.people.Person;

/**
 * This class is used to transfer the data for a single appointment serie.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Series {

    private final long id;
    private final String type;
    private final String title;
    private final String description;
    private final Collection<Person> participants;
    private final Reminder reminder;
    private final ZonedDateTime firstOccurence;
    private final LocalDate lastOccurence;
    private final int durationAmount;
    private final ChronoUnit durationUnit;
    private final OccupancyStatus occupancy;
    private final Turnus turnus;
    private final int skipping;

    public Series(long id, String type, String title, String description, Collection<Person> participants,
	    Reminder reminder, ZonedDateTime firstOccurence, LocalDate lastOccurence, int durationAmount,
	    ChronoUnit durationUnit, OccupancyStatus occupancy, Turnus turnus, int skipping) {
	super();
	this.id = id;
	this.type = type;
	this.title = title;
	this.description = description;
	this.participants = participants;
	this.reminder = reminder;
	this.firstOccurence = firstOccurence;
	this.lastOccurence = lastOccurence;
	this.durationAmount = durationAmount;
	this.durationUnit = durationUnit;
	this.occupancy = occupancy;
	this.turnus = turnus;
	this.skipping = skipping;
    }

    public Series(String type, String title, String description, Collection<Person> participants, Reminder reminder,
	    ZonedDateTime firstOccurence, LocalDate lastOccurence, int durationAmount, ChronoUnit durationUnit,
	    OccupancyStatus occupancy, Turnus turnus, int skipping) {
	this(-1, type, title, description, participants, reminder, firstOccurence, lastOccurence, durationAmount,
		durationUnit, occupancy, turnus, skipping);
    }

    public long getId() {
	return id;
    }

    public String getType() {
	return type;
    }

    public String getTitle() {
	return title;
    }

    public String getDescription() {
	return description;
    }

    public Collection<Person> getParticipants() {
	return participants;
    }

    public Reminder getReminder() {
	return reminder;
    }

    public ZonedDateTime getFirstOccurence() {
	return firstOccurence;
    }

    public LocalDate getLastOccurence() {
	return lastOccurence;
    }

    public int getDurationAmount() {
	return durationAmount;
    }

    public ChronoUnit getDurationUnit() {
	return durationUnit;
    }

    public OccupancyStatus getOccupancy() {
	return occupancy;
    }

    public Turnus getTurnus() {
	return turnus;
    }

    public int getSkipping() {
	return skipping;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + durationAmount;
	result = prime * result + ((durationUnit == null) ? 0 : durationUnit.hashCode());
	result = prime * result + ((firstOccurence == null) ? 0 : firstOccurence.hashCode());
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((lastOccurence == null) ? 0 : lastOccurence.hashCode());
	result = prime * result + ((occupancy == null) ? 0 : occupancy.hashCode());
	result = prime * result + ((participants == null) ? 0 : participants.hashCode());
	result = prime * result + ((reminder == null) ? 0 : reminder.hashCode());
	result = prime * result + skipping;
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((turnus == null) ? 0 : turnus.hashCode());
	result = prime * result + ((type == null) ? 0 : type.hashCode());
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
	Series other = (Series) obj;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (durationAmount != other.durationAmount)
	    return false;
	if (durationUnit != other.durationUnit)
	    return false;
	if (firstOccurence == null) {
	    if (other.firstOccurence != null)
		return false;
	} else if (!firstOccurence.equals(other.firstOccurence))
	    return false;
	if (id != other.id)
	    return false;
	if (lastOccurence == null) {
	    if (other.lastOccurence != null)
		return false;
	} else if (!lastOccurence.equals(other.lastOccurence))
	    return false;
	if (occupancy != other.occupancy)
	    return false;
	if (participants == null) {
	    if (other.participants != null)
		return false;
	} else if (!participants.equals(other.participants))
	    return false;
	if (reminder == null) {
	    if (other.reminder != null)
		return false;
	} else if (!reminder.equals(other.reminder))
	    return false;
	if (skipping != other.skipping)
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	if (turnus != other.turnus)
	    return false;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	return true;
    }

}
