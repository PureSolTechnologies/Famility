package com.puresoltechnologies.lifeassist.app.impl.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

public class ExtendedSQLQuery<T> extends SQLQuery<T> implements AutoCloseable {

    private static final long serialVersionUID = 1L;

    private final Connection connection;

    public ExtendedSQLQuery(Connection connection, SQLTemplates dialect) {
	super(connection, dialect);
	this.connection = connection;
    }

    public ExtendedSQLQuery(Connection connection, Configuration configuration) {
	super(connection, configuration);
	this.connection = connection;
    }

    public void commit() throws SQLException {
	connection.commit();
    }

    public void rollback() throws SQLException {
	connection.rollback();
    }

    @Override
    public void close() throws SQLException {
	connection.close();
    }

}
