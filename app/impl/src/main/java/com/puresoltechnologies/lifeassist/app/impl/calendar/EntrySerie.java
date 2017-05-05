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
 * This class is used to transfer the data for a single appointment serie.
 * 
 * @author Rick-Rainer Ludwig
 */
public class EntrySerie {

    private final long id;
    private final String type;
    private final String title;
    private final String description;
    private final Collection<Person> participants;
    private final boolean reminding;
    private final Reminder reminder;
    private final CalendarDay startDate;
    private final CalendarDay lastDate;
    private final String timezone;
    private final CalendarTime startTime;
    private final int durationAmount;
    private final ChronoUnit durationUnit;
    private final OccupancyStatus occupancy;
    private final Turnus turnus;
    private final int skipping;
    private final ZoneId zoneId;

    @JsonCreator
    public EntrySerie( //
	    @JsonProperty("id") long id, //
	    @JsonProperty("type") String type, //
	    @JsonProperty("title") String title, //
	    @JsonProperty("description") String description, //
	    @JsonProperty("participants") Collection<Person> participants, //
	    @JsonProperty("reminding") boolean reminding, //
	    @JsonProperty("reminder") Reminder reminder, //
	    @JsonProperty("startDate") CalendarDay startDate, //
	    @JsonProperty("lastDate") CalendarDay lastDate, //
	    @JsonProperty("timezone") String timezone, //
	    @JsonProperty("startTime") CalendarTime startTime, //
	    @JsonProperty("durationAmount") int durationAmount, //
	    @JsonProperty("durationUnit") ChronoUnit durationUnit, //
	    @JsonProperty("occupancy") OccupancyStatus occupancy, //
	    @JsonProperty("turnus") Turnus turnus, //
	    @JsonProperty("skipping") int skipping //
    ) {
	super();
	this.id = id;
	this.type = type;
	this.title = title;
	this.description = description;
	this.participants = participants;
	this.reminding = reminding;
	this.reminder = reminder;
	this.startDate = startDate;
	this.lastDate = lastDate;
	this.timezone = timezone;
	this.startTime = startTime;
	this.durationAmount = durationAmount;
	this.durationUnit = durationUnit;
	this.occupancy = occupancy;
	this.turnus = turnus;
	this.skipping = skipping;
	zoneId = ZoneId.of(timezone);
    }

    public EntrySerie( //
	    String type, //
	    String title, //
	    String description, //
	    Collection<Person> participants, //
	    boolean reminding, //
	    Reminder reminder, //
	    CalendarDay startDate, //
	    CalendarDay lastDate, //
	    String timezone, //
	    CalendarTime startTime, //
	    int durationAmount, //
	    ChronoUnit durationUnit, //
	    OccupancyStatus occupancy, //
	    Turnus turnus, //
	    int skipping //
    ) {
	this(-1l, type, title, description, participants, reminding, reminder, startDate, lastDate, timezone, startTime,
		durationAmount, durationUnit, occupancy, turnus, skipping);
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

    public boolean isReminding() {
	return reminding;
    }

    public Reminder getReminder() {
	return reminder;
    }

    public CalendarDay getStartDate() {
	return startDate;
    }

    public CalendarDay getLastDate() {
	return lastDate;
    }

    public String getTimezone() {
	return timezone;
    }

    public CalendarTime getStartTime() {
	return startTime;
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

    public Turnus getTurnus() {
	return turnus;
    }

    public int getSkipping() {
	return skipping;
    }

    @JsonIgnore
    public ZoneId getZoneId() {
	return zoneId;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + durationAmount;
	result = prime * result + ((durationUnit == null) ? 0 : durationUnit.hashCode());
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((lastDate == null) ? 0 : lastDate.hashCode());
	result = prime * result + ((occupancy == null) ? 0 : occupancy.hashCode());
	result = prime * result + ((participants == null) ? 0 : participants.hashCode());
	result = prime * result + ((reminder == null) ? 0 : reminder.hashCode());
	result = prime * result + (reminding ? 1231 : 1237);
	result = prime * result + skipping;
	result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
	result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
	result = prime * result + ((timezone == null) ? 0 : timezone.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((turnus == null) ? 0 : turnus.hashCode());
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
	EntrySerie other = (EntrySerie) obj;
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
	if (lastDate == null) {
	    if (other.lastDate != null)
		return false;
	} else if (!lastDate.equals(other.lastDate))
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
	if (skipping != other.skipping)
	    return false;
	if (startDate == null) {
	    if (other.startDate != null)
		return false;
	} else if (!startDate.equals(other.startDate))
	    return false;
	if (startTime == null) {
	    if (other.startTime != null)
		return false;
	} else if (!startTime.equals(other.startTime))
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
	if (turnus != other.turnus)
	    return false;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	if (zoneId == null) {
	    if (other.zoneId != null)
		return false;
	} else if (!zoneId.equals(other.zoneId))
	    return false;
	return true;
    }

}
