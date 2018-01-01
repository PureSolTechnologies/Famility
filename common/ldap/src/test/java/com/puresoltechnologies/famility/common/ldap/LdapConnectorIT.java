package com.puresoltechnologies.famility.common.ldap;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class LdapConnectorIT {

    @BeforeClass
    public void initialize() {
	LdapConnector.initialize();
    }

    @AfterClass
    public void shutdown() {
	LdapConnector.shutdown();
    }

    @Test
    public void test() throws LdapException, IOException, CursorException {
	try (LdapConnection connection = new LdapNetworkConnection("ldap", 389)) {
	    // base: base dc=puresol-technologies,dc=net
	    // The password is encrypted, but it does not protect against a MITM attack
	    connection.bind("cn=admin,dc=puresol-technologies,dc=net", "TrustNo1");
	    // DefaultEntry newEntry = new DefaultEntry("cn=test2,ou=People,
	    // dc=puresol-technologies, dc=net");
	    // newEntry.add("objectClass", "organizationalRole");
	    // newEntry.add("description", "desc");
	    // connection.add(newEntry);
	    try (EntryCursor cursor = connection.search("ou=People, dc=puresol-technologies, dc=net", "(objectclass=*)",
		    SearchScope.ONELEVEL, "*")) {
		while (cursor.next()) {
		    Entry entry = cursor.get();
		    System.out.println("cn: " + entry.get("cn").get());
		    System.out.println("sn: " + entry.get("sn").get());
		    System.out.println("givenName: " + entry.get("givenName").get());
		    Attribute photo = entry.get("jpegPhoto");
		    if (photo != null) {
			byte[] bytes = photo.getBytes();
			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(bytes));
			System.out.println(
				"Width x Heigth:" + originalImage.getWidth() + " x " + originalImage.getHeight());
		    }
		}
	    } finally {
		connection.unBind();
	    }
	}
    }

}
