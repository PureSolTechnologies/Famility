package com.puresoltechnologies.lifeassist.app.rest.api.people;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ws.rs.core.Response;

public interface PeopleService {

    public Collection<Person> getPeople() throws SQLException;

    public Response addPerson(Person person) throws SQLException, URISyntaxException;

    public Person getPerson(long id) throws SQLException;

    public void deletePerson(String id) throws SQLException;

    public Collection<Birthday> getBirthdays() throws SQLException;

}
