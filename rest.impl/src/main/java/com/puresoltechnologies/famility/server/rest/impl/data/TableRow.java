package com.puresoltechnologies.famility.server.rest.impl.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TableRow {

    private final List<Object> columns = new ArrayList<>();

    @JsonCreator
    public TableRow( //
	    @JsonProperty("columns") List<Object> columns //
    ) {
	super();
	this.columns.addAll(columns);
    }

    public List<Object> getColumns() {
	return columns;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((columns == null) ? 0 : columns.hashCode());
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
	TableRow other = (TableRow) obj;
	if (columns == null) {
	    if (other.columns != null)
		return false;
	} else if (!columns.equals(other.columns))
	    return false;
	return true;
    }

}
