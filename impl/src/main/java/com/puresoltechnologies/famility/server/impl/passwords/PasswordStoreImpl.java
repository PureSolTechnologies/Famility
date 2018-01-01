package com.puresoltechnologies.famility.server.impl.passwords;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.commons.types.Password;
import com.puresoltechnologies.famility.server.api.events.Event;
import com.puresoltechnologies.famility.server.api.events.EventLogger;
import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;
import com.puresoltechnologies.famility.server.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.famility.server.impl.events.EventLoggerImpl;
import com.puresoltechnologies.famility.server.model.settings.QPasswords;
import com.puresoltechnologies.passwordstore.api.PasswordStore;
import com.puresoltechnologies.passwordstore.core.PasswordEncrypter;
import com.puresoltechnologies.passwordstore.core.PasswordEncrypterImpl;
import com.puresoltechnologies.passwordstore.core.PasswordStrengthCalculator;
import com.puresoltechnologies.passwordstore.core.SecurityKeyGenerator;
import com.puresoltechnologies.passwordstore.core.SecurityKeyGeneratorImpl;
import com.puresoltechnologies.passwordstore.domain.PasswordActivationException;
import com.puresoltechnologies.passwordstore.domain.PasswordChangeException;
import com.puresoltechnologies.passwordstore.domain.PasswordCreationException;
import com.puresoltechnologies.passwordstore.domain.PasswordData;
import com.puresoltechnologies.passwordstore.domain.PasswordEncryptionException;
import com.puresoltechnologies.passwordstore.domain.PasswordResetException;
import com.puresoltechnologies.passwordstore.domain.PasswordState;
import com.querydsl.core.Tuple;

/**
 * This is the central implementation of the {@link PasswordStore}.
 * 
 * @author "Rick-Rainer Ludwig"
 * 
 */
public class PasswordStoreImpl implements PasswordStore {

    private Logger logger = LoggerFactory.getLogger(PasswordStoreImpl.class);

    public static final String PASSWORD_TABLE_NAME = "passwords";

    private static final List<Character> validCharacters = new ArrayList<>();

    static {
	for (char c = '0'; c <= '9'; c++) {
	    validCharacters.add(c);
	}
	for (char c = 'a'; c <= 'z'; c++) {
	    validCharacters.add(c);
	}
	for (char c = 'A'; c <= 'Z'; c++) {
	    validCharacters.add(c);
	}
	validCharacters.add('!');
	validCharacters.add('#');
	validCharacters.add('$');
	validCharacters.add('%');
	validCharacters.add('&');
	validCharacters.add('*');
	validCharacters.add('+');
	validCharacters.add(',');
	validCharacters.add('-');
	validCharacters.add('.');
	validCharacters.add('/');
	validCharacters.add(':');
	validCharacters.add(';');
	validCharacters.add('<');
	validCharacters.add('=');
	validCharacters.add('>');
	validCharacters.add('?');
	validCharacters.add('@');
	validCharacters.add('^');
	validCharacters.add('|');
	validCharacters.add('~');
    }

    private final SecurityKeyGenerator securityKeyGenerator = new SecurityKeyGeneratorImpl();
    private final PasswordEncrypter passwordEncrypter = new PasswordEncrypterImpl();

    private final EventLogger eventLogger;

    public PasswordStoreImpl() throws SQLException {
	eventLogger = new EventLoggerImpl();
    }

    @Override
    public String createPassword(EmailAddress email, Password password) throws PasswordCreationException {
	logger.info("An account for '" + email + "' is going to be created...");
	if (!PasswordStrengthCalculator.validate(password.getPassword())) {
	    Event event = PasswordStoreEvents.createPasswordTooWeakErrorEvent(email);
	    eventLogger.logEvent(event);
	    throw new PasswordCreationException(event.getMessage());
	}
	try {
	    if (getUserByEmail(email) != null) {
		Event event = PasswordStoreEvents.createAccountAlreadyExistsErrorEvent(email);
		eventLogger.logEvent(event);
		throw new PasswordCreationException(event.getMessage());
	    }

	    Date created = new Date();
	    Timestamp now = new Timestamp(created.getTime());
	    String activationKey = securityKeyGenerator.generate();
	    String passwordHash;
	    try {
		passwordHash = passwordEncrypter.encryptPassword(password.getPassword()).toString();
	    } catch (PasswordEncryptionException e) {
		Event event = PasswordStoreEvents.createPasswordEncryptionErrorEvent(email, e);
		eventLogger.logEvent(event);
		throw new RuntimeException(event.getMessage(), e);
	    }
	    try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
		queryFactory.insert(QPasswords.passwords) //
			.set(QPasswords.passwords.created, now) //
			.set(QPasswords.passwords.lastModified, now) //
			.set(QPasswords.passwords.state, PasswordState.CREATED.name()) //
			.set(QPasswords.passwords.email, email.getAddress()) //
			.set(QPasswords.passwords.password, passwordHash) //
			.set(QPasswords.passwords.activationKey, activationKey) //
			.execute();
		queryFactory.commit();

		eventLogger.logEvent(PasswordStoreEvents.createAccountCreationEvent(email, activationKey));
		return activationKey;
	    }
	} catch (SQLException e) {
	    throw new PasswordCreationException("Could not create password for user '" + email + "'.", e);
	}
    }

    @Override
    public EmailAddress activatePassword(EmailAddress email, String activationKey) throws PasswordActivationException {
	try {
	    logger.info("Account for user '" + email + "' is to be activated...");

	    Tuple account = getUserByEmail(email);
	    if (account == null) {
		Event event = PasswordStoreEvents.createInvalidEmailAddressErrorEvent(email);
		eventLogger.logEvent(event);
		throw new PasswordActivationException(event.getMessage());
	    }
	    String stateString = account.get(QPasswords.passwords.state);
	    if (!PasswordState.CREATED.name().equals(stateString)) {
		Event event = PasswordStoreEvents.createAccountAlreadyActivatedEvent(email);
		eventLogger.logEvent(event);
		throw new PasswordActivationException(event.getMessage());
	    }

	    String definedActivationKey = account.get(QPasswords.passwords.activationKey);
	    if (!definedActivationKey.equals(activationKey)) {
		Event event = PasswordStoreEvents.createInvalidActivationKeyErrorEvent(email);
		eventLogger.logEvent(event);
		throw new PasswordActivationException(event.getMessage());
	    }

	    try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
		queryFactory.update(QPasswords.passwords) //
			.set(QPasswords.passwords.lastModified, new Timestamp(new Date().getTime())) //
			.set(QPasswords.passwords.state, PasswordState.ACTIVE.name()) //
			.where(QPasswords.passwords.email.eq(email.getAddress())) //
			.execute();
		queryFactory.commit();
		eventLogger.logEvent(PasswordStoreEvents.createAccountActivatedEvent(email, activationKey));
		return email;
	    }
	} catch (SQLException e) {
	    throw new PasswordActivationException("Could not activate password activation for user '" + email + "'.",
		    e);
	}
    }

    private Tuple getUserByEmail(EmailAddress email) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    return queryFactory.select(QPasswords.passwords.all()) //
		    .from(QPasswords.passwords) //
		    .where(QPasswords.passwords.email.eq(email.getAddress())) //
		    .fetchOne();
	}
    }

    @Override
    public boolean authenticate(EmailAddress email, Password password) {
	try {
	    logger.info("Authenticating user '" + email + "'...");
	    Tuple account = getUserByEmail(email);
	    if (account == null) {
		eventLogger.logEvent(PasswordStoreEvents.createUserAuthenticationFailedAccountNotExistsEvent(email));
		return false;
	    }
	    String stateString = account.get(QPasswords.passwords.state);
	    if (!PasswordState.ACTIVE.name().equals(stateString)) {
		eventLogger.logEvent(PasswordStoreEvents.createUserAuthenticationFailedAccountNotActiveEvent(email));
		return false;
	    }
	    boolean authenticated = passwordEncrypter.checkPassword(password.getPassword(),
		    PasswordData.fromString(account.get(QPasswords.passwords.password)));
	    if (authenticated) {
		eventLogger.logEvent(PasswordStoreEvents.createUserAuthenticatedEvent(email));
	    } else {
		eventLogger.logEvent(PasswordStoreEvents.createUserAuthenticationFailedEvent(email));
	    }
	    return authenticated;
	} catch (SQLException | PasswordEncryptionException e) {
	    Event event = PasswordStoreEvents.createPasswordEncryptionErrorEvent(email, e);
	    eventLogger.logEvent(event);
	    throw new RuntimeException(event.getMessage(), e);
	}
    }

    @Override
    public boolean changePassword(EmailAddress email, Password oldPassword, Password newPassword)
	    throws PasswordChangeException {
	try {
	    logger.info("Password for user '" + email + "' is going to be changed...");
	    if (!authenticate(email, oldPassword)) {
		eventLogger.logEvent(PasswordStoreEvents.createPasswordChangeFailedNotAuthenticatedEvent(email));
		return false;
	    }
	    if (!PasswordStrengthCalculator.validate(newPassword.getPassword())) {
		Event event = PasswordStoreEvents.createPasswordChangeFailedPasswordTooWeakEvent(email);
		eventLogger.logEvent(event);
		throw new PasswordChangeException(event.getMessage());
	    }
	    String passwordHash;
	    try {
		passwordHash = passwordEncrypter.encryptPassword(newPassword.getPassword()).toString();
	    } catch (PasswordEncryptionException e) {
		Event event = PasswordStoreEvents.createPasswordEncryptionErrorEvent(email, e);
		eventLogger.logEvent(event);
		throw new RuntimeException(event.getMessage(), e);
	    }
	    try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
		queryFactory.update(QPasswords.passwords) //
			.set(QPasswords.passwords.password, passwordHash) //
			.set(QPasswords.passwords.lastModified, new Timestamp(Instant.now().getEpochSecond())) //
			.where(QPasswords.passwords.email.eq(email.getAddress())) //
			.execute();
		queryFactory.commit();

		eventLogger.logEvent(PasswordStoreEvents.createPasswordChangedEvent(email));
		return true;
	    }
	} catch (SQLException e) {
	    throw new RuntimeException("Could not change password for user '" + email + "'.", e);
	}
    }

    @Override
    public Password resetPassword(EmailAddress email) throws PasswordResetException {
	try {
	    logger.info("Password for user '" + email + "' is going to be reset...");
	    Tuple account = getUserByEmail(email);
	    if (account == null) {
		Event event = PasswordStoreEvents.createPasswordResetFailedUnknownAccountEvent(email);
		eventLogger.logEvent(event);
		throw new PasswordResetException(event.getMessage());
	    }
	    String password = generatePassword();
	    String passwordHash;
	    try {
		passwordHash = passwordEncrypter.encryptPassword(password).toString();
	    } catch (PasswordEncryptionException e) {
		Event event = PasswordStoreEvents.createPasswordEncryptionErrorEvent(email, e);
		eventLogger.logEvent(event);
		throw new RuntimeException(event.getMessage(), e);
	    }
	    try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
		queryFactory.update(QPasswords.passwords) //
			.set(QPasswords.passwords.password, passwordHash) //
			.set(QPasswords.passwords.lastModified, new Timestamp(Instant.now().getEpochSecond())) //
			.where(QPasswords.passwords.email.eq(email.getAddress())) //
			.execute();
		queryFactory.commit();
	    }
	    eventLogger.logEvent(PasswordStoreEvents.createPasswordResetEvent(email));
	    return new Password(password);
	} catch (SQLException e) {
	    throw new RuntimeException("Could not change password for user '" + email + "'.", e);
	}
    }

    private String generatePassword() {
	Random random = new Random();
	StringBuilder builder;
	do {
	    builder = new StringBuilder();
	    for (int i = 0; i < 8; i++) {
		builder.append(validCharacters.get(random.nextInt(validCharacters.size())));
	    }
	} while (!PasswordStrengthCalculator.validate(builder.toString()));
	return builder.toString();
    }

    @Override
    public void deletePassword(EmailAddress email) {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    queryFactory.delete(QPasswords.passwords) //
		    .where(QPasswords.passwords.email.eq(email.getAddress())) //
		    .execute();
	    queryFactory.commit();
	    eventLogger.logEvent(PasswordStoreEvents.createPasswordDeleteEvent(email));
	} catch (SQLException e) {
	    throw new RuntimeException("Could not delete account for user '" + email + "'.", e);
	}
    }
}
