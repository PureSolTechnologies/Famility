package com.puresoltechnologies.famility.framework;

import java.io.File;

public class FamilityFrameworkConfiguration {

    public static class OSGIConfiguration {

	private String pluginsDirectory = null;
	private String dataDirectory = null;

	public String getPluginsDirectory() {
	    return pluginsDirectory;
	}

	public void setPluginsDirectory(String osgiPluginsDirectory) {
	    this.pluginsDirectory = osgiPluginsDirectory;
	}

	public String getDataDirectory() {
	    return dataDirectory;
	}

	public void setDataDirectory(String osgiDataDirectory) {
	    this.dataDirectory = osgiDataDirectory;
	}
    }

    private File configurationFile = null;
    private OSGIConfiguration osgi = null;

    public File getConfigurationFile() {
	return configurationFile;
    }

    public void setConfigurationFile(File configurationFile) {
	this.configurationFile = configurationFile;
    }

    public OSGIConfiguration getOsgi() {
	return osgi;
    }

    public void setOsgi(OSGIConfiguration osgi) {
	this.osgi = osgi;
    }

}
