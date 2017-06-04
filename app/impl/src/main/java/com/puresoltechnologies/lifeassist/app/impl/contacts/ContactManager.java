package com.puresoltechnologies.lifeassist.app.impl.contacts;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.lifeassist.app.api.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.api.calendar.Reminder;
import com.puresoltechnologies.lifeassist.app.api.calendar.Series;
import com.puresoltechnologies.lifeassist.app.api.calendar.Turnus;
import com.puresoltechnologies.lifeassist.app.api.contacts.Birthday;
import com.puresoltechnologies.lifeassist.app.api.contacts.Contact;
import com.puresoltechnologies.lifeassist.app.impl.calendar.CalendarManager;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.model.contacts.QContacts;
import com.puresoltechnologies.lifeassist.app.model.contacts.QEmailTypes;
import com.puresoltechnologies.lifeassist.app.model.contacts.QEmails;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;

public class ContactManager {

    CalendarManager calendarManager = new CalendarManager();
    private static Field personIdField;
    static {
	try {
	    personIdField = Contact.class.getDeclaredField("id");
	    personIdField.setAccessible(true);
	} catch (NoSuchFieldException | SecurityException e) {
	    throw new RuntimeException(e);
	}
    }

    public List<Contact> getContacts() throws SQLException {
	List<Contact> contacts = new ArrayList<>();
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QContacts.contacts.all()).from(QContacts.contacts);
	    try (ResultSet resultSet = select.getResults()) {
		while (resultSet.next()) {
		    long id = resultSet.getLong("id");
		    String name = resultSet.getString("name");
		    Date date = resultSet.getDate("birthday");
		    LocalDate birthday = date != null ? date.toLocalDate() : null;
		    contacts.add(new Contact(id, name, birthday));
		}
	    }
	}
	return contacts;
    }

    public long addContact(Contact person) throws SQLException {
	Long serieId = null;
	if (person.getBirthday() != null) {
	    Series entrySerie = createBirthdayEntrySerie(person);
	    serieId = calendarManager.insertSeries(entrySerie);
	}
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("contacts.person_id_seq"));
	    long id = query.fetchOne();

	    Date birthday = convertBirthdayToDate(person);
	    SQLInsertClause insert = queryFactory //
		    .insert(QContacts.contacts)//
		    .set(QContacts.contacts.id, id) //
		    .set(QContacts.contacts.name, person.getName()) //
		    .set(QContacts.contacts.birthday, birthday) //
		    .set(QContacts.contacts.birthdayCalendarSeriesId, serieId) //
	    ;
	    insert.execute();
	    personIdField.set(person, id);
	    queryFactory.commit();
	    return id;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    private Series createBirthdayEntrySerie(Contact person) {
	ZonedDateTime begin = ZonedDateTime.of(person.getBirthday(), LocalTime.of(0, 0), ZoneId.of("UTC"));
	Series entrySerie = new Series("birthday", person.getName() + "'s Birthday", "", new ArrayList<>(),
		new Reminder(3, ChronoUnit.DAYS), begin, null, 24, ChronoUnit.HOURS, OccupancyStatus.AVAILABLE,
		Turnus.YEARLY, 0);
	return entrySerie;
    }

    private Date convertBirthdayToDate(Contact person) {
	Date birthday = null;
	if (person.getBirthday() != null) {
	    birthday = Date.valueOf(person.getBirthday());
	}
	return birthday;
    }

    public boolean updateContact(Contact person) throws SQLException {
	return updateContact(person.getId(), person);
    }

    public boolean updateContact(long id, Contact person) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> oldSelect = queryFactory //
		    .select(QContacts.contacts.birthdayCalendarSeriesId, QContacts.contacts.birthday) //
		    .from(QContacts.contacts) //
		    .where(QContacts.contacts.id.eq(id));
	    Tuple oldEntry = oldSelect.fetchOne();
	    if (oldEntry == null) {
		return false;
	    }
	    Long entrySerieId = oldEntry.get(QContacts.contacts.birthdayCalendarSeriesId);
	    LocalDate oldBirthday = null;
	    Date date = oldEntry.get(QContacts.contacts.birthday);
	    if (date != null) {
		oldBirthday = date.toLocalDate();
	    }
	    boolean delete = false;
	    if (oldBirthday == null) {
		if (person.getBirthday() != null) {
		    // create birthday entry series
		    Series entrySerie = createBirthdayEntrySerie(person);
		    entrySerieId = calendarManager.insertSeries(entrySerie);
		}
	    } else {
		if (person.getBirthday() == null) {
		    // remove birthday entry series
		    delete = true;
		} else if (!oldBirthday.equals(person.getBirthday())) {
		    // change birthday entry series
		    calendarManager.removeSeries(entrySerieId);
		    Series entrySerie = createBirthdayEntrySerie(person);
		    entrySerieId = calendarManager.insertSeries(entrySerie);
		}
	    }

	    Date birthday = convertBirthdayToDate(person);
	    SQLUpdateClause update = queryFactory//
		    .update(QContacts.contacts)//
		    .set(QContacts.contacts.id, id) //
		    .set(QContacts.contacts.name, person.getName()) //
		    .set(QContacts.contacts.birthday, birthday) //
		    .set(QContacts.contacts.birthdayCalendarSeriesId, delete ? null : entrySerieId) //
		    .where(QContacts.contacts.id.eq(id));
	    long updated = update.execute();
	    queryFactory.commit();
	    personIdField.set(person, id);

	    if (delete) {
		calendarManager.removeSeries(entrySerieId);
	    }

	    return updated > 0;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public Contact getContact(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QContacts.contacts.all()).from(QContacts.contacts)
		    .where(QContacts.contacts.id.eq(id));
	    Tuple result = select.fetchOne();
	    if (result == null) {
		return null;
	    }
	    String name = result.get(QContacts.contacts.name);
	    Date date = result.get(QContacts.contacts.birthday);
	    return new Contact(id, name, date != null ? date.toLocalDate() : null);
	}
    }

    public boolean deleteContact(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> selectEntrySerie = queryFactory//
		    .select(QContacts.contacts.birthdayCalendarSeriesId) //
		    .from(QContacts.contacts) //
		    .where(QContacts.contacts.id.eq(id));
	    Long entrySerieId = selectEntrySerie.fetchOne();

	    SQLDeleteClause delete = queryFactory //
		    .delete(QContacts.contacts)//
		    .where(QContacts.contacts.id.eq(id));
	    long deleted = delete.execute();
	    queryFactory.commit();

	    if (entrySerieId != null) {
		calendarManager.removeSeries(entrySerieId);
	    }
	    return deleted > 0;
	}
    }

    public List<Birthday> getBirthdays() throws SQLException {
	List<Contact> contacts = getContacts();
	List<Birthday> birthdays = new ArrayList<>();
	contacts.forEach(person -> {
	    if (person.getBirthday() != null) {
		birthdays.add(Birthday.of(person));
	    }
	});
	Collections.sort(birthdays);
	return birthdays;
    }

    public long addEMailType(String name) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("contacts.email_type_id_seq"));
	    long id = query.fetchOne();
	    queryFactory.insert(QEmailTypes.emailTypes) //
		    .set(QEmailTypes.emailTypes.id, id) //
		    .set(QEmailTypes.emailTypes.name, name) //
		    .execute();
	    queryFactory.commit();
	    return id;
	}
    }

    public void addEMailAddress(long id, EmailAddress emailAddress, long emailTypeId) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    queryFactory.insert(QEmails.emails) //
		    .set(QEmails.emails.contactId, id) //
		    .set(QEmails.emails.address, emailAddress.getAddress()) //
		    .set(QEmails.emails.typeId, emailTypeId) //
		    .execute();
	    queryFactory.commit();
	}
    }
}
