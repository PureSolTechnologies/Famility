package com.puresoltechnologies.famility.server.rest.impl;

import java.io.File;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.famility.common.plugins.AbstractFamilityPluginActivator;

public class Activator extends AbstractFamilityPluginActivator {

    private static final Logger logger = LoggerFactory.getLogger(Activator.class);

    private FamilityServer server;

    @Override
    public void start(BundleContext context) throws Exception {
	super.start(context);
	/*
	 * We need to da a workaround to get a valid classloader for Dropwizard. see:
	 * https://stackoverflow.com/questions/2198928/better-handling-of-thread-context
	 * -classloader-in-osgi
	 */
	Thread currentThread = Thread.currentThread();
	ClassLoader oldThreadClassLoader = currentThread.getContextClassLoader();
	try {
	    currentThread.setContextClassLoader(getClass().getClassLoader());
	    /*
	     * Start threads, or establish connections, here, now
	     */
	    String familityHome = System.getProperty("famility.home");
	    if (familityHome != null) {
		File configurationFile = new File(familityHome, "etc/server.yml");
		logger.info("Configuration file: " + configurationFile);
		server = new FamilityServer();
		server.run("server", configurationFile.getPath());
	    } else {
		logger.error("famility.home system property was not set and no configuration can be found.");
	    }
	} finally {
	    /*
	     * Set the old class loader back to not break OSGi.
	     */
	    currentThread.setContextClassLoader(oldThreadClassLoader);
	}
    }

    @Override
    public void stop(BundleContext context) throws Exception {
	server = null;
	super.stop(context);
    }

}
