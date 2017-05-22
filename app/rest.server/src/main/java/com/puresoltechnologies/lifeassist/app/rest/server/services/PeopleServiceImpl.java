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

import com.puresoltechnologies.lifeassist.app.impl.people.PeopleManager;
import com.puresoltechnologies.lifeassist.app.rest.api.people.Birthday;
import com.puresoltechnologies.lifeassist.app.rest.api.people.PeopleService;
import com.puresoltechnologies.lifeassist.app.rest.api.people.Person;

@Path("/people")
public class PeopleServiceImpl implements PeopleService {

    private static final PeopleManager peopleManager = new PeopleManager();

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Person> getPeople() throws SQLException {
	return RestConverter.convertPeopleToRest(peopleManager.getPeople());
    }

    @Override
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(Person person) throws SQLException, URISyntaxException {
	peopleManager.addPerson(RestConverter.convert(person));
	return Response.created(new URI(person.getName())).build();
    }

    @Override
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPerson(@PathParam("id") long id) throws SQLException {
	return RestConverter.convert(peopleManager.getPerson(id));
    }

    @Override
    @DELETE
    @Path("/{id}")
    public void deletePerson(@PathParam("id") String id) throws SQLException {
	peopleManager.deletePerson(Long.parseLong(id));
    }

    @Override
    @GET
    @Path("/birthdays")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Birthday> getBirthdays() throws SQLException {
	return RestConverter.convertBirthdays(peopleManager.getBirthdays());
    }

}
