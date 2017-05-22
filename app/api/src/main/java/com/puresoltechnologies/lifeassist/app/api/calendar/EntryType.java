package com.puresoltechnologies.lifeassist.app.api.calendar;

/**
 * Represents a single type of an calendar entry.
 * 
 * @author Rick-Rainer Ludwig
 */
public class EntryType {

    private final String type;
    private final String name;

    public EntryType(//
	    String type, //
	    String name //
    ) {
	super();
	this.type = type;
	this.name = name;
    }

    public String getType() {
	return type;
    }

    public String getName() {
	return name;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	EntryType other = (EntryType) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	return true;
    }

}
