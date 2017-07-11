package com.puresoltechnologies.lifeassist.app.rest.server.data;

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
