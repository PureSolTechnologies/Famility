package com.puresoltechnologies.famility.server.impl.settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.puresoltechnologies.famility.common.db.ParameterType;
import com.puresoltechnologies.famility.server.api.settings.ParameterDescription;
import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;

public class SettingsManager {

    public List<ParameterDescription> getSystemParmeters() throws SQLException {
	List<ParameterDescription> parameters = new ArrayList<>();
	try (Connection connection = DatabaseConnector.getConnection();
		Statement statement = connection.createStatement()) {
	    try (ResultSet resultSet = statement.executeQuery("SELECT * FROM system_settings")) {
		while (resultSet.next()) {
		    String name = resultSet.getString("parameter");
		    ParameterType type = ParameterType.valueOf(resultSet.getString("type"));
		    String value = resultSet.getString("value");
		    String defaultValue = resultSet.getString("default_value");
		    String unit = resultSet.getString("unit");
		    String description = resultSet.getString("description");
		    ParameterDescription parameter = new ParameterDescription(name, type, value, defaultValue, unit,
			    description);
		    parameters.add(parameter);
		}
	    }
	}
	return parameters;
    }

}
