package com.puresoltechnologies.famility.server.test.configuration;

import java.util.ArrayList;
import java.util.List;

public class TestLoggingConfiguration {

    public static class Appender {

	private String type = "";
	private String threshold = "";
	private String target = "";

	public String getType() {
	    return type;
	}

	public void setType(String type) {
	    this.type = type;
	}

	public String getThreshold() {
	    return threshold;
	}

	public void setThreshold(String threshold) {
	    this.threshold = threshold;
	}

	public String getTarget() {
	    return target;
	}

	public void setTarget(String target) {
	    this.target = target;
	}

    }

    private String level = null;
    private List<Appender> appenders = new ArrayList<>();

    public String getLevel() {
	return level;
    }

    public void setLevel(String level) {
	this.level = level;
    }

    public List<Appender> getAppenders() {
	return appenders;
    }

    public void setAppenders(List<Appender> appenders) {
	this.appenders = appenders;
    }

}
