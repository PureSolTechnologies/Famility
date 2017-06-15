package com.puresoltechnologies.lifeassist.app.rest.api.contacts;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ws.rs.core.Response;

public interface ContactsService {

    public Collection<Contact> getContacts() throws SQLException;

    public Response createContact(Contact person) throws SQLException, URISyntaxException;

    public Contact getContact(long id) throws SQLException;

    public void deleteContact(String id) throws SQLException;

    public Collection<Birthday> getBirthdays() throws SQLException;

}
