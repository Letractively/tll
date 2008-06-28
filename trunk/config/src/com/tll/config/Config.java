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

	private static final Config instance = new Config();

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
		reload();
	}

	/**
	 * [Re-]loads the configuration from disk.
	 */
	public void reload() {
		PropertiesConfiguration baseProps, machineUserProps = null, localProps;

		// load the required base props
		try {
			baseProps = new PropertiesConfiguration();
			baseProps.setDelimiterParsingDisabled(true);
			baseProps.load(CONFIG_PROPERTIES_FILE_NAME);
		}
		catch(ConfigurationException ce) {
			throw new RuntimeException("Unable to load base configuration: " + ce.getMessage(), ce);
		}

		// load the optional machine user props
		String machineUserPropFileName = getMachineUserConfigPropFileName();
		if(machineUserPropFileName != null) {
			try {
				machineUserProps = new PropertiesConfiguration();
				machineUserProps.setDelimiterParsingDisabled(true);
				machineUserProps.load(machineUserPropFileName);
			}
			catch(ConfigurationException ce) {
				// ok, this file is optional
			}
		}

		// load the optional local props overrides
		try {
			localProps = new PropertiesConfiguration();
			localProps.setDelimiterParsingDisabled(true);
			localProps.load(LOCAL_CONFIG_PROPERTIES_FILE_NAME);
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
	}

	public void addProperty(String key, Object value) {
		root.addProperty(key, value);
	}

	public void clear() {
		root.clear();
	}

	public void clearProperty(String key) {
		root.clearProperty(key);
	}

	public boolean containsKey(String key) {
		return root.containsKey(key);
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return root.getBigDecimal(key, defaultValue);
	}

	public BigDecimal getBigDecimal(String key) {
		return root.getBigDecimal(key);
	}

	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return root.getBigInteger(key, defaultValue);
	}

	public BigInteger getBigInteger(String key) {
		return root.getBigInteger(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return root.getBoolean(key, defaultValue);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		return root.getBoolean(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		return root.getBoolean(key);
	}

	public byte getByte(String key, byte defaultValue) {
		return root.getByte(key, defaultValue);
	}

	public Byte getByte(String key, Byte defaultValue) {
		return root.getByte(key, defaultValue);
	}

	public byte getByte(String key) {
		return root.getByte(key);
	}

	public double getDouble(String key, double defaultValue) {
		return root.getDouble(key, defaultValue);
	}

	public Double getDouble(String key, Double defaultValue) {
		return root.getDouble(key, defaultValue);
	}

	public double getDouble(String key) {
		return root.getDouble(key);
	}

	public float getFloat(String key, float defaultValue) {
		return root.getFloat(key, defaultValue);
	}

	public Float getFloat(String key, Float defaultValue) {
		return root.getFloat(key, defaultValue);
	}

	public float getFloat(String key) {
		return root.getFloat(key);
	}

	public int getInt(String key, int defaultValue) {
		return root.getInt(key, defaultValue);
	}

	public int getInt(String key) {
		return root.getInt(key);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		return root.getInteger(key, defaultValue);
	}

	public Iterator<?> getKeys() {
		return root.getKeys();
	}

	public Iterator<?> getKeys(String prefix) {
		return root.getKeys(prefix);
	}

	@SuppressWarnings("unchecked")
	public List<?> getList(String key, List defaultValue) {
		return root.getList(key, defaultValue);
	}

	public List<?> getList(String key) {
		return root.getList(key);
	}

	public long getLong(String key, long defaultValue) {
		return root.getLong(key, defaultValue);
	}

	public Long getLong(String key, Long defaultValue) {
		return root.getLong(key, defaultValue);
	}

	public long getLong(String key) {
		return root.getLong(key);
	}

	public Properties getProperties(String key) {
		return root.getProperties(key);
	}

	public Object getProperty(String key) {
		return root.getProperty(key);
	}

	public short getShort(String key, short defaultValue) {
		return root.getShort(key, defaultValue);
	}

	public Short getShort(String key, Short defaultValue) {
		return root.getShort(key, defaultValue);
	}

	public short getShort(String key) {
		return root.getShort(key);
	}

	public String getString(String key, String defaultValue) {
		return root.getString(key, defaultValue);
	}

	public String getString(String key) {
		return root.getString(key);
	}

	public String[] getStringArray(String key) {
		return root.getStringArray(key);
	}

	public boolean isEmpty() {
		return root.isEmpty();
	}

	public void setProperty(String key, Object value) {
		root.setProperty(key, value);
	}

	public Configuration subset(String prefix) {
		return root.subset(prefix);
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
	 * @param prefix the prefix of the keys for the subset. May be
	 *        <code>null</code> in which case all properties are considered.
	 * @param prependToken String to prepend to all resultant subset properties.
	 *        May be <code>null</code>.
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
