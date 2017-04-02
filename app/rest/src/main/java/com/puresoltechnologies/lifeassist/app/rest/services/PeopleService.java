package com.puresoltechnologies.lifeassist.app.rest.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.puresoltechnologies.lifeassist.app.api.people.Birthday;
import com.puresoltechnologies.lifeassist.app.api.people.Person;
import com.puresoltechnologies.lifeassist.app.impl.people.PeopleManager;

@Path("/people")
public class PeopleService {

    private static final PeopleManager peopleManager = new PeopleManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getPeople() throws SQLException {
	return peopleManager.getPeople();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(Person person) throws SQLException, URISyntaxException {
	peopleManager.addPerson(person);
	return Response.created(new URI(person.getName())).build();
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPerson(@PathParam("name") String name) throws SQLException {
	return peopleManager.getPerson(name);
    }

    @DELETE
    @Path("/{id}")
    public void deletePerson(@PathParam("id") String id) throws SQLException {
	peopleManager.deletePerson(Long.parseLong(id));
    }

    @GET
    @Path("/birthdays")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Birthday> getBirthdays() throws SQLException {
	return peopleManager.getBirthdays();
    }

}
