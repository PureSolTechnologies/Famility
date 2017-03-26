package com.puresoltechnologies.lifeassist.app.test;

import java.net.URI;

import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import com.puresoltechnologies.lifeassist.app.rest.LifeAssistantApplication;
import com.puresoltechnologies.lifeassist.app.rest.config.LifeAssistantConfiguration;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public abstract class AbstractRestTest {

    /**
     * @see http://www.dropwizard.io/1.0.0/docs/manual/testing.html#integration-testing
     */
    @ClassRule
    public static final DropwizardAppRule<LifeAssistantConfiguration> RULE = new DropwizardAppRule<LifeAssistantConfiguration>(
	    LifeAssistantApplication.class, ResourceHelpers.resourceFilePath("configuration.yml"));

    private static Client restClient = null;

    private final Class<?> serviceClass;
    private final String basePath;

    public AbstractRestTest(Class<?> serviceClass) {
	super();
	this.serviceClass = serviceClass;
	Path pathAnnotation = serviceClass.getAnnotation(Path.class);
	this.basePath = pathAnnotation.value();
    }

    @BeforeClass
    public static void initializeRestClient() {
	restClient = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));
    }

    @AfterClass
    public static void shutdownRestClient() {
	restClient.close();
	restClient = null;
    }

    protected abstract URI getBaseURI();

    public WebTarget getRestClient(String path) {
	return restClient.target(getBaseURI()).path(basePath).path(path);
    }
}
