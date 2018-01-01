package com.puresoltechnologies.famility.server.test.contacts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.puresoltechnologies.famility.server.rest.api.calendar.CalendarDay;
import com.puresoltechnologies.famility.server.rest.api.contacts.JsonContact;
import com.puresoltechnologies.famility.server.test.contacts.AbstractContactsServiceTest;

public class ContactsServiceIT extends AbstractContactsServiceTest {

    @Before
    public void initialize() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
	JerseyWebTarget client = getRestClient("");
	Response response = client.request().get();
	assertEquals(200, response.getStatus());
	MappingIterator<JsonContact> collection = convertCollectionEntity(response, JsonContact.class);
	while (collection.hasNext()) {
	    JerseyWebTarget client2 = getRestClient(String.valueOf(collection.next().getId()));
	    client2.request().delete();
	}
    }

    @Test
    public void testEmpty() throws URISyntaxException, IOException {
	JerseyWebTarget client = getRestClient("");
	Response response = client.request().get();
	assertEquals(200, response.getStatus());
	MappingIterator<JsonContact> collection = convertCollectionEntity(response, JsonContact.class);
	assertFalse(collection.hasNext());
    }

    @Test
    public void testAddContact() throws URISyntaxException, IOException {
	JerseyWebTarget client = getRestClient("");
	Entity<JsonContact> entity = Entity.entity(new JsonContact(-1, "Rick", new CalendarDay(2014, 9, 18)),
		MediaType.APPLICATION_JSON_TYPE);
	Response response = client.request().put(entity);
	assertEquals(201, response.getStatus());
	response = client.request().get();
	assertEquals(200, response.getStatus());
	MappingIterator<JsonContact> collection = convertCollectionEntity(response, JsonContact.class);
	assertTrue(collection.hasNext());
    }

}
