package com.puresoltechnologies.lifeassist.app.api.calendar;

/**
 * Specifies the dependency type of the calendar entry.
 * 
 * @author Rick-Rainer Ludwig
 */
public enum EntryDependency {

    RELATES_TO("relates to"), BLOCKS("blocks"), BLOCKED_BY("blocked by"), PRECEDES("precedes"), FOLLOWS("follows");

    private final String displayName;

    private EntryDependency(String displayName) {
	this.displayName = displayName;
    }

    public String getDisplayName() {
	return displayName;
    }

    public EntryDependency getReverseDependency() {
	switch (this) {
	case RELATES_TO:
	    return RELATES_TO;
	case BLOCKS:
	    return BLOCKED_BY;
	case BLOCKED_BY:
	    return BLOCKS;
	case PRECEDES:
	    return FOLLOWS;
	case FOLLOWS:
	    return PRECEDES;
	default:
	    throw new RuntimeException();
	}
    }

}
