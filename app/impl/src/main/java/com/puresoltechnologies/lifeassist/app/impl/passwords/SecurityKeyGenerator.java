package com.puresoltechnologies.lifeassist.app.impl.passwords;

public interface SecurityKeyGenerator {

    /**
     * This function generates a security key for the password safe. This is the
     * database identifier for secured objects.
     * 
     * @return
     */
    public String generate();
}
