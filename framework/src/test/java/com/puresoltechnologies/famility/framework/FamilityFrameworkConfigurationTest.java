package com.puresoltechnologies.famility.framework;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.puresoltechnologies.famility.framework.FamilityFrameworkConfiguration.OSGIConfiguration;

public class FamilityFrameworkConfigurationTest {

    @Test
    public void test() throws IOException {
	try (InputStream inputStream = FamilityFrameworkConfigurationTest.class.getResourceAsStream("/framework.yml")) {
	    Yaml yaml = new Yaml();
	    FamilityFrameworkConfiguration configuration = yaml.loadAs(inputStream,
		    FamilityFrameworkConfiguration.class);
	    assertNotNull(configuration);

	    OSGIConfiguration osgi = configuration.getOsgi();
	    assertNotNull(osgi);
	    assertNotNull(osgi.getDataDirectory());
	    assertNotNull(osgi.getPluginsDirectory());
	}
    }

}
