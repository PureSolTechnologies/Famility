package com.puresoltechnologies.lifeassist.app.api.calendar;

import java.time.temporal.ChronoUnit;

/**
 * A class containing a single duration unit definitin.
 * 
 * @author Rick-Rainer Ludwig
 */
public class DurationUnit {

    private final ChronoUnit unit;
    private final String name;

    public DurationUnit( //
	    ChronoUnit unit, //
	    String name //
    ) {
	super();
	this.unit = unit;
	this.name = name;
    }

    public ChronoUnit getUnit() {
	return unit;
    }

    public String getName() {
	return name;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
	DurationUnit other = (DurationUnit) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (unit != other.unit)
	    return false;
	return true;
    }

}
