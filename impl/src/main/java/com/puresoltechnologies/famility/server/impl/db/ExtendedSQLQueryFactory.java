package com.puresoltechnologies.famility.server.impl.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Provider;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQueryFactory;

public class ExtendedSQLQueryFactory extends SQLQueryFactory implements AutoCloseable {

    public ExtendedSQLQueryFactory(Configuration configuration, Provider<Connection> connProvider) {
	super(configuration, connProvider);
    }

    public void commit() throws SQLException {
	getConnection().commit();
    }

    public void rollback() throws SQLException {
	getConnection().rollback();
    }

    @Override
    public void close() throws SQLException {
	getConnection().close();
    }

}
