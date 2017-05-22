package com.puresoltechnologies.lifeassist.app.api.calendar;

import java.time.ZonedDateTime;
import java.util.Collection;

import com.puresoltechnologies.lifeassist.app.api.people.Person;

/**
 * This class is used to transfer the data for a single appointment.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Entry implements Comparable<Entry> {

    private final long id;
    private final String type;
    private final String title;
    private final String description;
    private final Collection<Person> participants;
    private final Reminder reminder;
    private final ZonedDateTime begin;
    private final ZonedDateTime end;
    private final OccupancyStatus occupancy;

    public Entry(long id, String type, String title, String description, Collection<Person> participants,
	    Reminder reminder, ZonedDateTime begin, ZonedDateTime end, OccupancyStatus occupancy) {
	super();
	this.id = id;
	this.type = type;
	this.title = title;
	this.description = description;
	this.participants = participants;
	this.reminder = reminder;
	this.begin = begin;
	this.end = end;
	this.occupancy = occupancy;
    }

    public Entry(String type, String title, String description, Collection<Person> participants, Reminder reminder,
	    ZonedDateTime begin, ZonedDateTime end, OccupancyStatus occupancy) {
	this(-1, type, title, description, participants, reminder, begin, end, occupancy);
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

    public ZonedDateTime getBegin() {
	return begin;
    }

    public ZonedDateTime getEnd() {
	return end;
    }

    public OccupancyStatus getOccupancy() {
	return occupancy;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((begin == null) ? 0 : begin.hashCode());
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((end == null) ? 0 : end.hashCode());
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((occupancy == null) ? 0 : occupancy.hashCode());
	result = prime * result + ((participants == null) ? 0 : participants.hashCode());
	result = prime * result + ((reminder == null) ? 0 : reminder.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
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
	Entry other = (Entry) obj;
	if (begin == null) {
	    if (other.begin != null)
		return false;
	} else if (!begin.equals(other.begin))
	    return false;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (end == null) {
	    if (other.end != null)
		return false;
	} else if (!end.equals(other.end))
	    return false;
	if (id != other.id)
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
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return type + " at " + begin + " until " + end + ": '" + title + "'";
    }

    @Override
    public int compareTo(Entry o) {
	int result = begin.compareTo(o.begin);
	if (result != 0) {
	    return result;
	}
	return end.compareTo(o.end);
    }

}
