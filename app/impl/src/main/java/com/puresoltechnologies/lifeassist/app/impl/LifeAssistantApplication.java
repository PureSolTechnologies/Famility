package com.puresoltechnologies.lifeassist.app.impl;

import org.eclipse.jetty.util.resource.URLResource;

import com.puresoltechnologies.lifeassist.app.impl.filters.CORSFilter;
import com.puresoltechnologies.lifeassist.app.impl.filters.IllegalEmailAddressExceptionMapper;
import com.puresoltechnologies.lifeassist.app.impl.filters.SQLExceptionMapper;
import com.puresoltechnologies.lifeassist.app.impl.rest.CalendarServiceResource;
import com.puresoltechnologies.lifeassist.app.impl.rest.LoginServiceResource;
import com.puresoltechnologies.lifeassist.app.impl.rest.PeopleServiceResource;
import com.puresoltechnologies.lifeassist.app.impl.rest.PluginServiceResource;
import com.puresoltechnologies.lifeassist.app.impl.rest.SettingsServiceResource;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LifeAssistantApplication extends Application<LifeAssistantRestServiceConfiguration> {

    @Override
    public String getName() {
	return LifeAssistantApplication.class.getSimpleName();
    }

    @Override
    public void initialize(Bootstrap<LifeAssistantRestServiceConfiguration> bootstrap) {
	bootstrap.addBundle(new AssetsBundle("/LifeAssistantUI", "/", "/index.html"));
    }

    @Override
    public void run(LifeAssistantRestServiceConfiguration configuration, Environment environment) throws Exception {
	JerseyEnvironment jersey = environment.jersey();
	jersey.setUrlPattern("/rest");
	jersey.register(new CORSFilter());
	jersey.register(new IllegalEmailAddressExceptionMapper());
	jersey.register(new SQLExceptionMapper());
	jersey.register(LoginServiceResource.class);
	jersey.register(PluginServiceResource.class);
	jersey.register(CalendarServiceResource.class);
	jersey.register(PeopleServiceResource.class);
	jersey.register(SettingsServiceResource.class);

	ServletEnvironment servlets = environment.servlets();
	servlets.setBaseResource(
		URLResource.newResource(LifeAssistantApplication.class.getResource("/LifeAssistantUI")));
    }

    public static void main(String[] args) throws Exception {
	new LifeAssistantApplication().run(args);
    }

}
