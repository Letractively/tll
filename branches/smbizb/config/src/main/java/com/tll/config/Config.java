package com.tll.config;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Enumeration;
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
	 * The default config properties file name.
	 */
	public static final String DEFAULT_FILE_NAME = "config.properties";

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
	 * Attempts to load properties from {@link #DEFAULT_FILE_NAME} at the root of
	 * the classpath with <em>NO</em> delimeter parsing.
	 * @see #load(boolean, boolean, String)
	 */
	public void load() {
		load(true, false, DEFAULT_FILE_NAME);
	}

	/**
	 * Loads all found config property resources named:
	 * {@link Config#DEFAULT_FILE_NAME} at the root of the classpath.
	 * @throws IOException When an io related error occurs.
	 */
	public void loadAll() throws IOException {
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(DEFAULT_FILE_NAME);
		while(urls != null && urls.hasMoreElements()) {
			URL url = urls.nextElement();
			loadProperties(url, true, true);
		}
	}

	/**
	 * Attempts to load properties from {@link #DEFAULT_FILE_NAME} at the root of
	 * the classpath.
	 * @param disableDelimeterParsing
	 * @param merge Replace existing properties with those of the same name at the
	 *        given url?
	 * @param configResourceName the name of the config property resource
	 * @see #loadProperties(URL, boolean, boolean)
	 */
	public void load(boolean disableDelimeterParsing, boolean merge, String configResourceName) {
		loadProperties((Thread.currentThread().getContextClassLoader()).getResource(configResourceName),
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
			throw new RuntimeException("Unable to load properties '" + url + "': " + ce.getMessage(), ce);
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

	@SuppressWarnings("unchecked")
	public Iterator<String> getKeys() {
		return root.getKeys();
	}

	@SuppressWarnings("unchecked")
	public Iterator<String> getKeys(String prefix) {
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
	 * Filters the held properties returning a new instance containing only the
	 * filtered properties.
	 * @param filter the required filter to employ
	 * @return a new {@link Config} containing only the filtered properties.
	 */
	@SuppressWarnings("unchecked")
	public Config filter(IConfigFilter filter) {
		if(filter == null) throw new IllegalArgumentException("A filter must be specified.");
		Config cfg = new Config();
		for(Iterator<String> itr = root.getKeys(); itr.hasNext();) {
			String key = itr.next();
			if(filter.accept(key)) {
				cfg.addProperty(key, this.getProperty(key));
			}
		}
		return cfg;
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
	 * @param prefix the prefix of the keys for the subset. May be
	 *        <code>null</code> in which case all properties are considered.
	 * @return java.util.Properties instance
	 */
	public Properties asProperties(String prefix) {
		return ConfigurationConverter.getProperties(subset(prefix));
	}
}
