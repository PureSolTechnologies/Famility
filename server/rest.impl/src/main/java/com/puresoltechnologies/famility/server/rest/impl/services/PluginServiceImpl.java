package com.puresoltechnologies.famility.server.rest.impl.services;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.puresoltechnologies.famility.server.impl.plugins.PluginLoader;
import com.puresoltechnologies.famility.server.rest.api.plugins.PluginDescription;
import com.puresoltechnologies.famility.server.rest.api.plugins.PluginService;

@Timed
@ExceptionMetered
@Path("/plugins")
public class PluginServiceImpl implements PluginService {

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<PluginDescription> getPluginDescriptions() {
	return RestConverter.convertPluginDescriptions(PluginLoader.getInstance().getPluginDescriptions());
    }

}
