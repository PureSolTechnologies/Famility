package com.puresoltechnologies.lifeassist.app.test;

import java.io.IOException;
import java.sql.SQLException;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.commons.types.Password;
import com.puresoltechnologies.lifeassist.app.api.contacts.Contact;
import com.puresoltechnologies.lifeassist.app.impl.accounts.AccountManager;
import com.puresoltechnologies.lifeassist.app.impl.contacts.ContactManager;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConfiguration;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.model.contacts.QBankAccountTypes;
import com.puresoltechnologies.lifeassist.app.model.contacts.QEmailAdressTypes;
import com.puresoltechnologies.lifeassist.app.model.contacts.QOtherContactTypes;
import com.puresoltechnologies.lifeassist.app.model.contacts.QPhoneNumberTypes;
import com.puresoltechnologies.lifeassist.app.model.contacts.QPostalAddressTypes;
import com.puresoltechnologies.passwordstore.domain.PasswordActivationException;
import com.puresoltechnologies.passwordstore.domain.PasswordCreationException;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;

public class CreateDefaultConfiguration {

    private static void initializeDatabase() {
	DatabaseConfiguration configuration = new DatabaseConfiguration();
	configuration.setHost("localhost");
	configuration.setPort(5432);
	configuration.setDatabase("famility");
	configuration.setUser("famility");
	configuration.setPassword("TrustNo1");
	DatabaseConnector.initialize(configuration);
    }

    private static void createDefaultAdminAccount()
	    throws SQLException, PasswordCreationException, PasswordActivationException {
	ContactManager contactManager = new ContactManager();
	long id = contactManager.addContact(new Contact("admin", null));
	long emailTypeId = contactManager.addEMailType("preferred");
	EmailAddress email = new EmailAddress("admin@famility.com");
	contactManager.addEMailAddress(id, email, emailTypeId);
	AccountManager accountManager = new AccountManager();
	accountManager.createAccount(email, new Password("TrustNo1!"));
    }

    private static void createDefaultEmailAddressTypes() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("contacts.email_address_type_id_seq"));
	    long id = query.fetchOne();
	    queryFactory.insert(QEmailAdressTypes.emailAdressTypes) //
		    .set(QEmailAdressTypes.emailAdressTypes.id, id) //
		    .set(QEmailAdressTypes.emailAdressTypes.name, "Other") //
		    .execute();

	    queryFactory.commit();
	}
    }

    private static void createDefaultBankAccountTypes() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("contacts.bank_account_type_id_seq"));
	    long id = query.fetchOne();
	    queryFactory.insert(QBankAccountTypes.bankAccountTypes) //
		    .set(QBankAccountTypes.bankAccountTypes.id, id) //
		    .set(QBankAccountTypes.bankAccountTypes.name, "Preferred") //
		    .execute();

	    query = queryFactory.select(SQLExpressions.nextval("contacts.bank_account_type_id_seq"));
	    id = query.fetchOne();
	    queryFactory.insert(QBankAccountTypes.bankAccountTypes) //
		    .set(QBankAccountTypes.bankAccountTypes.id, id) //
		    .set(QBankAccountTypes.bankAccountTypes.name, "Other") //
		    .execute();

	    queryFactory.commit();
	}
    }

    private static void createDefaultPhoneNumberTypes() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("contacts.phone_number_type_id_seq"));
	    long id = query.fetchOne();
	    queryFactory.insert(QPhoneNumberTypes.phoneNumberTypes) //
		    .set(QPhoneNumberTypes.phoneNumberTypes.id, id) //
		    .set(QPhoneNumberTypes.phoneNumberTypes.name, "Landline Home") //
		    .execute();

	    id = query.fetchOne();
	    queryFactory.insert(QPhoneNumberTypes.phoneNumberTypes) //
		    .set(QPhoneNumberTypes.phoneNumberTypes.id, id) //
		    .set(QPhoneNumberTypes.phoneNumberTypes.name, "Landline Work") //
		    .execute();

	    id = query.fetchOne();
	    queryFactory.insert(QPhoneNumberTypes.phoneNumberTypes) //
		    .set(QPhoneNumberTypes.phoneNumberTypes.id, id) //
		    .set(QPhoneNumberTypes.phoneNumberTypes.name, "Mobile Home") //
		    .execute();

	    id = query.fetchOne();
	    queryFactory.insert(QPhoneNumberTypes.phoneNumberTypes) //
		    .set(QPhoneNumberTypes.phoneNumberTypes.id, id) //
		    .set(QPhoneNumberTypes.phoneNumberTypes.name, "Mobile Work") //
		    .execute();

	    queryFactory.commit();
	}
    }

    private static void createDefaultPostalAddressTypes() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("contacts.postal_address_type_id_seq"));
	    long id = query.fetchOne();
	    queryFactory.insert(QPostalAddressTypes.postalAddressTypes) //
		    .set(QPostalAddressTypes.postalAddressTypes.id, id) //
		    .set(QPostalAddressTypes.postalAddressTypes.name, "Home") //
		    .execute();

	    id = query.fetchOne();
	    queryFactory.insert(QPostalAddressTypes.postalAddressTypes) //
		    .set(QPostalAddressTypes.postalAddressTypes.id, id) //
		    .set(QPostalAddressTypes.postalAddressTypes.name, "Work") //
		    .execute();

	    queryFactory.commit();
	}
    }

    private static void createDefaultOtherContactTypes() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("contacts.other_contact_type_id_seq"));
	    long id = query.fetchOne();
	    queryFactory.insert(QOtherContactTypes.otherContactTypes) //
		    .set(QOtherContactTypes.otherContactTypes.id, id) //
		    .set(QOtherContactTypes.otherContactTypes.name, "LinkedIn") //
		    .execute();

	    id = query.fetchOne();
	    queryFactory.insert(QOtherContactTypes.otherContactTypes) //
		    .set(QOtherContactTypes.otherContactTypes.id, id) //
		    .set(QOtherContactTypes.otherContactTypes.name, "Xing") //
		    .execute();

	    id = query.fetchOne();
	    queryFactory.insert(QOtherContactTypes.otherContactTypes) //
		    .set(QOtherContactTypes.otherContactTypes.id, id) //
		    .set(QOtherContactTypes.otherContactTypes.name, "Skype") //
		    .execute();

	    queryFactory.commit();
	}
    }

    public static void main(String[] args)
	    throws SQLException, PasswordCreationException, PasswordActivationException, IOException {
	initializeDatabase();
	createDefaultAdminAccount();
	createDefaultEmailAddressTypes();
	createDefaultBankAccountTypes();
	createDefaultPhoneNumberTypes();
	createDefaultPostalAddressTypes();
	createDefaultOtherContactTypes();
    }
}
