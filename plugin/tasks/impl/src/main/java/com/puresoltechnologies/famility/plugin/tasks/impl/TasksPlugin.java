package com.puresoltechnologies.famility.plugin.tasks.impl;

import com.puresoltechnologies.famility.common.plugins.LifeAssistantPlugin;
import com.puresoltechnologies.famility.common.plugins.PluginDescription;

public class TasksPlugin implements LifeAssistantPlugin {

    private static final PluginDescription description = new PluginDescription("Appointments Plugin",
	    "Manage your tasks and get remembered.");

    @Override
    public PluginDescription getDescription() {
	return description;
    }

}
