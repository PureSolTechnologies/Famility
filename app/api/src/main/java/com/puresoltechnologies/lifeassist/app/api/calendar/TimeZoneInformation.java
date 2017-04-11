package com.puresoltechnologies.lifeassist.app.api.calendar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class contains the time zone information.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TimeZoneInformation implements Comparable<TimeZoneInformation> {

    private final String id;
    private final String name;
    private final int offset;

    @JsonCreator
    public TimeZoneInformation(//
	    @JsonProperty("id") String id, //
	    @JsonProperty("name") String name, //
	    @JsonProperty("offset") int offset) {
	this.id = id;
	this.name = name;
	this.offset = offset;
    }

    public String getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public int getOffset() {
	return offset;
    }

    @Override
    public int compareTo(TimeZoneInformation o) {
	if (offset < o.offset) {
	    return -1;
	} else if (offset > o.offset) {
	    return 1;
	}
	return id.compareTo(o.id);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
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
	TimeZoneInformation other = (TimeZoneInformation) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

}
