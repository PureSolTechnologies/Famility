package com.puresoltechnologies.lifeassist.app.impl.rest;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.people.Person;
import com.puresoltechnologies.lifeassist.app.impl.people.PeopleManager;

@Path("/people")
public class PeopleServiceResource {

    private static final PeopleManager peopleManager = new PeopleManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getPeople() throws SQLException {
	return peopleManager.getPeople();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void addPerson(RestPerson person) throws SQLException {
	TemporalAccessor date = DateTimeFormatter.ISO_LOCAL_DATE.parse(person.getBirthday());
	peopleManager.addPerson(new Person(-1, person.getName(), CalendarDay.of(date)));
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
    public List<Person> getBirthdays() throws SQLException {
	return peopleManager.getBirthdays();
    }

}
