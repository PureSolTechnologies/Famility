package com.puresoltechnologies.famility.server.rest.impl;

import org.eclipse.jetty.util.resource.URLResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;
import com.puresoltechnologies.famility.server.rest.impl.config.FamilityConfiguration;
import com.puresoltechnologies.famility.server.rest.impl.filters.AuthenticationAndAuthorizationFilter;
import com.puresoltechnologies.famility.server.rest.impl.filters.CORSFilter;
import com.puresoltechnologies.famility.server.rest.impl.filters.IllegalEmailAddressExceptionMapper;
import com.puresoltechnologies.famility.server.rest.impl.filters.SQLExceptionMapper;
import com.puresoltechnologies.famility.server.rest.impl.health.DatabaseHealthCheck;
import com.puresoltechnologies.famility.server.rest.impl.metrics.DatabaseConnectionPoolMetricsSet;
import com.puresoltechnologies.famility.server.rest.impl.services.AccountServiceImpl;
import com.puresoltechnologies.famility.server.rest.impl.services.AuthServiceImpl;
import com.puresoltechnologies.famility.server.rest.impl.services.CalendarServiceImpl;
import com.puresoltechnologies.famility.server.rest.impl.services.ContactsServiceImpl;
import com.puresoltechnologies.famility.server.rest.impl.services.DataService;
import com.puresoltechnologies.famility.server.rest.impl.services.FinanceService;
import com.puresoltechnologies.famility.server.rest.impl.services.PluginServiceImpl;
import com.puresoltechnologies.famility.server.rest.impl.services.SettingsServiceImpl;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FamilityServer extends Application<FamilityConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(FamilityServer.class);

    @Override
    public String getName() {
	return FamilityServer.class.getSimpleName();
    }

    @Override
    public void initialize(Bootstrap<FamilityConfiguration> bootstrap) {
	bootstrap.addBundle(new AssetsBundle("/FamilityUI", "/", "/index.html"));
    }

    @Override
    public void run(FamilityConfiguration configuration, Environment environment) throws Exception {
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
	jersey.register(AuthenticationAndAuthorizationFilter.class);
	jersey.register(new CORSFilter());
	jersey.register(new IllegalEmailAddressExceptionMapper());
	jersey.register(new SQLExceptionMapper());
	jersey.register(AuthServiceImpl.getInstance());
	jersey.register(AccountServiceImpl.class);
	jersey.register(PluginServiceImpl.class);
	jersey.register(CalendarServiceImpl.class);
	jersey.register(ContactsServiceImpl.class);
	jersey.register(SettingsServiceImpl.class);
	jersey.register(FinanceService.class);
	jersey.register(DataService.class);

	ServletEnvironment servlets = environment.servlets();
	servlets.setBaseResource(
		URLResource.newResource(FamilityServer.class.getResource("/FamilityUI")));
    }

    @Override
    protected void onFatalError() {
	logger.error("SEVERE ISSUE OCCURED. APPLICATION IS SHUTTING DOWN.");
	super.onFatalError();
    }

    public static void main(String[] args) {
	try {
	    FamilityServer application = new FamilityServer();
	    application.run(args);
	} catch (Throwable e) {
	    logger.error("SEVERE ISSUE OCCURED. APPLICATION IS SHUTTING DOWN.", e);
	    System.exit(1);
	}
    }

}
