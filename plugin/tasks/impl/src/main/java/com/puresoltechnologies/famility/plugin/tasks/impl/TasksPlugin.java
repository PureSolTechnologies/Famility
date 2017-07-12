package com.puresoltechnologies.famility.plugin.tasks.impl;

import com.puresoltechnologies.famility.common.plugins.FamilityPlugin;
import com.puresoltechnologies.famility.common.plugins.PluginDescription;

public class TasksPlugin implements FamilityPlugin {

    private static final PluginDescription description = new PluginDescription("Appointments Plugin",
	    "Manage your tasks and get remembered.");

    @Override
    public PluginDescription getDescription() {
	return description;
    }

}
