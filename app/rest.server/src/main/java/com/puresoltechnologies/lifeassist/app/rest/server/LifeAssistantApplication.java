package com.puresoltechnologies.lifeassist.app.rest.server;

import org.eclipse.jetty.util.resource.URLResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.rest.server.config.LifeAssistantConfiguration;
import com.puresoltechnologies.lifeassist.app.rest.server.filters.CORSFilter;
import com.puresoltechnologies.lifeassist.app.rest.server.filters.IllegalEmailAddressExceptionMapper;
import com.puresoltechnologies.lifeassist.app.rest.server.filters.SQLExceptionMapper;
import com.puresoltechnologies.lifeassist.app.rest.server.health.DatabaseHealthCheck;
import com.puresoltechnologies.lifeassist.app.rest.server.metrics.DatabaseConnectionPoolMetricsSet;
import com.puresoltechnologies.lifeassist.app.rest.server.services.CalendarServiceImpl;
import com.puresoltechnologies.lifeassist.app.rest.server.services.LoginServiceImpl;
import com.puresoltechnologies.lifeassist.app.rest.server.services.ContactsServiceImpl;
import com.puresoltechnologies.lifeassist.app.rest.server.services.PluginServiceImpl;
import com.puresoltechnologies.lifeassist.app.rest.server.services.SettingsServiceImpl;

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

	MetricRegistry metrics = environment.metrics();
	metrics.register("memory_usage", new MemoryUsageGaugeSet());
	metrics.register("garbage_collector", new GarbageCollectorMetricSet());
	metrics.register("jvm_attributes", new JvmAttributeGaugeSet());
	metrics.register("db.pool", new DatabaseConnectionPoolMetricsSet());

	JerseyEnvironment jersey = environment.jersey();
	jersey.setUrlPattern("/rest");
	jersey.register(new CORSFilter());
	jersey.register(new IllegalEmailAddressExceptionMapper());
	jersey.register(new SQLExceptionMapper());
	jersey.register(LoginServiceImpl.class);
	jersey.register(PluginServiceImpl.class);
	jersey.register(CalendarServiceImpl.class);
	jersey.register(ContactsServiceImpl.class);
	jersey.register(SettingsServiceImpl.class);

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
