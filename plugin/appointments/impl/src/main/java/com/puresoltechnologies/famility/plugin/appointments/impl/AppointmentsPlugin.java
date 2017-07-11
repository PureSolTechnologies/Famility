package com.puresoltechnologies.famility.plugin.appointments.impl;

import com.puresoltechnologies.famility.common.plugins.LifeAssistantPlugin;
import com.puresoltechnologies.famility.common.plugins.PluginDescription;

public class AppointmentsPlugin implements LifeAssistantPlugin {

    private static final PluginDescription description = new PluginDescription("Appointments Plugin",
	    "Manage your appointments and get remembered.");

    @Override
    public PluginDescription getDescription() {
	return description;
    }

}
