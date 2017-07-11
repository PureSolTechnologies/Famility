package com.puresoltechnologies.famility.server.rest.impl.metrics;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.SlidingTimeWindowReservoir;
import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;

/**
 * This metrics set contains the important values for the database connection
 * pool.
 * 
 * @author Rick-Rainer Ludwig
 *
 */
public class DatabaseConnectionPoolMetricsSet implements MetricSet {

    private final GenericObjectPool<Connection> pool;
    private final Histogram numIdleHistogram = new Histogram(new SlidingTimeWindowReservoir(7, TimeUnit.DAYS));
    private final Histogram numActiveHistogram = new Histogram(new SlidingTimeWindowReservoir(7, TimeUnit.DAYS));
    private final Histogram numWaitersHistogram = new Histogram(new SlidingTimeWindowReservoir(7, TimeUnit.DAYS));

    @SuppressWarnings("unchecked")
    public DatabaseConnectionPoolMetricsSet()
	    throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
	Field poolField = DatabaseConnector.class.getDeclaredField("pool");
	poolField.setAccessible(true);
	pool = (GenericObjectPool<Connection>) poolField.get(null);
    }

    @Override
    public Map<String, Metric> getMetrics() {
	int numIdle = pool.getNumIdle();
	int numActive = pool.getNumActive();
	int numWaiters = pool.getNumWaiters();
	Map<String, Metric> metrics = new HashMap<>();
	metrics.put("num.idle", new Gauge<Integer>() {
	    @Override
	    public Integer getValue() {
		return numIdle;
	    }
	});
	metrics.put("num.active", new Gauge<Integer>() {
	    @Override
	    public Integer getValue() {
		return numActive;
	    }
	});
	metrics.put("num.waiters", new Gauge<Integer>() {
	    @Override
	    public Integer getValue() {
		return numWaiters;
	    }
	});
	metrics.put("num.idle.histogram", numIdleHistogram);
	metrics.put("num.active.histogram", numActiveHistogram);
	metrics.put("num.waiters.histogram", numWaitersHistogram);
	return Collections.unmodifiableMap(metrics);
    }

}
