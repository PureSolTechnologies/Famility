package com.puresoltechnologies.lifeassist.app.rest.server.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.puresoltechnologies.lifeassist.app.api.contacts.TypeDefinition;
import com.puresoltechnologies.lifeassist.app.impl.contacts.ContactManager;
import com.puresoltechnologies.lifeassist.app.rest.api.contacts.Birthday;
import com.puresoltechnologies.lifeassist.app.rest.api.contacts.Contact;
import com.puresoltechnologies.lifeassist.app.rest.api.contacts.ContactsService;

@Path("/contacts")
public class ContactsServiceImpl implements ContactsService {

    private static final ContactManager contactManager = new ContactManager();

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Contact> getContacts() throws SQLException {
	return RestConverter.convertContactsToRest(contactManager.getContacts());
    }

    @Override
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createContact(Contact contact) throws SQLException, URISyntaxException {
	com.puresoltechnologies.lifeassist.app.api.contacts.Contact converted = RestConverter.convert(contact);
	contactManager.addContact(converted);
	String idString = String.valueOf(converted.getId());
	return Response.created(new URI("/contacts/" + idString)).entity(idString).build();
    }

    @Override
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Contact getContact(@PathParam("id") long id) throws SQLException {
	return RestConverter.convert(contactManager.getContact(id));
    }

    @Override
    @DELETE
    @Path("/{id}")
    public void deleteContact(@PathParam("id") String id) throws SQLException {
	contactManager.deleteContact(Long.parseLong(id));
    }

    @Override
    @GET
    @Path("/birthdays")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Birthday> getBirthdays() throws SQLException {
	return RestConverter.convertBirthdays(contactManager.getBirthdays());
    }

    @GET
    @Path("/email-address-types")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<TypeDefinition> getEmailAddressTypes() throws SQLException {
	return contactManager.getEmailAddressTypes();
    }

    @GET
    @Path("/phone-number-types")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<TypeDefinition> getPhoneNumberTypes() throws SQLException {
	return contactManager.getPhoneNumberTypes();
    }

    @GET
    @Path("/postal-address-types")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<TypeDefinition> getPostalAddressTypes() throws SQLException {
	return contactManager.getPostalAddressTypes();
    }

    @GET
    @Path("/bank-account-types")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<TypeDefinition> getBankAccountTypes() throws SQLException {
	return contactManager.getBankAccountTypes();
    }

    @GET
    @Path("/other-contact-types")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<TypeDefinition> getOtherContactTypes() throws SQLException {
	return contactManager.getOtherContactTypes();
    }

}
