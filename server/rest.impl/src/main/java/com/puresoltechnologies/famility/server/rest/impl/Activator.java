package com.puresoltechnologies.famility.server.rest.impl;

import java.io.File;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.famility.common.configuration.FamilityConfigurationService;
import com.puresoltechnologies.famility.common.plugins.AbstractFamilityPluginActivator;

public class Activator extends AbstractFamilityPluginActivator {

    private static final Logger logger = LoggerFactory.getLogger(Activator.class);

    private FamilityServer server;

    @Override
    public void start(BundleContext context) throws Exception {
	super.start(context);
	ServiceReference<FamilityConfigurationService> serverCconfiguratorReference = context
		.getServiceReference(FamilityConfigurationService.class);
	if (serverCconfiguratorReference != null) {
	    FamilityConfigurationService configuration = context.getService(serverCconfiguratorReference);
	    File configurationFile = new File(configuration.getConfigurationDirectory(), "server.yml");
	    logger.info("Configuration file: " + configurationFile);
	    server = new FamilityServer();
	    server.run("server", configurationFile.getPath());
	}
    }

    @Override
    public void stop(BundleContext context) throws Exception {
	super.stop(context);
	server = null;
    }

}
