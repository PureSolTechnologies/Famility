package com.puresoltechnologies.famility.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.felix.main.AutoProcessor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class FamilityFramework {

    private static final Logger logger = LoggerFactory.getLogger(FamilityFramework.class);

    private static final long DEFAULT_AUTO_DEPLOYMENT_PERIOD = 10; // [s]

    private final Map<String, String> configuration = new HashMap<>();
    private Framework framework = null;
    private Thread autoDeployerThread = null;

    public FamilityFramework(FamilityFrameworkConfiguration frameworkConfiguration) {
	// base directory
	File baseDirectory = frameworkConfiguration.getConfigurationFile().getParentFile();
	// data directory
	File dataDirectory = new File(baseDirectory, frameworkConfiguration.getOsgi().getDataDirectory());
	logger.info("Configured OSGi data directory: " + dataDirectory);
	dataDirectory.mkdirs();
	configuration.put(Constants.FRAMEWORK_STORAGE, dataDirectory.getPath());
	configuration.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
	// plugins directory
	File pluginsDirectory = new File(baseDirectory, frameworkConfiguration.getOsgi().getPluginsDirectory());
	logger.info("Configured OSGi plugins directory: " + pluginsDirectory);
	pluginsDirectory.mkdirs();
	configuration.put(AutoProcessor.AUTO_DEPLOY_DIR_PROPERTY, pluginsDirectory.getPath());
	configuration.put(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERTY, //
		AutoProcessor.AUTO_DEPLOY_INSTALL_VALUE + "," //
		// + AutoProcessor.AUTO_DEPLOY_UPDATE_VALUE + "," //
			+ AutoProcessor.AUTO_DEPLOY_START_VALUE);
    }

    public void startup() throws BundleException {
	startOSGi();
    }

    private void startOSGi() throws BundleException {
	logger.info("Starting OSGi framework...");
	ServiceLoader<FrameworkFactory> frameworkFactories = ServiceLoader.load(FrameworkFactory.class);
	Iterator<FrameworkFactory> frameworkFactoriesIterator = frameworkFactories.iterator();
	if (!frameworkFactoriesIterator.hasNext()) {
	    throw new BundleException("No OSGi framework factory was found via SPI.");
	}
	FrameworkFactory frameworkFactory = frameworkFactoriesIterator.next();
	if (frameworkFactoriesIterator.hasNext()) {
	    throw new BundleException(
		    "Multiple OSGi framework factories were found via SPI. No idea which to instantiate. Aborting...");
	}
	framework = frameworkFactory.newFramework(configuration);
	framework.init();
	framework.start();
	// File file = new
	// File("/home/ludwig/git/FamilityServer/server/bundle/target/server.bundle-0.1.0-SNAPSHOT.jar");
	// installBundle(file);

	logger.info("OSGi framework started.");
    }

    private void installBundle(File file) throws BundleException {
	try {
	    Bundle installedBundle = framework.getBundleContext().installBundle("file://" + file,
		    new FileInputStream(file));
	    installedBundle.start(Framework.START_ACTIVATION_POLICY);
	    installedBundle.getBundleContext().getBundles();
	} catch (FileNotFoundException e) {
	    throw new BundleException("Cannot find bundle '" + file.getAbsolutePath() + "'.", e);
	}
    }

    public void shutdown() throws BundleException {
	logger.info("Stopping OSGi framework...");
	framework.stop();
	logger.info("OSGi framework stopped.");
    }

    private void stopAutoDeployer() {
	logger.info("Stopping AutoDeployer thread...");
	if (autoDeployerThread == null) {
	    throw new IllegalStateException("AutoDeployer thread was not started, yet.");
	}
	autoDeployerThread.interrupt();
	autoDeployerThread = null;
	logger.info("AutoDeployer thread stopped.");
    }

    private void startAutoDeployer() {
	logger.info("Starting AutoDeployer thread...");
	if (autoDeployerThread != null) {
	    throw new IllegalStateException("AutoDeployer thread was already started.");
	}
	autoDeployerThread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		try {
		    while (true) {
			logger.info("AutoDeployer invoked.");
			AutoProcessor.process(configuration, framework.getBundleContext());
			TimeUnit.SECONDS.sleep(DEFAULT_AUTO_DEPLOYMENT_PERIOD);
		    }
		} catch (InterruptedException e) {
		    logger.info("AutoDeployer thread was interrupted.");
		}
	    }
	});
	autoDeployerThread.start();
	logger.info("AutoDeployer thread started.");
    }

    private void addShutdownHook() {
	Runtime.getRuntime().addShutdownHook(new Thread("OSGi Shutdown Hook") {
	    @Override
	    public void run() {
		try {
		    stopAutoDeployer();
		    shutdown();
		} catch (Exception ex) {
		    logger.error("Error stopping OSGi.", ex);
		}
	    }
	});
    }

    private void waitForFinish() throws InterruptedException {
	framework.waitForStop(0);
    }

    public Framework getFramework() {
	return framework;
    }

    public String getStateString(int state) {
	switch (state) {
	case Framework.ACTIVE:
	    return "ACTIVE";
	case Framework.INSTALLED:
	    return "INSTALLED";
	case Framework.RESOLVED:
	    return "RESOLVED";
	case Framework.STARTING:
	    return "STARTING";
	case Framework.STOPPING:
	    return "STOPPING";
	case Framework.UNINSTALLED:
	    return "UNINSTALLED";
	default:
	    return "unknown: " + state;
	}
    }

    private static CommandLine parseCommandLine(String[] args) throws ParseException {
	Options options = new Options();
	options.addOption("h", "help", false, "Requests this help page.");

	CommandLineParser parser = new DefaultParser();
	CommandLine cmd = parser.parse(options, args);

	if (cmd.hasOption("h")) {
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp(72, FamilityFramework.class.getSimpleName() + " [options] <config directory>",
		    "\nAvailable Options:", options, "");
	    System.exit(0);
	}

	String[] arguments = cmd.getArgs();
	if (arguments.length != 1) {
	    System.err.println("Configuration directory is missing.");
	    System.exit(1);
	}
	return cmd;
    }

    private static FamilityFrameworkConfiguration readConfigurationFile(CommandLine cmd) throws IOException {
	File configurationFile = new File(cmd.getArgs()[0]);
	if (!configurationFile.exists()) {
	    System.err.println("Configuration file '" + configurationFile + "' does not exist.");
	    System.exit(1);
	}
	if (!configurationFile.isFile()) {
	    System.err.println("Path '" + configurationFile + "' does not point to a file.");
	    System.exit(1);
	}
	FamilityFrameworkConfiguration configuration;
	try (InputStream inputStream = new FileInputStream(configurationFile)) {
	    Yaml yaml = new Yaml();
	    configuration = yaml.loadAs(inputStream, FamilityFrameworkConfiguration.class);
	}
	configuration.setConfigurationFile(configurationFile);
	return configuration;
    }

    public static void main(String[] args) {
	try {
	    CommandLine cmd = parseCommandLine(args);
	    FamilityFrameworkConfiguration configuration = readConfigurationFile(cmd);

	    FamilityFramework bootstrap = new FamilityFramework(configuration);
	    bootstrap.startup();
	    bootstrap.startAutoDeployer();
	    bootstrap.addShutdownHook();
	} catch (Throwable throwable) {
	    logger.error("A fatal error ocurred. Aborting...", throwable);
	    System.exit(1);
	}
    }

}
