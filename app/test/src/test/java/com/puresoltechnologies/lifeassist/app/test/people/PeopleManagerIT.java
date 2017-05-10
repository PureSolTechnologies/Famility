package com.puresoltechnologies.lifeassist.app.test.people;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.people.Person;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.impl.people.PeopleManager;
import com.puresoltechnologies.lifeassist.app.model.QCalendarSeries;
import com.puresoltechnologies.lifeassist.app.test.calendar.AbstractPeopleManagerTest;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;

public class PeopleManagerIT extends AbstractPeopleManagerTest {

    @Test
    public void testAppointmentCRUDwithBirthday() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertEquals(0, entrySeries.fetch().size());

	    PeopleManager peopleManager = getPeopleManager();
	    Person original = new Person(1, "Rick-Rainer Ludwig", new CalendarDay(1978, 5, 16));
	    long id = peopleManager.addPerson(original);
	    assertEquals(id, original.getId());
	    entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertNotNull(entrySeries.fetchOne());

	    Person read = peopleManager.getPerson(id);
	    assertEquals(original, read);

	    Person updated = new Person("Rick", new CalendarDay(1978, 5, 16));
	    assertFalse(peopleManager.updatePerson(updated));
	    assertTrue(peopleManager.updatePerson(id, updated));

	    Person readUpdated = peopleManager.getPerson(id);
	    assertEquals(updated, readUpdated);

	    assertTrue(peopleManager.deletePerson(id));

	    assertNull(peopleManager.getPerson(id));
	    assertNull(entrySeries.fetchOne());

	    assertFalse(peopleManager.deletePerson(id));
	}
    }

    @Test
    public void testAppointmentCRUDwithoutBirthday() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertEquals(0, entrySeries.fetch().size());

	    PeopleManager peopleManager = getPeopleManager();
	    Person original = new Person(1, "Rick-Rainer Ludwig", null);
	    long id = peopleManager.addPerson(original);
	    assertEquals(id, original.getId());
	    entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertNull(entrySeries.fetchOne());

	    Person read = peopleManager.getPerson(id);
	    assertEquals(original, read);

	    Person updated = new Person("Rick", null);
	    assertFalse(peopleManager.updatePerson(updated));
	    assertTrue(peopleManager.updatePerson(id, updated));

	    Person readUpdated = peopleManager.getPerson(id);
	    assertEquals(updated, readUpdated);

	    assertTrue(peopleManager.deletePerson(id));

	    assertNull(peopleManager.getPerson(id));
	    assertNull(entrySeries.fetchOne());

	    assertFalse(peopleManager.deletePerson(id));
	}
    }

    @Test
    public void testAddBirthday() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertEquals(0, entrySeries.fetch().size());

	    PeopleManager peopleManager = getPeopleManager();
	    Person original = new Person(1, "Rick-Rainer Ludwig", null);
	    long id = peopleManager.addPerson(original);
	    assertEquals(id, original.getId());
	    entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertNull(entrySeries.fetchOne());

	    Person read = peopleManager.getPerson(id);
	    assertEquals(original, read);

	    Person updated = new Person("Rick", new CalendarDay(1978, 5, 16));
	    assertTrue(peopleManager.updatePerson(id, updated));

	    entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertNotNull(entrySeries.fetchOne());
	}
    }

    @Test
    public void testDeleteBirthday() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertEquals(0, entrySeries.fetch().size());

	    PeopleManager peopleManager = getPeopleManager();
	    Person original = new Person(1, "Rick-Rainer Ludwig", new CalendarDay(1978, 5, 16));
	    long id = peopleManager.addPerson(original);
	    assertEquals(id, original.getId());
	    entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertNotNull(entrySeries.fetchOne());

	    Person read = peopleManager.getPerson(id);
	    assertEquals(original, read);

	    Person updated = new Person("Rick", null);
	    assertTrue(peopleManager.updatePerson(id, updated));

	    entrySeries = queryFactory.select(QCalendarSeries.calendarSeries.all())
		    .from(QCalendarSeries.calendarSeries);
	    assertNull(entrySeries.fetchOne());
	}

    }

}
