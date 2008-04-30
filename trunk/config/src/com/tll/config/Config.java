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
	 * The default domain env key
	 */
	public static final String DEFAULT_DOMAIN_ENVIRONMENT_KEY = "USERDOMAIN";

	/**
	 * The default username env key
	 */
	public static final String DEFAULT_USERNAME_ENVIRONMENT_KEY = "USERNAME";

	private static String usernameKey = DEFAULT_USERNAME_ENVIRONMENT_KEY;

	private static String domainKey = DEFAULT_DOMAIN_ENVIRONMENT_KEY;

	/**
	 * Sets the domain environment key employed by the {@link Config} instance.
	 * @param key
	 */
	public static final void setDomainEnvKey(String key) {
		domainKey = key;
	}

	/**
	 * Sets the username environment key employed by the {@link Config} instance.
	 * @param key
	 */
	public static final void setUsernameEnvKey(String key) {
		usernameKey = key;
	}

	private static final Config instance = new Config();

	/**
	 * <p>
	 * FORMAT:
	 * <p>
	 * "{domain env key prop val}.{username env prop val}.config.properties"
	 * @return The user environment config file overriding the base config prop
	 *         file but overridable by the local confif file.
	 * @throws ConfigurationException When at least one of the env keys resolves
	 *         to a <code>null</code> value.
	 */
	private final String getDomainUserConfigPropFileName() throws ConfigurationException {
		String domain = System.getenv(domainKey);
		String username = System.getenv(usernameKey);
		if(domain == null || username == null) {
			throw new ConfigurationException();
		}
		return domain + '.' + username + ".config.properties";
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
	 * @throws RuntimeException When the app configuration can't be successfully
	 *         loaded.
	 */
	private Config() {
		super();
		reload();
	}

	/**
	 * [Re-]loads the configuration from disk.
	 */
	public void reload() {
		PropertiesConfiguration baseProps, userDomainProps, localProps;

		// load base props
		try {
			baseProps = new PropertiesConfiguration();
			baseProps.setDelimiterParsingDisabled(true);
			baseProps.load(CONFIG_PROPERTIES_FILE_NAME);
		}
		catch(ConfigurationException ce) {
			throw new RuntimeException("Unable to load base configuration: " + ce.getMessage(), ce);
		}

		// load domain user props
		try {
			userDomainProps = new PropertiesConfiguration();
			userDomainProps.setDelimiterParsingDisabled(true);
			userDomainProps.load(getDomainUserConfigPropFileName());
		}
		catch(ConfigurationException ce) {
			userDomainProps = null; // ok, this file is optional
		}

		// load optional props (if present)
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
		if(userDomainProps != null) cc.addConfiguration(userDomainProps);
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

	/**
	 * Saves the cofiguration properties to file.
	 * @param f
	 * @param prefix the prefix of the keys for the subset. May be
	 *        <code>null</code> in which case all properties are considered.
	 * @param prependToken String to prepend to all resultant subset properties.
	 *        May be <code>null</code>.
	 * @throws ConfigurationException
	 */
	public void saveAsPropFile(File f, String prefix, String prependToken) throws ConfigurationException {
		subsetAsProps(prefix, prependToken).save(f);
	}

	/**
	 * Puts the held properties in distinct String keyed and valued map.
	 * @param prefix the prefix of the keys for the subset. May be
	 *        <code>null</code> in which case all properties are considered.
	 * @param prependToken String to prepend to all resultant subset properties.
	 *        May be <code>null</code>.
	 * @return Map of String property names and String property values
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> asMap(String prefix, String prependToken) {
		PropertiesConfiguration pc = subsetAsProps(prefix, prependToken);
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(Iterator<String> itr = pc.getKeys(); itr.hasNext();) {
			String key = itr.next();
			String val = pc.getString(key);
			map.put(key, val);
		}
		return map;
	}

	/**
	 * Provides a {@link Properties} instance representation of this config
	 * instance.
	 * @param prefix the prefix of the keys for the subset. May be
	 *        <code>null</code> in which case all properties are considered.
	 * @return java.util.Properties instance
	 */
	public Properties asProperties(String prefix) {
		return ConfigurationConverter.getProperties(Config.instance().subset(prefix));
	}
}
