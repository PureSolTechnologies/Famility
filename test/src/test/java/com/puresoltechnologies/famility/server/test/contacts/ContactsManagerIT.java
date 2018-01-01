package com.puresoltechnologies.famility.server.test.contacts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Test;

import com.puresoltechnologies.famility.server.api.contacts.Contact;
import com.puresoltechnologies.famility.server.impl.contacts.ContactManagerImpl;
import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;
import com.puresoltechnologies.famility.server.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.famility.server.model.calendar.QSeries;
import com.puresoltechnologies.famility.server.test.contacts.AbstractContactsManagerTest;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;

public class ContactsManagerIT extends AbstractContactsManagerTest {

    @Test
    public void testAppointmentCRUDwithBirthday() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertEquals(0, entrySeries.fetch().size());

	    ContactManagerImpl peopleManager = getPeopleManager();
	    Contact original = new Contact(1, "Rick-Rainer Ludwig", LocalDate.of(1978, 5, 16));
	    long id = peopleManager.addContact(original);
	    assertEquals(id, original.getId());
	    entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertNotNull(entrySeries.fetchOne());

	    Contact read = peopleManager.getContact(id);
	    assertEquals(original, read);

	    Contact updated = new Contact("Rick", LocalDate.of(1978, 5, 16));
	    assertFalse(peopleManager.updateContact(updated));
	    assertTrue(peopleManager.updateContact(id, updated));

	    Contact readUpdated = peopleManager.getContact(id);
	    assertEquals(updated, readUpdated);

	    assertTrue(peopleManager.deleteContact(id));

	    assertNull(peopleManager.getContact(id));
	    assertNull(entrySeries.fetchOne());

	    assertFalse(peopleManager.deleteContact(id));
	}
    }

    @Test
    public void testAppointmentCRUDwithoutBirthday() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertEquals(0, entrySeries.fetch().size());

	    ContactManagerImpl peopleManager = getPeopleManager();
	    Contact original = new Contact(1, "Rick-Rainer Ludwig", null);
	    long id = peopleManager.addContact(original);
	    assertEquals(id, original.getId());
	    entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertNull(entrySeries.fetchOne());

	    Contact read = peopleManager.getContact(id);
	    assertEquals(original, read);

	    Contact updated = new Contact("Rick", null);
	    assertFalse(peopleManager.updateContact(updated));
	    assertTrue(peopleManager.updateContact(id, updated));

	    Contact readUpdated = peopleManager.getContact(id);
	    assertEquals(updated, readUpdated);

	    assertTrue(peopleManager.deleteContact(id));

	    assertNull(peopleManager.getContact(id));
	    assertNull(entrySeries.fetchOne());

	    assertFalse(peopleManager.deleteContact(id));
	}
    }

    @Test
    public void testAddBirthday() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertEquals(0, entrySeries.fetch().size());

	    ContactManagerImpl peopleManager = getPeopleManager();
	    Contact original = new Contact(1, "Rick-Rainer Ludwig", null);
	    long id = peopleManager.addContact(original);
	    assertEquals(id, original.getId());
	    entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertNull(entrySeries.fetchOne());

	    Contact read = peopleManager.getContact(id);
	    assertEquals(original, read);

	    Contact updated = new Contact("Rick", LocalDate.of(1978, 5, 16));
	    assertTrue(peopleManager.updateContact(id, updated));

	    entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertNotNull(entrySeries.fetchOne());
	}
    }

    @Test
    public void testDeleteBirthday() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertEquals(0, entrySeries.fetch().size());

	    ContactManagerImpl peopleManager = getPeopleManager();
	    Contact original = new Contact(1, "Rick-Rainer Ludwig", LocalDate.of(1978, 5, 16));
	    long id = peopleManager.addContact(original);
	    assertEquals(id, original.getId());
	    entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertNotNull(entrySeries.fetchOne());

	    Contact read = peopleManager.getContact(id);
	    assertEquals(original, read);

	    Contact updated = new Contact("Rick", null);
	    assertTrue(peopleManager.updateContact(id, updated));

	    entrySeries = queryFactory.select(QSeries.series.all()).from(QSeries.series);
	    assertNull(entrySeries.fetchOne());
	}

    }

}
