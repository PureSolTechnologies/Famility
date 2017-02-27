package com.puresoltechnologies.lifeassist.common.plugins;

/**
 * This is the LifeAssistent's plugin interface used by SPI to load plugins.
 * 
 * @author Rick-Rainer Ludwig
 */
public interface LifeAssistantPlugin {

    public PluginDescription getDescription();

}
