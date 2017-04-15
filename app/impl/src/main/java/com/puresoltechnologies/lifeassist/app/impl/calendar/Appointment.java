package com.puresoltechnologies.lifeassist.app.impl.calendar;

import java.time.Duration;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

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
    private final Reminder reminder;
    private final CalendarDay date;
    private final String timezone;
    private final CalendarTime time;
    private final int durationAmount;
    private final ChronoUnit durationUnit;
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
	    @JsonProperty("reminder") Reminder reminder, //
	    @JsonProperty("date") CalendarDay date, //
	    @JsonProperty("timezone") String timezone, //
	    @JsonProperty("time") CalendarTime time, //
	    @JsonProperty("durationAmount") int durationAmount, //
	    @JsonProperty("durationUnit") ChronoUnit durationUnit, //
	    @JsonProperty("occupancy") OccupancyStatus occupancy) {
	super();
	this.id = id;
	this.type = type;
	this.title = title;
	this.description = description;
	this.participants = participants;
	this.reminding = reminding;
	this.reminder = reminder;
	this.date = date;
	this.timezone = timezone;
	this.time = time;
	this.durationAmount = durationAmount;
	this.durationUnit = durationUnit;
	this.occupancy = occupancy;
	zoneId = ZoneId.of(timezone);
    }

    public Appointment(//
	    AppointmentType type, //
	    String title, //
	    String description, //
	    Collection<Person> participans, //
	    boolean reminding, //
	    Reminder reminder, //
	    CalendarDay date, //
	    String timezone, //
	    CalendarTime time, //
	    int durationAmount, //
	    ChronoUnit durationUnit, //
	    OccupancyStatus occupancy) {
	this(-1l, type, title, description, participans, reminding, reminder, date, timezone, time, durationAmount,
		durationUnit, occupancy);
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

    public Reminder getReminder() {
	return reminder;
    }

    public CalendarDay getDate() {
	return date;
    }

    public String getTimezone() {
	return timezone;
    }

    public CalendarTime getTime() {
	return time;
    }

    @JsonIgnore
    public Duration getDuration() {
	return Duration.of(durationAmount, durationUnit);
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
	result = prime * result + durationAmount;
	result = prime * result + ((durationUnit == null) ? 0 : durationUnit.hashCode());
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((occupancy == null) ? 0 : occupancy.hashCode());
	result = prime * result + ((participants == null) ? 0 : participants.hashCode());
	result = prime * result + ((reminder == null) ? 0 : reminder.hashCode());
	result = prime * result + (reminding ? 1231 : 1237);
	result = prime * result + ((time == null) ? 0 : time.hashCode());
	result = prime * result + ((timezone == null) ? 0 : timezone.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((type == null) ? 0 : type.hashCode());
	result = prime * result + ((zoneId == null) ? 0 : zoneId.hashCode());
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
	if (durationAmount != other.durationAmount)
	    return false;
	if (durationUnit != other.durationUnit)
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
	if (reminding != other.reminding)
	    return false;
	if (time == null) {
	    if (other.time != null)
		return false;
	} else if (!time.equals(other.time))
	    return false;
	if (timezone == null) {
	    if (other.timezone != null)
		return false;
	} else if (!timezone.equals(other.timezone))
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	if (type != other.type)
	    return false;
	if (zoneId == null) {
	    if (other.zoneId != null)
		return false;
	} else if (!zoneId.equals(other.zoneId))
	    return false;
	return true;
    }
}
