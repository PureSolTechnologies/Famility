package com.puresoltechnologies.famility.common.ldap;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.directory.ldap.client.api.LdapConnectionPool;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;

/**
 * This metrics set contains the important values for the database connection
 * pool.
 * 
 * @author Rick-Rainer Ludwig
 *
 */
public class LdapConnectorPoolMetricsSet implements MetricSet {

    private final LdapConnectionPool pool;

    public LdapConnectorPoolMetricsSet() {
	try {
	    Field poolField = LdapConnector.class.getDeclaredField("pool");
	    poolField.setAccessible(true);
	    pool = (LdapConnectionPool) poolField.get(null);
	} catch (SecurityException | NoSuchFieldException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public Map<String, Metric> getMetrics() {
	Map<String, Metric> gauges = new HashMap<>();
	gauges.put("pool.idle", new Gauge<Integer>() {
	    @Override
	    public Integer getValue() {
		return pool.getNumIdle();
	    }
	});
	gauges.put("pool.active", new Gauge<Integer>() {
	    @Override
	    public Integer getValue() {
		return pool.getNumActive();
	    }
	});
	return Collections.unmodifiableMap(gauges);
    }

}
