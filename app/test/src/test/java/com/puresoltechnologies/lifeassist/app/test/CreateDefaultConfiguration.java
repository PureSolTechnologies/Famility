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
import com.puresoltechnologies.passwordstore.domain.PasswordActivationException;
import com.puresoltechnologies.passwordstore.domain.PasswordCreationException;

public class CreateDefaultConfiguration {

    public static void main(String[] args)
	    throws SQLException, PasswordCreationException, PasswordActivationException, IOException {
	DatabaseConfiguration configuration = new DatabaseConfiguration();
	configuration.setHost("localhost");
	configuration.setPort(5432);
	configuration.setDatabase("famility");
	configuration.setUser("famility");
	configuration.setPassword("TrustNo1");
	DatabaseConnector.initialize(configuration);
	ContactManager contactManager = new ContactManager();
	long id = contactManager.addContact(new Contact("admin", null));
	long emailTypeId = contactManager.addEMailType("preferred");
	EmailAddress email = new EmailAddress("admin@famility.com");
	contactManager.addEMailAddress(id, email, emailTypeId);
	AccountManager accountManager = new AccountManager();
	accountManager.createAccount(email, new Password("TrustNo1!"));
    }

}
