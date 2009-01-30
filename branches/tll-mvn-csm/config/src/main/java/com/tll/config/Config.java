package com.tll.config;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Config - Configuration store that loads properties from a properties file
 * that must be present on the runtime classpath:
 * {@value #CONFIG_PROPERTIES_FILE_NAME}
 * <p>
 * {@value #LOCAL_CONFIG_PROPERTIES_FILE_NAME} is optional but if present, these
 * held properties OVERRIDE those declared in
 * {@link #CONFIG_PROPERTIES_FILE_NAME}.
 * <p>
 * <strong>NOTE: </strong>Delimeter parsing is disabled.
 * @author jpk
 */
public final class Config implements Configuration {

	private static final Log log = LogFactory.getLog(Config.class);

	/**
	 * The base config file name.
	 */
	public static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

	/**
	 * The local config file name. Properties declared within this file override
	 * all other declared properties.
	 */
	public static final String LOCAL_CONFIG_PROPERTIES_FILE_NAME = "local.config.properties";

	/**
	 * The machine name system property key
	 */
	public static final String MACHINE_NAME_KEY = "tll.config.machine.name";

	/**
	 * The user name system property key
	 */
	public static final String USER_NAME_KEY = "tll.config.user.name";

	/**
	 * The config instance
	 */
	private static final Config instance = new Config();

	/**
	 * @return The {@link Config} instance.
	 */
	public static final Config instance() {
		return instance;
	}

	/**
	 * Implementation decoratee
	 */
	private CombinedConfiguration root;

	/**
	 * Constructor
	 * @throws RuntimeException When the configuration is not successfully loaded.
	 */
	private Config() {
		super();
	}

	/**
	 * Loads the configuration from disk re-loading if already loaded.
	 * <p>
	 * The config property files are expected to be at the root of the classpath.
	 * @see #load(String)
	 */
	public void load() {
		load(null);
	}

	/**
	 * Loads the configuration from disk re-loading if already loaded.
	 * @param basePath The base path that points to the dir containing the config
	 *        property files. May be <code>null</code> in which case, the config
	 *        files are expected to be at the root of the classpath.
	 */
	public void load(String basePath) {
		PropertiesConfiguration baseProps, machineUserProps = null, localProps;

		// load the required base props
		try {
			baseProps = new PropertiesConfiguration();
			baseProps.setBasePath(basePath);
			baseProps.setDelimiterParsingDisabled(true);
			baseProps.load(CONFIG_PROPERTIES_FILE_NAME);
			log.info(CONFIG_PROPERTIES_FILE_NAME + " loaded.");
		}
		catch(ConfigurationException ce) {
			throw new RuntimeException("Unable to load base configuration: " + ce.getMessage(), ce);
		}

		// load the optional machine user props
		String machineUserPropFileName = getMachineUserConfigPropFileName();
		if(machineUserPropFileName != null) {
			try {
				machineUserProps = new PropertiesConfiguration();
				machineUserProps.setBasePath(basePath);
				machineUserProps.setDelimiterParsingDisabled(true);
				machineUserProps.load(machineUserPropFileName);
				log.info(machineUserPropFileName + " loaded.");
			}
			catch(ConfigurationException ce) {
				// ok, this file is optional
			}
		}

		// load the optional local props overrides
		try {
			localProps = new PropertiesConfiguration();
			localProps.setBasePath(basePath);
			localProps.setDelimiterParsingDisabled(true);
			localProps.load(LOCAL_CONFIG_PROPERTIES_FILE_NAME);
			log.info(LOCAL_CONFIG_PROPERTIES_FILE_NAME + " loaded.");
		}
		catch(ConfigurationException ce) {
			localProps = null; // ok, this file is optional
		}

		CompositeConfiguration cc = new CompositeConfiguration();
		if(localProps != null) cc.addConfiguration(localProps);
		if(machineUserProps != null) cc.addConfiguration(machineUserProps);
		cc.addConfiguration(baseProps);

		root = new CombinedConfiguration();
		root.append(ConfigurationUtils.convertToHierarchical(cc));
		log.info("Config property files loaded.");
	}

	/**
	 * Unloads the configuration.
	 */
	public void unload() {
		root = null;
	}

	/**
	 * Determines the machine/user config file name based on the corresponding
	 * system property values for {@link #MACHINE_NAME_KEY} and
	 * {@link #USER_NAME_KEY}. FORMAT:
	 * <code>"{machine name}.{user name}.config.properties"</code>
	 * @return The user environment config file overriding the base config prop
	 *         file but overridable by the local confif file or <code>null</code>
	 *         if either the machine name or user name properties could not be
	 *         resolved.
	 */
	private final String getMachineUserConfigPropFileName() {
		String machinename = System.getProperty(MACHINE_NAME_KEY);
		if(machinename == null) {
			// try env property
			machinename = System.getenv(MACHINE_NAME_KEY);
		}
		if(machinename == null || machinename.isEmpty()) {
			return null;
		}
		String username = System.getProperty(USER_NAME_KEY);
		if(username == null) {
			// try env property
			username = System.getenv(USER_NAME_KEY);
		}
		if(username == null || username.isEmpty()) {
			return null;
		}
		return machinename + '.' + username + ".config.properties";
	}

	private CombinedConfiguration safeGetRoot() {
		if(root == null) {
			load();
		}
		return root;
	}

	public void addProperty(String key, Object value) {
		safeGetRoot().addProperty(key, value);
	}

	public void clear() {
		safeGetRoot().clear();
	}

	public void clearProperty(String key) {
		safeGetRoot().clearProperty(key);
	}

	public boolean containsKey(String key) {
		return safeGetRoot().containsKey(key);
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return safeGetRoot().getBigDecimal(key, defaultValue);
	}

	public BigDecimal getBigDecimal(String key) {
		return safeGetRoot().getBigDecimal(key);
	}

	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return safeGetRoot().getBigInteger(key, defaultValue);
	}

	public BigInteger getBigInteger(String key) {
		return safeGetRoot().getBigInteger(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return safeGetRoot().getBoolean(key, defaultValue);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		return safeGetRoot().getBoolean(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		return safeGetRoot().getBoolean(key);
	}

	public byte getByte(String key, byte defaultValue) {
		return safeGetRoot().getByte(key, defaultValue);
	}

	public Byte getByte(String key, Byte defaultValue) {
		return safeGetRoot().getByte(key, defaultValue);
	}

	public byte getByte(String key) {
		return safeGetRoot().getByte(key);
	}

	public double getDouble(String key, double defaultValue) {
		return safeGetRoot().getDouble(key, defaultValue);
	}

	public Double getDouble(String key, Double defaultValue) {
		return safeGetRoot().getDouble(key, defaultValue);
	}

	public double getDouble(String key) {
		return safeGetRoot().getDouble(key);
	}

	public float getFloat(String key, float defaultValue) {
		return safeGetRoot().getFloat(key, defaultValue);
	}

	public Float getFloat(String key, Float defaultValue) {
		return safeGetRoot().getFloat(key, defaultValue);
	}

	public float getFloat(String key) {
		return safeGetRoot().getFloat(key);
	}

	public int getInt(String key, int defaultValue) {
		return safeGetRoot().getInt(key, defaultValue);
	}

	public int getInt(String key) {
		return safeGetRoot().getInt(key);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		return safeGetRoot().getInteger(key, defaultValue);
	}

	public Iterator<?> getKeys() {
		return safeGetRoot().getKeys();
	}

	public Iterator<?> getKeys(String prefix) {
		return safeGetRoot().getKeys(prefix);
	}

	@SuppressWarnings("unchecked")
	public List<?> getList(String key, List defaultValue) {
		return safeGetRoot().getList(key, defaultValue);
	}

	public List<?> getList(String key) {
		return safeGetRoot().getList(key);
	}

	public long getLong(String key, long defaultValue) {
		return safeGetRoot().getLong(key, defaultValue);
	}

	public Long getLong(String key, Long defaultValue) {
		return safeGetRoot().getLong(key, defaultValue);
	}

	public long getLong(String key) {
		return safeGetRoot().getLong(key);
	}

	public Properties getProperties(String key) {
		return safeGetRoot().getProperties(key);
	}

	public Object getProperty(String key) {
		return safeGetRoot().getProperty(key);
	}

	public short getShort(String key, short defaultValue) {
		return safeGetRoot().getShort(key, defaultValue);
	}

	public Short getShort(String key, Short defaultValue) {
		return safeGetRoot().getShort(key, defaultValue);
	}

	public short getShort(String key) {
		return safeGetRoot().getShort(key);
	}

	public String getString(String key, String defaultValue) {
		return safeGetRoot().getString(key, defaultValue);
	}

	public String getString(String key) {
		return safeGetRoot().getString(key);
	}

	public String[] getStringArray(String key) {
		return safeGetRoot().getStringArray(key);
	}

	public boolean isEmpty() {
		return safeGetRoot().isEmpty();
	}

	public void setProperty(String key, Object value) {
		safeGetRoot().setProperty(key, value);
	}

	public Configuration subset(String prefix) {
		return safeGetRoot().subset(prefix);
	}

	/**
	 * Provides a subset of the configuration as a separate
	 * {@link PropertiesConfiguration} instance enabling extended functionality.
	 * @param prefix the prefix of the keys for the subset. May be
	 *        <code>null</code> in which case all properties are considered.
	 * @param prependToken String to prepend to all resultant subset properties.
	 *        May be <code>null</code>.
	 * @return new {@link PropertiesConfiguration} instance
	 */
	@SuppressWarnings("unchecked")
	private PropertiesConfiguration subsetAsProps(String prefix, String prependToken) {
		Configuration sub = subset(prefix);
		PropertiesConfiguration pc = new PropertiesConfiguration();
		for(Iterator<String> itr = sub.getKeys(); itr.hasNext();) {
			String key = itr.next();
			pc.addProperty(prependToken == null ? key : prependToken + key, sub.getString(key));
		}
		return pc;
	}

	private PropertiesConfiguration filter(IConfigKeyProvider keyProvider) {
		String[] keys = keyProvider == null ? null : keyProvider.getConfigKeys();
		if(keys == null) return null;
		PropertiesConfiguration pc = new PropertiesConfiguration();
		for(String key : keys) {
			pc.addProperty(key, root.getProperty(key));
		}
		return pc;
	}

	/**
	 * Saves the cofiguration properties to file.
	 * @param f The file to save to
	 * @throws ConfigurationException
	 */
	public void saveAsPropFile(File f) throws ConfigurationException {
		subsetAsProps(null, null).save(f);
	}

	/**
	 * Saves the cofiguration properties to file given a key provider which serves
	 * as a filter.
	 * @param f The file to save to
	 * @param keyProvider A config key provider
	 * @throws ConfigurationException
	 */
	public void saveAsPropFile(File f, IConfigKeyProvider keyProvider) throws ConfigurationException {
		filter(keyProvider).save(f);
	}

	/**
	 * Saves the cofiguration properties to file given a prefix and prepend token.
	 * @param f The file to save to
	 * @param prefix Optional prefix of the keys for the subset. If
	 *        <code>null</code>, all properties are considered.
	 * @param prependToken Optional token that is prepended to all resultant
	 *        subset properties. May be <code>null</code>.
	 * @throws ConfigurationException
	 */
	public void saveAsPropFile(File f, String prefix, String prependToken) throws ConfigurationException {
		subsetAsProps(prefix, prependToken).save(f);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> asMap(PropertiesConfiguration pc) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(Iterator<String> itr = pc.getKeys(); itr.hasNext();) {
			String key = itr.next();
			String val = pc.getString(key);
			map.put(key, val);
		}
		return map;
	}

	/**
	 * Puts the held properties in a distinct map given a config key provider.
	 * @param keyProvider The config key provider
	 * @return Map of String property names and String property values
	 */
	public Map<String, String> asMap(IConfigKeyProvider keyProvider) {
		return asMap(filter(keyProvider));
	}

	/**
	 * Puts the held properties in distinct String keyed and valued map.
	 * @param prefix the prefix of the keys for the subset. May be
	 *        <code>null</code> in which case all properties are considered.
	 * @param prependToken String to prepend to all resultant subset properties.
	 *        May be <code>null</code>.
	 * @return Map of String property names and String property values
	 */
	public Map<String, String> asMap(String prefix, String prependToken) {
		return asMap(subsetAsProps(prefix, prependToken));
	}

	/**
	 * Provides a {@link Properties} instance representation of this config
	 * instance.
	 * @param keyProvider The config key provider
	 * @return java.util.Properties instance
	 */
	public Properties asProperties(IConfigKeyProvider keyProvider) {
		return ConfigurationConverter.getProperties(filter(keyProvider));
	}

	/**
	 * Provides a {@link Properties} instance representation of this config
	 * instance.
	 * @param prefix the prefix of the keys for the subset. May be
	 *        <code>null</code> in which case all properties are considered.
	 * @return java.util.Properties instance
	 */
	public Properties asProperties(String prefix) {
		return ConfigurationConverter.getProperties(subset(prefix));
	}
}
