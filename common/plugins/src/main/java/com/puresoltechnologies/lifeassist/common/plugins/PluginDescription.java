package com.puresoltechnologies.lifeassist.common.plugins;

public class PluginDescription {

    private final String name;
    private final String description;

    public PluginDescription(String name, String description) {
	super();
	this.name = name;
	this.description = description;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

}
