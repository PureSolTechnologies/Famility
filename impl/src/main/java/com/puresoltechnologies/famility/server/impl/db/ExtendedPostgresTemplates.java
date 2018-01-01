package com.puresoltechnologies.famility.server.impl.db;

import com.querydsl.sql.PostgreSQLTemplates;

/**
 * This class was overwritten to configure the templates and to use explicit
 * schemas.
 * 
 * @author Rick-Rainer Ludwig
 */
public class ExtendedPostgresTemplates extends PostgreSQLTemplates {

    public ExtendedPostgresTemplates() {
	super();
	setPrintSchema(true);
    }

}
