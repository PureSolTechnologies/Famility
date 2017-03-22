package com.puresoltechnologies.lifeassist.app.impl.db;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * This class contains the settings for the database connection pool.
 * 
 * @author Rick-Rainer Ludwig
 */
public class DatabasePoolConfiguration {

    @Min(0)
    @Max(32768)
    private int minIdle = 5;

    @Min(0)
    @Max(32768)
    private int setMaxIdle = 25;

    @Min(1)
    @Max(32768)
    private int maxTotal = 100;

    @Min(-1)
    @Max(32768)
    private long maxWaitMillis = -1;

    public int getMinIdle() {
	return minIdle;
    }

    public void setMinIdle(int minIdle) {
	this.minIdle = minIdle;
    }

    public int getSetMaxIdle() {
	return setMaxIdle;
    }

    public void setSetMaxIdle(int setMaxIdle) {
	this.setMaxIdle = setMaxIdle;
    }

    public int getMaxTotal() {
	return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
	this.maxTotal = maxTotal;
    }

    public long getMaxWaitMillis() {
	return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
	this.maxWaitMillis = maxWaitMillis;
    }

}
