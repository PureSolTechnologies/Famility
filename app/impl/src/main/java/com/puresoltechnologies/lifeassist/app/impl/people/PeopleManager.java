package com.puresoltechnologies.lifeassist.app.impl.people;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.people.Person;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;

public class PeopleManager {

    public List<Person> getPeople() throws SQLException {
	List<Person> people = new ArrayList<>();
	try (Connection connection = DatabaseConnector.getConnection();
		Statement statement = connection.createStatement()) {
	    try (ResultSet resultSet = statement.executeQuery("SELECT id, name, birthday FROM users")) {
		while (resultSet.next()) {
		    long id = resultSet.getLong("id");
		    String name = resultSet.getString("name");
		    LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
		    people.add(new Person(id, name, CalendarDay.of(birthday)));
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

    public void deletePerson(long id) throws SQLException {
	try (Connection connection = DatabaseConnector.getConnection();
		PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id=?")) {
	    statement.setLong(1, id);
	    statement.execute();
	    connection.commit();
	}
    }

    public List<Person> getBirthdays() throws SQLException {
	List<Person> people = getPeople();
	LocalDate today = LocalDate.now();
	Collections.sort(people, new Comparator<Person>() {

	    private boolean isThisYear(CalendarDay day) {
		if (day.getMonth() < today.getMonthValue()) {
		    return false;
		}
		if ((day.getMonth() == today.getDayOfMonth()) && (day.getDayOfMonth() < today.getDayOfMonth())) {
		    return false;
		}
		return true;
	    }

	    @Override
	    public int compare(Person o1, Person o2) {
		CalendarDay birthday1 = nextAnniversery(today, o1.getBirthday());
		CalendarDay birthday2 = nextAnniversery(today, o2.getBirthday());
		return birthday1.compareTo(birthday2);
	    }

	    private CalendarDay nextAnniversery(LocalDate today, CalendarDay day) {
		if (isThisYear(day)) {
		    return new CalendarDay(today.getYear(), day.getMonth(), day.getDayOfMonth());
		} else {
		    return new CalendarDay(today.getYear() + 1, day.getMonth(), day.getDayOfMonth());
		}
	    }
	});
	return people;
    }
}
