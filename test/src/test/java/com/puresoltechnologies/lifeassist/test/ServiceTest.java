package com.puresoltechnologies.lifeassist.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.common.plugins.LifeAssistantPlugin;

public class ServiceTest {

    @Test
    public void test() {
	ServiceLoader<LifeAssistantPlugin> loader = ServiceLoader.load(LifeAssistantPlugin.class);
	assertNotNull(loader);
	int count = 0;
	Iterator<LifeAssistantPlugin> iterator = loader.iterator();
	while (iterator.hasNext()) {
	    System.out.println(iterator.next().getClass().getName());
	    count++;
	}
	assertEquals(3, count);
    }

}
