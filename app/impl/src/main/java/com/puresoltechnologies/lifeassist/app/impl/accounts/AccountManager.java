package com.puresoltechnologies.lifeassist.app.impl.accounts;

import java.sql.SQLException;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.commons.types.Password;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.impl.passwords.PasswordStoreImpl;
import com.puresoltechnologies.lifeassist.app.model.contacts.QContacts;
import com.puresoltechnologies.lifeassist.app.model.contacts.QEmailAddresses;
import com.puresoltechnologies.passwordstore.api.PasswordStore;
import com.puresoltechnologies.passwordstore.domain.PasswordActivationException;
import com.puresoltechnologies.passwordstore.domain.PasswordChangeException;
import com.puresoltechnologies.passwordstore.domain.PasswordCreationException;
import com.puresoltechnologies.passwordstore.domain.PasswordResetException;
import com.querydsl.core.Tuple;

/**
 * This manager handles user account for login and authentication.
 * 
 * @author Rick-Rainer Ludwig
 */
public class AccountManager {

    /**
     * Creates a new account.
     * 
     * @param email
     * @param password
     * @throws SQLException
     * @throws PasswordCreationException
     * @throws PasswordActivationException
     */
    public void createAccount(EmailAddress email, Password password)
	    throws SQLException, PasswordCreationException, PasswordActivationException {
	PasswordStore passwordStore = new PasswordStoreImpl();
	String activationKey = passwordStore.createPassword(email, password);
	passwordStore.activatePassword(email, activationKey);
    }

    /**
     * Changes the password for an account.
     * 
     * @param email
     * @param oldPassword
     * @param newPassword
     * @return <code>true</code> is returned in case password change was
     *         successful. <code>false</code> is returned otherwise.
     * @throws SQLException
     * @throws PasswordChangeException
     */
    public boolean changeAccountPassword(EmailAddress email, Password oldPassword, Password newPassword)
	    throws SQLException, PasswordChangeException {
	PasswordStore passwordStore = new PasswordStoreImpl();
	return passwordStore.changePassword(email, oldPassword, newPassword);
    }

    public Password resetAccountPassword(EmailAddress email) throws SQLException, PasswordResetException {
	PasswordStore passwordStore = new PasswordStoreImpl();
	return passwordStore.resetPassword(email);
    }

    /**
     * Checks the password for validity.
     * 
     * @param email
     * @param password
     * @return
     * @throws SQLException
     * @throws PasswordChangeException
     */
    public boolean authenticateAccount(EmailAddress email, Password password) throws SQLException {
	PasswordStore passwordStore = new PasswordStoreImpl();
	return passwordStore.authenticate(email, password);
    }

    /**
     * Checks the password for validity.
     * 
     * @param email
     * @param password
     * @return
     * @throws SQLException
     * @throws PasswordChangeException
     */
    public void deleteAccount(EmailAddress email) throws SQLException {
	PasswordStore passwordStore = new PasswordStoreImpl();
	passwordStore.deletePassword(email);
    }

    public User getUser(EmailAddress email) throws SQLException {
	try (ExtendedSQLQueryFactory createQueryFactory = DatabaseConnector.createQueryFactory()) {
	    Tuple result = createQueryFactory.select(QContacts.contacts.name, QEmailAddresses.emailAddresses.address) //
		    .from(QContacts.contacts, QEmailAddresses.emailAddresses) //
		    .where(QEmailAddresses.emailAddresses.address.eq(email.getAddress())) //
		    .where(QEmailAddresses.emailAddresses.contactId.eq(QContacts.contacts.id)) //
		    .fetchOne();
	    String name = result.get(QContacts.contacts.name);
	    return new User(email, name, new Role("administrator", "Administrator"));
	}
    }

}
