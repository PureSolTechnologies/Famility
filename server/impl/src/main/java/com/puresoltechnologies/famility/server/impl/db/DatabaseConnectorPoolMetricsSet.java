package com.puresoltechnologies.famility.server.impl.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;

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
public class DatabaseConnectorPoolMetricsSet implements MetricSet {

    private final GenericObjectPool<Connection> pool;

    @SuppressWarnings("unchecked")
    public DatabaseConnectorPoolMetricsSet() {
	try {
	    Field poolField = DatabaseConnector.class.getDeclaredField("pool");
	    poolField.setAccessible(true);
	    pool = (GenericObjectPool<Connection>) poolField.get(null);
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
	gauges.put("pool.waiters", new Gauge<Integer>() {
	    @Override
	    public Integer getValue() {
		return pool.getNumWaiters();
	    }
	});
	return Collections.unmodifiableMap(gauges);
    }

}
