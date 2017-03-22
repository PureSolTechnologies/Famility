package com.puresoltechnologies.lifeassist.app.rest.services;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.puresoltechnologies.lifeassist.app.api.settings.ParameterDescription;
import com.puresoltechnologies.lifeassist.app.impl.settings.SettingsManager;

@Path("/settings")
public class SettingsServiceResource {

    private static SettingsManager settingsManager = new SettingsManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ParameterDescription> getSystemParameters() throws SQLException {
	return settingsManager.getSystemParmeters();
    }

}
