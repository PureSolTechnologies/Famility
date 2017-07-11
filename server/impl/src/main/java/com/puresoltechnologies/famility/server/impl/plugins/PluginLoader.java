package com.puresoltechnologies.famility.server.impl.plugins;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import com.puresoltechnologies.famility.common.plugins.LifeAssistantPlugin;
import com.puresoltechnologies.famility.common.plugins.PluginDescription;

public class PluginLoader {

    private static final PluginLoader instance = new PluginLoader();

    public static PluginLoader getInstance() {
	return instance;
    }

    private Set<LifeAssistantPlugin> plugins = new HashSet<>();

    private PluginLoader() {
	ServiceLoader<LifeAssistantPlugin> serviceLoader = ServiceLoader.load(LifeAssistantPlugin.class);
	serviceLoader.forEach(plugin -> plugins.add(plugin));
    }

    public Set<PluginDescription> getPluginDescriptions() {
	Set<PluginDescription> descriptions = new HashSet<>();
	plugins.forEach(plugin -> descriptions.add(plugin.getDescription()));
	return descriptions;
    }
}
