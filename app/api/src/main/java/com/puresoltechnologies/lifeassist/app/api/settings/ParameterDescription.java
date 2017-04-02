package com.puresoltechnologies.lifeassist.app.api.settings;

import com.puresoltechnologies.lifeassist.common.db.ParameterType;

public class ParameterDescription {

    private final String name;
    private final ParameterType type;
    private final String value;
    private final String defaultValue;
    private final String unit;
    private final String description;

    public ParameterDescription(String name, ParameterType type, String value, String defaultValue, String unit,
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

    public ParameterType getType() {
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
