package com.puresoltechnologies.famility.server.rest.api.services;

public class ParameterDescription {

    private final String name;
    private final String type;
    private final String value;
    private final String defaultValue;
    private final String unit;
    private final String description;

    public ParameterDescription(String name, String type, String value, String defaultValue, String unit,
	    String description) {
	super();
	this.name = name;
	this.type = type;
	this.value = value;
	this.defaultValue = defaultValue;
	this.unit = unit;
	this.description = description;
    }

    public String getName() {
	return name;
    }

    public String getType() {
	return type;
    }

    public String getValue() {
	return value;
    }

    public String getDefaultValue() {
	return defaultValue;
    }

    public String getUnit() {
	return unit;
    }

    public String getDescription() {
	return description;
    }

}
