package com.puresoltechnologies.lifeassist.app.impl.rest;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> addPerson(@PathParam("name") String name, @HeaderParam("birthday") String birthday)
	    throws SQLException {
	TemporalAccessor date = DateTimeFormatter.ISO_LOCAL_DATE.parse(birthday);
	return peopleManager.addPerson(new Person(name, CalendarDay.of(date)));
    }

}
