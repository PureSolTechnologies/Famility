package com.puresoltechnologies.lifeassist.app.rest;

import org.eclipse.jetty.util.resource.URLResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.rest.config.LifeAssistantConfiguration;
import com.puresoltechnologies.lifeassist.app.rest.health.DatabaseHealthCheck;
import com.puresoltechnologies.lifeassist.app.rest.health.filters.CORSFilter;
import com.puresoltechnologies.lifeassist.app.rest.health.filters.IllegalEmailAddressExceptionMapper;
import com.puresoltechnologies.lifeassist.app.rest.health.filters.SQLExceptionMapper;
import com.puresoltechnologies.lifeassist.app.rest.services.CalendarService;
import com.puresoltechnologies.lifeassist.app.rest.services.LoginService;
import com.puresoltechnologies.lifeassist.app.rest.services.PeopleService;
import com.puresoltechnologies.lifeassist.app.rest.services.PluginService;
import com.puresoltechnologies.lifeassist.app.rest.services.SettingsService;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LifeAssistantApplication extends Application<LifeAssistantConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(LifeAssistantApplication.class);

    @Override
    public String getName() {
	return LifeAssistantApplication.class.getSimpleName();
    }

    @Override
    public void initialize(Bootstrap<LifeAssistantConfiguration> bootstrap) {
	bootstrap.addBundle(new AssetsBundle("/LifeAssistantUI", "/", "/index.html"));
    }

    @Override
    public void run(LifeAssistantConfiguration configuration, Environment environment) throws Exception {
	DatabaseConnector.initialize(configuration.getDatabase());

	HealthCheckRegistry healthChecks = environment.healthChecks();
	healthChecks.register("database", new DatabaseHealthCheck());

	JerseyEnvironment jersey = environment.jersey();
	jersey.setUrlPattern("/rest");
	jersey.register(new CORSFilter());
	jersey.register(new IllegalEmailAddressExceptionMapper());
	jersey.register(new SQLExceptionMapper());
	jersey.register(LoginService.class);
	jersey.register(PluginService.class);
	jersey.register(CalendarService.class);
	jersey.register(PeopleService.class);
	jersey.register(SettingsService.class);

	ServletEnvironment servlets = environment.servlets();
	servlets.setBaseResource(
		URLResource.newResource(LifeAssistantApplication.class.getResource("/LifeAssistantUI")));
    }

    @Override
    protected void onFatalError() {
	logger.error("SEVERE ISSUE OCCURED. APPLICATION IS SHUTTING DOWN.");
	super.onFatalError();
    }

    public static void main(String[] args) {
	try {
	    LifeAssistantApplication application = new LifeAssistantApplication();
	    application.run(args);
	} catch (Throwable e) {
	    logger.error("SEVERE ISSUE OCCURED. APPLICATION IS SHUTTING DOWN.", e);
	    System.exit(1);
	}
    }

}
