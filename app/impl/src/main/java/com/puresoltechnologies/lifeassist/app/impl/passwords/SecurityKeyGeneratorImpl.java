package com.puresoltechnologies.lifeassist.app.impl.passwords;

import com.puresoltechnologies.lifeassist.app.impl.passwords.encrypt.EncryptionUtilities;

/**
 * This bean manages the creation and
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class SecurityKeyGeneratorImpl implements SecurityKeyGenerator {

    @Override
    public String generate() {
	byte[] randomBytes = EncryptionUtilities.generateRandomBytes(32);
	return EncryptionUtilities.convertBytesToHexString(randomBytes);
    }
}
