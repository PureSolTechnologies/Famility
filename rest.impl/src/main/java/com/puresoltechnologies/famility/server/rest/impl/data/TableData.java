package com.puresoltechnologies.famility.server.rest.impl.data;

public class TableData {

    private final String name;
    private final TableDefinition tableDefinition;

    public TableData(String name, TableDefinition tableDefinition) {
	super();
	this.name = name;
	this.tableDefinition = tableDefinition;
    }

    public String getName() {
	return name;
    }

    public TableDefinition getTableDefinition() {
	return tableDefinition;
    }

}
