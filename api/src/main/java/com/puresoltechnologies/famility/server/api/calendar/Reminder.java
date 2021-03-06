package com.puresoltechnologies.famility.server.api.calendar;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a single reminder configuration.
 * 
 * @author Rick-Rainer Ludwig
 *
 */
public class Reminder {

    private final int amount;
    private final ChronoUnit unit;

    public Reminder( //
	    int amount, //
	    ChronoUnit unit //
    ) {
	super();
	this.amount = amount;
	this.unit = unit;
    }

    public int getAmount() {
	return amount;
    }

    public ChronoUnit getUnit() {
	return unit;
    }

    public Duration getDuration() {
	return Duration.of(amount, unit);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + amount;
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
	Reminder other = (Reminder) obj;
	if (amount != other.amount)
	    return false;
	if (unit != other.unit)
	    return false;
	return true;
    }

}
