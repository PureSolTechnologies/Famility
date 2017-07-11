package com.puresoltechnologies.lifeassist.app.rest.server.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnDefinition {

    private final String name;
    private final ColumnType type;

    @JsonCreator
    public ColumnDefinition( //
	    @JsonProperty("name") String name, //
	    @JsonProperty("type") ColumnType type //
    ) {
	this.name = name;
	this.type = type;
    }

    public String getName() {
	return name;
    }

    public ColumnType getType() {
	return type;
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
	ColumnDefinition other = (ColumnDefinition) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (type != other.type)
	    return false;
	return true;
    }

}
