package com.puresoltechnologies.famility.server.rest.api.contacts;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ws.rs.core.Response;

public interface ContactsService {

    public Collection<JsonContact> getContacts() throws SQLException;

    public Response createContact(JsonContact person) throws SQLException, URISyntaxException;

    public JsonContact getContact(long id) throws SQLException;

    public void deleteContact(String id) throws SQLException;

    public Collection<JsonBirthday> getBirthdays() throws SQLException;

}
