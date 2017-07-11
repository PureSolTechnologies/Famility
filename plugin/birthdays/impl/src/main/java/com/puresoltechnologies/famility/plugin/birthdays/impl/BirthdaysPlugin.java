package com.puresoltechnologies.famility.plugin.birthdays.impl;

import com.puresoltechnologies.famility.common.plugins.LifeAssistantPlugin;
import com.puresoltechnologies.famility.common.plugins.PluginDescription;

public class BirthdaysPlugin implements LifeAssistantPlugin {
    private static final PluginDescription description = new PluginDescription("Birthdays Plugin",
	    "Manage birthdays and get remembered.");

    @Override
    public PluginDescription getDescription() {
	return description;
    }

}
