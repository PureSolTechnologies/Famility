package com.puresoltechnologies.lifeassist.app.impl.calendar;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.api.people.Person;

/**
 * This class is used to transfer the data for a single appointment serie.
 * 
 * @author Rick-Rainer Ludwig
 */
public class AppointmentSerie {

    private final long id;
    private final AppointmentType type;
    private final String title;
    private final String description;
    private final Collection<Person> participans;
    private final boolean reminding;
    private final int timeAmount;
    private final TimeUnit timeUnit;
    private final CalendarDay startDate;
    private final CalendarTime fromTime;
    private final CalendarTime toTime;
    private final OccupancyStatus occupany;
    private final Turnus turnus;
    private final int skipping;

    @JsonCreator
    public AppointmentSerie( //
	    @JsonProperty("id") long id, //
	    @JsonProperty("type") AppointmentType type, //
	    @JsonProperty("title") String title, //
	    @JsonProperty("description") String description, //
	    @JsonProperty("participans") Collection<Person> participans, //
	    @JsonProperty("reminding") boolean reminding, //
	    @JsonProperty("timeAmount") int timeAmount, //
	    @JsonProperty("timeUnit") TimeUnit timeUnit, //
	    @JsonProperty("startDate") CalendarDay startDate, //
	    @JsonProperty("fromTime") CalendarTime fromTime, //
	    @JsonProperty("toTime") CalendarTime toTime, //
	    @JsonProperty("occupany") OccupancyStatus occupany, //
	    @JsonProperty("turnus") Turnus turnus, //
	    @JsonProperty("skipping") int skipping //
    ) {
	super();
	this.id = id;
	this.type = type;
	this.title = title;
	this.description = description;
	this.participans = participans;
	this.reminding = reminding;
	this.timeAmount = timeAmount;
	this.timeUnit = timeUnit;
	this.startDate = startDate;
	this.fromTime = fromTime;
	this.toTime = toTime;
	this.occupany = occupany;
	this.turnus = turnus;
	this.skipping = skipping;
    }

    public AppointmentSerie( //
	    AppointmentType type, //
	    String title, //
	    String description, //
	    Collection<Person> participans, //
	    boolean reminding, //
	    int timeAmount, //
	    TimeUnit timeUnit, //
	    CalendarDay startDate, //
	    CalendarTime fromTime, //
	    CalendarTime toTime, //
	    OccupancyStatus occupany, //
	    Turnus turnus, //
	    int skipping //
    ) {
	this(-1l, type, title, description, participans, reminding, timeAmount, timeUnit, startDate, fromTime, toTime,
		occupany, turnus, skipping);
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

    public Collection<Person> getParticipans() {
	return participans;
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

    public CalendarDay getStartDate() {
	return startDate;
    }

    public CalendarTime getFromTime() {
	return fromTime;
    }

    public CalendarTime getToTime() {
	return toTime;
    }

    public OccupancyStatus getOccupany() {
	return occupany;
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
	result = prime * result + ((fromTime == null) ? 0 : fromTime.hashCode());
	result = prime * result + ((occupany == null) ? 0 : occupany.hashCode());
	result = prime * result + ((participans == null) ? 0 : participans.hashCode());
	result = prime * result + ((turnus == null) ? 0 : turnus.hashCode());
	result = prime * result + (reminding ? 1231 : 1237);
	result = prime * result + skipping;
	result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
	AppointmentSerie other = (AppointmentSerie) obj;
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
	if (occupany != other.occupany)
	    return false;
	if (participans == null) {
	    if (other.participans != null)
		return false;
	} else if (!participans.equals(other.participans))
	    return false;
	if (turnus != other.turnus)
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
