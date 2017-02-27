package com.puresoltechnologies.lifeassist.plugin.tasks.impl;

import com.puresoltechnologies.lifeassist.common.plugins.LifeAssistantPlugin;
import com.puresoltechnologies.lifeassist.common.plugins.PluginDescription;

public class TasksPlugin implements LifeAssistantPlugin {

    private static final PluginDescription description = new PluginDescription("Appointments Plugin",
	    "Manage your tasks and get remembered.");

    @Override
    public PluginDescription getDescription() {
	return description;
    }

}
