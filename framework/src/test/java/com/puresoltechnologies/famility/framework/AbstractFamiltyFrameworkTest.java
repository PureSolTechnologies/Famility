package com.puresoltechnologies.famility.framework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.yaml.snakeyaml.Yaml;

public abstract class AbstractFamiltyFrameworkTest {

    private static FamilityFrameworkConfiguration configuration = null;

    @BeforeClass
    public static void readConfiguration() throws IOException {
	try (InputStream inputStream = FamilityFrameworkConfigurationTest.class.getResourceAsStream("/framework.yml")) {
	    Yaml yaml = new Yaml();
	    configuration = yaml.loadAs(inputStream, FamilityFrameworkConfiguration.class);
	    configuration.setConfigurationFile(new File("src/main/resources"));
	}
    }

    protected static FamilityFrameworkConfiguration getConfiguration() {
	return configuration;
    }
}
