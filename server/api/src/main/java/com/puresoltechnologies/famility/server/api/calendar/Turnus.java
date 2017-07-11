package com.puresoltechnologies.famility.server.api.calendar;

import java.time.temporal.ChronoUnit;

public enum Turnus {

    DAILY(1, ChronoUnit.DAYS), //
    WEEKLY(1, ChronoUnit.WEEKS), //
    MONTHLY(1, ChronoUnit.MONTHS), //
    QUARTERLY(3, ChronoUnit.MONTHS), //
    YEARLY(1, ChronoUnit.YEARS);

    private final int amout;
    private final ChronoUnit unit;

    private Turnus(int amout, ChronoUnit unit) {
	this.amout = amout;
	this.unit = unit;
    }

    public int getAmout() {
	return amout;
    }

    public ChronoUnit getUnit() {
	return unit;
    }

}
