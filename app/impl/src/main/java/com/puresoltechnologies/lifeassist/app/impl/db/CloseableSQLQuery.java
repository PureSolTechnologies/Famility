package com.puresoltechnologies.lifeassist.app.impl.db;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

public class CloseableSQLQuery<T> extends SQLQuery<T> implements Closeable {

    private static final long serialVersionUID = 1L;

    private final Connection connection;

    public CloseableSQLQuery(Connection connection, SQLTemplates dialect) {
	super(connection, dialect);
	this.connection = connection;
    }

    public CloseableSQLQuery(Connection connection, Configuration configuration) {
	super(connection, configuration);
	this.connection = connection;
    }

    @Override
    public void close() throws IOException {
	try {
	    connection.close();
	} catch (SQLException e) {
	    throw new IOException("Could not close database connection.", e);
	}
    }

}
