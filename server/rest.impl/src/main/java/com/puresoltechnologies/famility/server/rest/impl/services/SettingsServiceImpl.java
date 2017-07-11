package com.puresoltechnologies.famility.server.rest.impl.services;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.puresoltechnologies.famility.server.impl.settings.SettingsManager;
import com.puresoltechnologies.famility.server.rest.api.services.ParameterDescription;
import com.puresoltechnologies.famility.server.rest.api.services.SettingsService;

@Path("/settings")
public class SettingsServiceImpl implements SettingsService {

    private static SettingsManager settingsManager = new SettingsManager();

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ParameterDescription> getSystemParameters() throws SQLException {
	return RestConverter.convertParameterDescriptions(settingsManager.getSystemParmeters());
    }

}
