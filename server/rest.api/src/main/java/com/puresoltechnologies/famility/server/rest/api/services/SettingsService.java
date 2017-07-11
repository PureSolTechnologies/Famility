package com.puresoltechnologies.famility.server.rest.api.services;

import java.sql.SQLException;
import java.util.List;

public interface SettingsService {

    public List<ParameterDescription> getSystemParameters() throws SQLException;

}
