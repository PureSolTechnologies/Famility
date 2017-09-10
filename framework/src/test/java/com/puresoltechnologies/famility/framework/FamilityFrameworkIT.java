package com.puresoltechnologies.famility.framework;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

public class FamilityFrameworkIT extends AbstractFamiltyFrameworkTest {

    private static FamilityFramework familityFramework;

    @BeforeClass
    public static void initialize() throws BundleException {
	familityFramework = new FamilityFramework(getConfiguration());
	familityFramework.startup();
    }

    @AfterClass
    public static void destroy() throws BundleException {
	familityFramework.shutdown();
    }

    @Test
    public void test() {
	Framework framework = familityFramework.getFramework();
	System.out.println(framework.getSymbolicName());
	System.out.println(framework.getVersion());
	System.out.println(framework.getLocation());
	System.out.println(framework.getState());
	System.out.println(familityFramework.getStateString(framework.getState()));
    }
}
