package com.puresoltechnologies.famility.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.junit.Test;

import com.puresoltechnologies.famility.common.plugins.FamilityPlugin;

public class ServiceTest {

    @Test
    public void test() {
	ServiceLoader<FamilityPlugin> loader = ServiceLoader.load(FamilityPlugin.class);
	assertNotNull(loader);
	int count = 0;
	Iterator<FamilityPlugin> iterator = loader.iterator();
	while (iterator.hasNext()) {
	    System.out.println(iterator.next().getClass().getName());
	    count++;
	}
	assertEquals(3, count);
    }

}
