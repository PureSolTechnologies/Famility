package com.puresoltechnologies.lifeassist.app.impl;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.resource.URLResource;

import com.puresoltechnologies.lifeassist.app.impl.rest.LoginServiceResource;
import com.puresoltechnologies.lifeassist.app.impl.rest.RestServiceResource;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LifeAssistantApplication extends Application<LifeAssistantRestServiceConfiguration> {

    @Override
    public String getName() {
	return LifeAssistantApplication.class.getSimpleName();
    }

    @Override
    public void run(LifeAssistantRestServiceConfiguration configuration, Environment environment) throws Exception {
	environment.jersey().setUrlPattern("/rest");
	// environment.jersey().register(UserInterfaceResource.class);
	environment.jersey().register(RestServiceResource.class);
	environment.jersey().register(LoginServiceResource.class);

	environment.servlets().addFilter("Cross-Origin-Filter", new CrossOriginFilter())
		.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	environment.servlets().setBaseResource(
		URLResource.newResource(LifeAssistantApplication.class.getResource("/LifeAssistantUI")));
    }

    @Override
    public void initialize(Bootstrap<LifeAssistantRestServiceConfiguration> bootstrap) {
	bootstrap.addBundle(new AssetsBundle("/LifeAssistantUI", "/", "/index.html"));
    }

    public static void main(String[] args) throws Exception {
	new LifeAssistantApplication().run(args);
    }

}
