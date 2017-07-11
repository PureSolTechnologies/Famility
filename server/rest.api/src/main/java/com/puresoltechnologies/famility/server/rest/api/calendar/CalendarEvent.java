package com.puresoltechnologies.famility.server.rest.api.calendar;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.puresoltechnologies.famility.server.rest.api.contacts.JsonContact;

/**
 * This class is used to transfer the data for a single appointment.
 * 
 * @author Rick-Rainer Ludwig
 */
public class CalendarEvent implements Comparable<CalendarEvent> {

    private final long id;
    private final String type;
    private final String title;
    private final String description;
    private final Collection<JsonContact> participants;
    private final boolean reminding;
    private final Reminder reminder;
    private final CalendarDay beginDate;
    private final CalendarTime beginTime;
    private final String beginTimezone;
    private final CalendarDay endDate;
    private final CalendarTime endTime;
    private final String endTimezone;
    private final String occupancy;
    private final ZonedDateTime begin;
    private final ZonedDateTime end;

    @JsonCreator
    public CalendarEvent( //
	    @JsonProperty("id") long id, //
	    @JsonProperty("type") String type, //
	    @JsonProperty("title") String title, //
	    @JsonProperty("description") String description, //
	    @JsonProperty("participants") Collection<JsonContact> participants, //
	    @JsonProperty("reminding") boolean reminding, //
	    @JsonProperty("reminder") Reminder reminder, //
	    @JsonProperty("beginDate") CalendarDay beginDate, //
	    @JsonProperty("beginTime") CalendarTime beginTime, //
	    @JsonProperty("beginTimezone") String beginTimezone, //
	    @JsonProperty("endDate") CalendarDay endDate, //
	    @JsonProperty("endTime") CalendarTime endTime, //
	    @JsonProperty("endTimezone") String endTimezone, //
	    @JsonProperty("occupancy") String occupancy) {
	super();
	this.id = id;
	this.type = type;
	this.title = title;
	this.description = description;
	this.participants = participants;
	this.reminding = reminding;
	this.reminder = reminder;
	this.beginDate = beginDate;
	this.beginTime = beginTime;
	this.beginTimezone = beginTimezone;
	this.endDate = endDate;
	this.endTime = endTime;
	this.endTimezone = endTimezone;
	this.occupancy = occupancy;
	begin = ZonedDateTime.of(CalendarDay.toLocalDate(beginDate), CalendarTime.toLocalTime(beginTime),
		ZoneId.of(beginTimezone));
	end = ZonedDateTime.of(CalendarDay.toLocalDate(beginDate), CalendarTime.toLocalTime(beginTime),
		ZoneId.of(beginTimezone));
    }

    public CalendarEvent(//
	    String type, //
	    String title, //
	    String description, //
	    Collection<JsonContact> participans, //
	    boolean reminding, //
	    Reminder reminder, //
	    CalendarDay beginDate, //
	    CalendarTime beginTime, //
	    String beginTimezone, //
	    CalendarDay endDate, //
	    CalendarTime endTime, //
	    String endTimezone, //
	    String occupancy) {
	this(-1l, type, title, description, participans, reminding, reminder, beginDate, beginTime, beginTimezone,
		endDate, endTime, endTimezone, occupancy);
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

    public Collection<JsonContact> getParticipants() {
	return participants;
    }

    public boolean isReminding() {
	return reminding;
    }

    public Reminder getReminder() {
	return reminder;
    }

    public CalendarDay getBeginDate() {
	return beginDate;
    }

    public CalendarTime getBeginTime() {
	return beginTime;
    }

    public String getBeginTimezone() {
	return beginTimezone;
    }

    public CalendarDay getEndDate() {
	return endDate;
    }

    public CalendarTime getEndTime() {
	return endTime;
    }

    public String getEndTimezone() {
	return endTimezone;
    }

    public String getOccupancy() {
	return occupancy;
    }

    @JsonIgnore
    public ZonedDateTime getBegin() {
	return begin;
    }

    @JsonIgnore
    public ZonedDateTime getEnd() {
	return end;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((begin == null) ? 0 : begin.hashCode());
	result = prime * result + ((beginDate == null) ? 0 : beginDate.hashCode());
	result = prime * result + ((beginTime == null) ? 0 : beginTime.hashCode());
	result = prime * result + ((beginTimezone == null) ? 0 : beginTimezone.hashCode());
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((end == null) ? 0 : end.hashCode());
	result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
	result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
	result = prime * result + ((endTimezone == null) ? 0 : endTimezone.hashCode());
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((occupancy == null) ? 0 : occupancy.hashCode());
	result = prime * result + ((participants == null) ? 0 : participants.hashCode());
	result = prime * result + ((reminder == null) ? 0 : reminder.hashCode());
	result = prime * result + (reminding ? 1231 : 1237);
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
	CalendarEvent other = (CalendarEvent) obj;
	if (begin == null) {
	    if (other.begin != null)
		return false;
	} else if (!begin.equals(other.begin))
	    return false;
	if (beginDate == null) {
	    if (other.beginDate != null)
		return false;
	} else if (!beginDate.equals(other.beginDate))
	    return false;
	if (beginTime == null) {
	    if (other.beginTime != null)
		return false;
	} else if (!beginTime.equals(other.beginTime))
	    return false;
	if (beginTimezone == null) {
	    if (other.beginTimezone != null)
		return false;
	} else if (!beginTimezone.equals(other.beginTimezone))
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
	if (endDate == null) {
	    if (other.endDate != null)
		return false;
	} else if (!endDate.equals(other.endDate))
	    return false;
	if (endTime == null) {
	    if (other.endTime != null)
		return false;
	} else if (!endTime.equals(other.endTime))
	    return false;
	if (endTimezone == null) {
	    if (other.endTimezone != null)
		return false;
	} else if (!endTimezone.equals(other.endTimezone))
	    return false;
	if (id != other.id)
	    return false;
	if (occupancy == null) {
	    if (other.occupancy != null)
		return false;
	} else if (!occupancy.equals(other.occupancy))
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
	if (reminding != other.reminding)
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
	return type + " at " + beginDate + " " + beginTime + ": '" + title + "'";
    }

    @Override
    public int compareTo(CalendarEvent o) {
	int result = begin.compareTo(o.begin);
	if (result != 0) {
	    return result;
	}
	return end.compareTo(o.end);
    }

}
