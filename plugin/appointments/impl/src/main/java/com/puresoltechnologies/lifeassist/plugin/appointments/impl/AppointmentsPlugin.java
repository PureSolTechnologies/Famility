package com.puresoltechnologies.lifeassist.plugin.appointments.impl;

import com.puresoltechnologies.lifeassist.common.plugins.LifeAssistantPlugin;
import com.puresoltechnologies.lifeassist.common.plugins.PluginDescription;

public class AppointmentsPlugin implements LifeAssistantPlugin {

    private static final PluginDescription description = new PluginDescription("Appointments Plugin",
	    "Manage your appointments and get remembered.");

    @Override
    public PluginDescription getDescription() {
	return description;
    }

}
