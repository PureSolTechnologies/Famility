package com.puresoltechnologies.lifeassist.app.impl.people;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.people.Person;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;

public class PeopleManager {

    public List<Person> getPeople() throws SQLException {
	List<Person> people = new ArrayList<>();
	try (Connection connection = DatabaseConnector.getConnection();
		Statement statement = connection.createStatement()) {
	    try (ResultSet resultSet = statement.executeQuery("SELECT name, birthday FROM users")) {
		while (resultSet.next()) {
		    String name = resultSet.getString("name");
		    LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
		    people.add(new Person(name, CalendarDay.of(birthday)));
		}
	    }
	}
	return people;
    }

    public void addPerson(Person person) throws SQLException {
	try (Connection connection = DatabaseConnector.getConnection();
		PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO users (id, name, birthday) VALUES (nextval('user_id_seq'), ?, ?)")) {
	    statement.setString(1, person.getName());
	    statement.setDate(2, Date.valueOf(CalendarDay.toLocalDate(person.getBirthday())));
	    statement.execute();
	    connection.commit();
	}
    }

    public Person getPerson(String name) {
	// TODO Auto-generated method stub
	return null;
    }
}
