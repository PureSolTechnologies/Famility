package com.puresoltechnologies.famility.server.rest.impl.data;

import java.util.ArrayList;
import java.util.List;

public class TableDefinition {

    private final String name;
    private final List<ColumnDefinition> columnDefinitions = new ArrayList<>();

    public TableDefinition(String name, List<ColumnDefinition> columnDefinitions) {
	super();
	this.name = name;
	this.columnDefinitions.addAll(columnDefinitions);
    }

    public String getName() {
	return name;
    }

    public List<ColumnDefinition> getColumnDefinitions() {
	return columnDefinitions;
    }

}
