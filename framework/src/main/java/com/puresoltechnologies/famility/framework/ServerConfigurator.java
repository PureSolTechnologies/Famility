package com.puresoltechnologies.famility.framework;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class ServerConfigurator implements ManagedService {

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
	if (properties == null) {
	    // no configuration from configuration admin
	    // or old configuration has been deleted
	} else {
	    // apply configuration from config admin
	}
    }

}
