package com.puresoltechnologies.lifeassist.app.impl.calendar;

import java.time.ZoneId;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.api.people.Person;

/**
 * This class is used to transfer the data for a single appointment.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Appointment {

    private final long id;
    private final AppointmentType type;
    private final String title;
    private final String description;
    private final Collection<Person> participants;
    private final boolean reminding;
    private final int timeAmount;
    private final TimeUnit timeUnit;
    private final CalendarDay date;
    private final String timezone;
    private final CalendarTime fromTime;
    private final CalendarTime toTime;
    private final OccupancyStatus occupancy;
    private final ZoneId zoneId;

    @JsonCreator
    public Appointment(//
	    @JsonProperty("id") long id, //
	    @JsonProperty("type") AppointmentType type, //
	    @JsonProperty("title") String title, //
	    @JsonProperty("description") String description, //
	    @JsonProperty("participants") Collection<Person> participants, //
	    @JsonProperty("reminding") boolean reminding, //
	    @JsonProperty("timeAmount") int timeAmount, //
	    @JsonProperty("timeUnit") TimeUnit timeUnit, //
	    @JsonProperty("date") CalendarDay date, //
	    @JsonProperty("timezone") String timezone, //
	    @JsonProperty("fromTime") CalendarTime fromTime, //
	    @JsonProperty("toTime") CalendarTime toTime, //
	    @JsonProperty("occupancy") OccupancyStatus occupancy) {
	super();
	this.id = id;
	this.type = type;
	this.title = title;
	this.description = description;
	this.participants = participants;
	this.reminding = reminding;
	this.timeAmount = timeAmount;
	this.timeUnit = timeUnit;
	this.date = date;
	this.timezone = timezone;
	this.fromTime = fromTime;
	this.toTime = toTime;
	this.occupancy = occupancy;
	zoneId = ZoneId.of(timezone);
    }

    public Appointment(//
	    AppointmentType type, //
	    String title, //
	    String description, //
	    Collection<Person> participans, //
	    boolean reminding, //
	    int timeAmount, //
	    TimeUnit timeUnit, //
	    CalendarDay date, //
	    String timezone, //
	    CalendarTime fromTime, //
	    CalendarTime toTime, //
	    OccupancyStatus occupancy) {
	this(-1l, type, title, description, participans, reminding, timeAmount, timeUnit, date, timezone, fromTime,
		toTime, occupancy);
    }

    public long getId() {
	return id;
    }

    public AppointmentType getType() {
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

    public boolean isReminding() {
	return reminding;
    }

    public int getTimeAmount() {
	return timeAmount;
    }

    public TimeUnit getTimeUnit() {
	return timeUnit;
    }

    public CalendarDay getDate() {
	return date;
    }

    public String getTimezone() {
	return timezone;
    }

    public CalendarTime getFromTime() {
	return fromTime;
    }

    public CalendarTime getToTime() {
	return toTime;
    }

    public OccupancyStatus getOccupancy() {
	return occupancy;
    }

    @JsonIgnore
    public ZoneId getZoneId() {
	return zoneId;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((date == null) ? 0 : date.hashCode());
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((fromTime == null) ? 0 : fromTime.hashCode());
	result = prime * result + ((occupancy == null) ? 0 : occupancy.hashCode());
	result = prime * result + ((participants == null) ? 0 : participants.hashCode());
	result = prime * result + (reminding ? 1231 : 1237);
	result = prime * result + timeAmount;
	result = prime * result + ((timeUnit == null) ? 0 : timeUnit.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((toTime == null) ? 0 : toTime.hashCode());
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
	Appointment other = (Appointment) obj;
	if (date == null) {
	    if (other.date != null)
		return false;
	} else if (!date.equals(other.date))
	    return false;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (fromTime == null) {
	    if (other.fromTime != null)
		return false;
	} else if (!fromTime.equals(other.fromTime))
	    return false;
	if (occupancy != other.occupancy)
	    return false;
	if (participants == null) {
	    if (other.participants != null)
		return false;
	} else if (!participants.equals(other.participants))
	    return false;
	if (reminding != other.reminding)
	    return false;
	if (timeAmount != other.timeAmount)
	    return false;
	if (timeUnit != other.timeUnit)
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	if (toTime == null) {
	    if (other.toTime != null)
		return false;
	} else if (!toTime.equals(other.toTime))
	    return false;
	if (type != other.type)
	    return false;
	return true;
    }

}
