package com.puresoltechnologies.famility.server.rest.impl.services;

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

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.famility.server.api.contacts.Contact;
import com.puresoltechnologies.famility.server.api.contacts.ContactEmailAddress;
import com.puresoltechnologies.famility.server.api.contacts.TypeDefinition;
import com.puresoltechnologies.famility.server.impl.contacts.ContactManagerImpl;
import com.puresoltechnologies.famility.server.rest.api.contacts.ContactsService;
import com.puresoltechnologies.famility.server.rest.api.contacts.JsonBirthday;
import com.puresoltechnologies.famility.server.rest.api.contacts.JsonContact;
import com.puresoltechnologies.famility.server.rest.api.contacts.JsonContactEmailAddress;

@Timed
@ExceptionMetered
@Path("/contacts")
public class ContactsServiceImpl implements ContactsService {

    private static final ContactManagerImpl contactManager = new ContactManagerImpl();

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<JsonContact> getContacts() throws SQLException {
	return RestConverter.convertContactsToRest(contactManager.getContacts());
    }

    @Override
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createContact(JsonContact contact) throws SQLException, URISyntaxException {
	Contact converted = RestConverter.convert(contact);
	contactManager.addContact(converted);
	String idString = String.valueOf(converted.getId());
	return Response.created(new URI("/contacts/" + idString)).entity(idString).build();
    }

    @Override
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonContact getContact(@PathParam("id") long id) throws SQLException {
	return RestConverter.convert(contactManager.getContact(id));
    }

    @Override
    @DELETE
    @Path("/{id}")
    public void deleteContact(@PathParam("id") String id) throws SQLException {
	contactManager.deleteContact(Long.parseLong(id));
    }

    @PUT
    @Path("/{id}/emails")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEmailAddress(@PathParam("id") long id, JsonContactEmailAddress email)
	    throws SQLException, URISyntaxException {
	EmailAddress emailAddress = email.getEmailAddress();
	contactManager.addEMailAddress(id, emailAddress, email.getTypeId());
	return Response.created(new URI("/contacts/" + id + "/email/" + emailAddress.getAddress()))
		.entity(emailAddress.getAddress()).build();

    }

    @GET
    @Path("/{id}/emails")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<JsonContactEmailAddress> addEmailAddress(@PathParam("id") long id) throws SQLException {
	Collection<ContactEmailAddress> eMailAddresses = contactManager.getEMailAddresses(id);
	return RestConverter.convertContactEmailAddressesToJson(eMailAddresses);
    }

    @Override
    @GET
    @Path("/birthdays")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<JsonBirthday> getBirthdays() throws SQLException {
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
