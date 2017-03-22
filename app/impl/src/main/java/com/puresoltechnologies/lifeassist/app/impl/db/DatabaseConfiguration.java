package com.puresoltechnologies.lifeassist.app.impl.db;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This class represent the database configuration.
 * 
 * @author Rick-Rainer Ludwig
 */
public class DatabaseConfiguration {

    @NotNull
    private String host;

    @Min(1)
    @Max(65536)
    private int port = 5432;

    @NotNull
    private String database;

    @NotNull
    private String user;

    @NotNull
    private String password;

    private boolean ssl = false;

    @Valid
    @NotNull
    private DatabasePoolConfiguration pool = new DatabasePoolConfiguration();

    public String getHost() {
	return host;
    }

    public void setHost(String host) {
	this.host = host;
    }

    public int getPort() {
	return port;
    }

    public void setPort(int port) {
	this.port = port;
    }

    public String getDatabase() {
	return database;
    }

    public void setDatabase(String database) {
	this.database = database;
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public boolean isSsl() {
	return ssl;
    }

    public void setSsl(boolean ssl) {
	this.ssl = ssl;
    }

    public DatabasePoolConfiguration getPool() {
	return pool;
    }

    public void setPool(DatabasePoolConfiguration pool) {
	this.pool = pool;
    }

}
