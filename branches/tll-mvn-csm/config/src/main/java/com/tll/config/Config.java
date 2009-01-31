package com.tll.config;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Config - Configuration store able to load multiple property files and hold
 * xml-like data structures.
 * <p>
 * <strong>NOTE: </strong>Delimeter parsing is disabled.
 * @author jpk
 */
public final class Config implements Configuration {

	private static final Log log = LogFactory.getLog(Config.class);

	/**
	 * The base config file name.
	 */
	public static final String DEFAULT_CONFIG_PROPERTIES_FILE_NAME = "config.properties";

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
	private final CombinedConfiguration root;

	/**
	 * Constructor
	 * @throws RuntimeException When the configuration is not successfully loaded.
	 */
	private Config() {
		super();
		root = new CombinedConfiguration();
	}

	/**
	 * Attempts to load properties from
	 * {@link #DEFAULT_CONFIG_PROPERTIES_FILE_NAME} at the root of the classpath
	 * with <em>NO</em> delimeter parsing.
	 * @see #load(boolean, boolean)
	 */
	public void load() {
		load(true, false);
	}

	/**
	 * Attempts to load properties from
	 * {@link #DEFAULT_CONFIG_PROPERTIES_FILE_NAME} at the root of the classpath.
	 * @param disableDelimeterParsing
	 * @param merge Replace existing properties with those of the same name at the
	 *        given url?
	 * @see #loadProperties(URL, boolean, boolean)
	 */
	public void load(boolean disableDelimeterParsing, boolean merge) {
		loadProperties((Thread.currentThread().getContextClassLoader()).getResource(DEFAULT_CONFIG_PROPERTIES_FILE_NAME),
				disableDelimeterParsing, merge);
	}

	/**
	 * Loads properties from the given resource url.
	 * @param url Points to the properties file to be loaded.
	 * @param disableDelimeterParsing Disable delimeter parsing?
	 * @param merge Replace existing properties with those of the same name at the
	 *        given url? If <code>false</code>, the proerties are "appended".
	 * @see PropertiesConfiguration#setDelimiterParsingDisabled(boolean)
	 * @see CombinedConfiguration#append(Configuration)
	 */
	@SuppressWarnings("unchecked")
	public void loadProperties(URL url, boolean disableDelimeterParsing, boolean merge) {
		PropertiesConfiguration props;

		// load the required base props
		try {
			props = new PropertiesConfiguration();
			props.setDelimiterParsingDisabled(disableDelimeterParsing);
			props.load(url);
		}
		catch(ConfigurationException ce) {
			throw new RuntimeException("Unable to load base configuration: " + ce.getMessage(), ce);
		}

		if(merge) {
			for(Iterator<String> itr = props.getKeys(); itr.hasNext();) {
				String key = itr.next();
				root.setProperty(key, props.getProperty(key));
			}
		}
		else {
			root.append(props);
		}

		log.info("Properties loaded for: " + url.getPath());
	}

	private CombinedConfiguration safeGetRoot() {
		assert root != null;
		if(root.isEmpty()) {
			// attempt to load default config props..
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
