package com.puresoltechnologies.famility.server.impl.plugins;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import com.puresoltechnologies.famility.common.plugins.FamilityPlugin;
import com.puresoltechnologies.famility.common.plugins.PluginDescription;

public class PluginLoader {

    private static final PluginLoader instance = new PluginLoader();

    public static PluginLoader getInstance() {
	return instance;
    }

    private Set<FamilityPlugin> plugins = new HashSet<>();

    private PluginLoader() {
	ServiceLoader<FamilityPlugin> serviceLoader = ServiceLoader.load(FamilityPlugin.class);
	serviceLoader.forEach(plugin -> plugins.add(plugin));
    }

    public Set<PluginDescription> getPluginDescriptions() {
	Set<PluginDescription> descriptions = new HashSet<>();
	plugins.forEach(plugin -> descriptions.add(plugin.getDescription()));
	return descriptions;
    }
}
