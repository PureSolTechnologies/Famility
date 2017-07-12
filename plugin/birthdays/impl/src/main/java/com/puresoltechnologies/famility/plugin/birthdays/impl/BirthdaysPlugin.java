package com.puresoltechnologies.famility.plugin.birthdays.impl;

import com.puresoltechnologies.famility.common.plugins.FamilityPlugin;
import com.puresoltechnologies.famility.common.plugins.PluginDescription;

public class BirthdaysPlugin implements FamilityPlugin {
    private static final PluginDescription description = new PluginDescription("Birthdays Plugin",
	    "Manage birthdays and get remembered.");

    @Override
    public PluginDescription getDescription() {
	return description;
    }

}
