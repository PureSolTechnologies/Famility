package com.puresoltechnologies.lifeassist.app.rest.server.services;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.puresoltechnologies.lifeassist.app.impl.plugins.PluginLoader;
import com.puresoltechnologies.lifeassist.app.rest.api.plugins.PluginDescription;
import com.puresoltechnologies.lifeassist.app.rest.api.plugins.PluginService;

@Path("/plugins")
public class PluginServiceImpl implements PluginService {

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<PluginDescription> getPluginDescriptions() {
	return RestConverter.convertPluginDescriptions(PluginLoader.getInstance().getPluginDescriptions());
    }

}
