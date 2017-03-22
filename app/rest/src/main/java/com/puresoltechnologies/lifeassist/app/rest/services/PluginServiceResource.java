package com.puresoltechnologies.lifeassist.app.rest.services;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.puresoltechnologies.lifeassist.app.impl.plugins.PluginLoader;
import com.puresoltechnologies.lifeassist.common.plugins.PluginDescription;

@Path("/plugins")
public class PluginServiceResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<PluginDescription> getPluginDescriptions() {
	return PluginLoader.getInstance().getPluginDescriptions();
    }

}
