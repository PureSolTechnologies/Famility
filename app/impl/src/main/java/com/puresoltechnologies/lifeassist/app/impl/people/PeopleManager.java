package com.puresoltechnologies.lifeassist.app.impl.people;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.api.people.Birthday;
import com.puresoltechnologies.lifeassist.app.api.people.Person;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.impl.calendar.EntrySerie;
import com.puresoltechnologies.lifeassist.app.impl.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Reminder;
import com.puresoltechnologies.lifeassist.app.impl.calendar.Turnus;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.model.QPeople;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;

public class PeopleManager {

    CalendarManager calendarManager = new CalendarManager();
    private static Field personIdField;
    static {
	try {
	    personIdField = Person.class.getDeclaredField("id");
	    personIdField.setAccessible(true);
	} catch (NoSuchFieldException | SecurityException e) {
	    throw new RuntimeException(e);
	}
    }

    public List<Person> getPeople() throws SQLException {
	List<Person> people = new ArrayList<>();
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QPeople.people.all()).from(QPeople.people);
	    try (ResultSet resultSet = select.getResults()) {
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

    public long addPerson(Person person) throws SQLException {
	Long serieId = null;
	if (person.getBirthday() != null) {
	    EntrySerie entrySerie = createBirthdayEntrySerie(person);
	    serieId = calendarManager.insertEntrySerie(entrySerie);
	}
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("person_id_seq"));
	    long id = query.fetchOne();

	    Date birthday = convertBirthdayToDate(person);
	    SQLInsertClause insert = queryFactory //
		    .insert(QPeople.people)//
		    .set(QPeople.people.id, id) //
		    .set(QPeople.people.name, person.getName()) //
		    .set(QPeople.people.birthday, birthday) //
		    .set(QPeople.people.birthdayCalendarSeriesId, serieId) //
	    ;
	    insert.execute();
	    personIdField.set(person, id);
	    queryFactory.commit();
	    return id;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    private EntrySerie createBirthdayEntrySerie(Person person) {
	EntrySerie entrySerie = new EntrySerie("birthday", person.getName() + "'s Birthday", "", new ArrayList<>(),
		true, new Reminder(3, ChronoUnit.DAYS), person.getBirthday(), null, "UTC", new CalendarTime(0, 0, 0),
		24, ChronoUnit.HOURS, OccupancyStatus.AVAILABLE, Turnus.YEARLY, 0);
	return entrySerie;
    }

    private Date convertBirthdayToDate(Person person) {
	Date birthday = null;
	if (person.getBirthday() != null) {
	    birthday = Date.valueOf(CalendarDay.toLocalDate(person.getBirthday()));
	}
	return birthday;
    }

    public boolean updatePerson(Person person) throws SQLException {
	return updatePerson(person.getId(), person);
    }

    public boolean updatePerson(long id, Person person) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> oldSelect = queryFactory //
		    .select(QPeople.people.birthdayCalendarSeriesId, QPeople.people.birthday) //
		    .from(QPeople.people) //
		    .where(QPeople.people.id.eq(id));
	    Tuple oldEntry = oldSelect.fetchOne();
	    if (oldEntry == null) {
		return false;
	    }
	    Long entrySerieId = oldEntry.get(QPeople.people.birthdayCalendarSeriesId);
	    CalendarDay oldBirthday = null;
	    Date date = oldEntry.get(QPeople.people.birthday);
	    if (date != null) {
		oldBirthday = CalendarDay.of(date.toLocalDate());
	    }
	    boolean delete = false;
	    if (oldBirthday == null) {
		if (person.getBirthday() != null) {
		    // create birthday entry series
		    EntrySerie entrySerie = createBirthdayEntrySerie(person);
		    entrySerieId = calendarManager.insertEntrySerie(entrySerie);
		}
	    } else {
		if (person.getBirthday() == null) {
		    // remove birthday entry series
		    delete = true;
		} else if (!oldBirthday.equals(person.getBirthday())) {
		    // change birthday entry series
		    calendarManager.removeEntrySerie(entrySerieId);
		    EntrySerie entrySerie = createBirthdayEntrySerie(person);
		    entrySerieId = calendarManager.insertEntrySerie(entrySerie);
		}
	    }

	    Date birthday = convertBirthdayToDate(person);
	    SQLUpdateClause update = queryFactory//
		    .update(QPeople.people)//
		    .set(QPeople.people.id, id) //
		    .set(QPeople.people.name, person.getName()) //
		    .set(QPeople.people.birthday, birthday) //
		    .set(QPeople.people.birthdayCalendarSeriesId, delete ? null : entrySerieId) //
		    .where(QPeople.people.id.eq(id));
	    long updated = update.execute();
	    queryFactory.commit();
	    personIdField.set(person, id);

	    if (delete) {
		calendarManager.removeEntrySerie(entrySerieId);
	    }

	    return updated > 0;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public Person getPerson(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QPeople.people.all()).from(QPeople.people)
		    .where(QPeople.people.id.eq(id));
	    Tuple result = select.fetchOne();
	    if (result == null) {
		return null;
	    }
	    String name = result.get(QPeople.people.name);
	    Date date = result.get(QPeople.people.birthday);
	    CalendarDay birthday = null;
	    if (date != null) {
		birthday = CalendarDay.of(date.toLocalDate());
	    }
	    return new Person(id, name, birthday);
	}
    }

    public boolean deletePerson(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> selectEntrySerie = queryFactory//
		    .select(QPeople.people.birthdayCalendarSeriesId) //
		    .from(QPeople.people) //
		    .where(QPeople.people.id.eq(id));
	    Long entrySerieId = selectEntrySerie.fetchOne();

	    SQLDeleteClause delete = queryFactory //
		    .delete(QPeople.people)//
		    .where(QPeople.people.id.eq(id));
	    long deleted = delete.execute();
	    queryFactory.commit();

	    if (entrySerieId != null) {
		calendarManager.removeEntrySerie(entrySerieId);
	    }
	    return deleted > 0;
	}
    }

    public List<Birthday> getBirthdays() throws SQLException {
	List<Person> people = getPeople();
	List<Birthday> birthdays = new ArrayList<>();
	people.forEach(person -> birthdays.add(Birthday.of(person)));
	Collections.sort(birthdays);
	return birthdays;
    }
}
