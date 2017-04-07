package com.puresoltechnologies.lifeassist.app.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.puresoltechnologies.lifeassist.app.rest.LifeAssistantApplication;
import com.puresoltechnologies.lifeassist.app.rest.config.LifeAssistantConfiguration;
import com.puresoltechnologies.lifeassist.common.utils.JsonSerializer;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public abstract class AbstractRestTest extends AbstractLifeAssistantTest {

    private static String configurationFile = ResourceHelpers.resourceFilePath("configuration.yml");

    /**
     * @see http://www.dropwizard.io/1.0.0/docs/manual/testing.html#integration-testing
     */
    @ClassRule
    public static final DropwizardAppRule<LifeAssistantConfiguration> RULE = new DropwizardAppRule<LifeAssistantConfiguration>(
	    LifeAssistantApplication.class, configurationFile);

    private static JerseyClient restClient = null;

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
	restClient = JerseyClientBuilder.createClient().register(LoggingFeature.class);
    }

    @AfterClass
    public static void shutdownRestClient() {
	restClient.close();
	restClient = null;
    }

    protected URI getBaseURI() throws URISyntaxException {
	return new URI("http://localhost:8080/rest");
    }

    public JerseyWebTarget getRestClient(String path) throws URISyntaxException {
	return restClient.target(getBaseURI()).path(basePath).path(path);
    }

    protected <T> T convertEntity(Response response, Class<T> clazz)
	    throws JsonParseException, JsonMappingException, IOException {
	return JsonSerializer.fromInputStream(response.readEntity(InputStream.class), clazz);
    }

    protected <T> MappingIterator<T> convertCollectionEntity(Response response, Class<T> clazz)
	    throws JsonParseException, JsonMappingException, IOException {
	return JsonSerializer.fromCollectionInputStream(response.readEntity(InputStream.class), clazz);
    }
}
