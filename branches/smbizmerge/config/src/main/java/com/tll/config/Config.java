package com.tll.config;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Collection;
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
	 * The default loading routine where a classpath resource named:
	 * {@link ConfigRef#DEFAULT_NAME} is sought.
	 * @return New {@link Config} instance.
	 */
	public static Config load() {
		return load(new ConfigRef());
	}

	/**
	 * Loads config resources returning a new {@link Config} instance.
	 * @param refs The required resource refs defining what and how to load
	 * @return New {@link Config} instance
	 */
	public static Config load(ConfigRef... refs) {
		if(refs == null || refs.length < 1) throw new IllegalArgumentException("No config refs specified.");
		Config c = new Config();
		for(ConfigRef ref : refs) {
			Collection<URL> urls = ref.urls;
			for(URL url : urls) {
				c.loadProperties(url, ref.disableDelimeterParsing, true);
			}
		}
		return c;
	}

	/**
	 * Implementation decoratee
	 */
	private final CombinedConfiguration root;

	/**
	 * Constructor - May be used directory usu. when we want to manually add
	 * config properties.
	 */
	public Config() {
		super();
		root = new CombinedConfiguration();
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
	private void loadProperties(URL url, boolean disableDelimeterParsing, boolean merge) {
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
			root.setDelimiterParsingDisabled(disableDelimeterParsing);
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

	@Override
	public void addProperty(String key, Object value) {
		root.addProperty(key, value);
	}

	@Override
	public void clear() {
		root.clear();
	}

	@Override
	public void clearProperty(String key) {
		root.clearProperty(key);
	}

	@Override
	public boolean containsKey(String key) {
		return root.containsKey(key);
	}

	@Override
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return root.getBigDecimal(key, defaultValue);
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		return root.getBigDecimal(key);
	}

	@Override
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return root.getBigInteger(key, defaultValue);
	}

	@Override
	public BigInteger getBigInteger(String key) {
		return root.getBigInteger(key);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return root.getBoolean(key, defaultValue);
	}

	@Override
	public Boolean getBoolean(String key, Boolean defaultValue) {
		return root.getBoolean(key, defaultValue);
	}

	@Override
	public boolean getBoolean(String key) {
		return root.getBoolean(key);
	}

	@Override
	public byte getByte(String key, byte defaultValue) {
		return root.getByte(key, defaultValue);
	}

	@Override
	public Byte getByte(String key, Byte defaultValue) {
		return root.getByte(key, defaultValue);
	}

	@Override
	public byte getByte(String key) {
		return root.getByte(key);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return root.getDouble(key, defaultValue);
	}

	@Override
	public Double getDouble(String key, Double defaultValue) {
		return root.getDouble(key, defaultValue);
	}

	@Override
	public double getDouble(String key) {
		return root.getDouble(key);
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return root.getFloat(key, defaultValue);
	}

	@Override
	public Float getFloat(String key, Float defaultValue) {
		return root.getFloat(key, defaultValue);
	}

	@Override
	public float getFloat(String key) {
		return root.getFloat(key);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return root.getInt(key, defaultValue);
	}

	@Override
	public int getInt(String key) {
		return root.getInt(key);
	}

	@Override
	public Integer getInteger(String key, Integer defaultValue) {
		return root.getInteger(key, defaultValue);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterator<String> getKeys() {
		return root.getKeys();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterator<String> getKeys(String prefix) {
		return root.getKeys(prefix);
	}

	@Override
	@SuppressWarnings({
		"rawtypes"
	})
	public List<?> getList(String key, List defaultValue) {
		return root.getList(key, defaultValue);
	}

	@Override
	public List<?> getList(String key) {
		return root.getList(key);
	}

	@Override
	public long getLong(String key, long defaultValue) {
		return root.getLong(key, defaultValue);
	}

	@Override
	public Long getLong(String key, Long defaultValue) {
		return root.getLong(key, defaultValue);
	}

	@Override
	public long getLong(String key) {
		return root.getLong(key);
	}

	@Override
	public Properties getProperties(String key) {
		return root.getProperties(key);
	}

	@Override
	public Object getProperty(String key) {
		return root.getProperty(key);
	}

	@Override
	public short getShort(String key, short defaultValue) {
		return root.getShort(key, defaultValue);
	}

	@Override
	public Short getShort(String key, Short defaultValue) {
		return root.getShort(key, defaultValue);
	}

	@Override
	public short getShort(String key) {
		return root.getShort(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		return root.getString(key, defaultValue);
	}

	@Override
	public String getString(String key) {
		return root.getString(key);
	}

	@Override
	public String[] getStringArray(String key) {
		return root.getStringArray(key);
	}

	@Override
	public boolean isEmpty() {
		return root.isEmpty();
	}

	@Override
	public void setProperty(String key, Object value) {
		root.setProperty(key, value);
	}

	@Override
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
			Object pval = sub.getProperty(key);
			String spval = pval == null ? "" : pval.toString();
			pc.addProperty(prependToken == null ? key : prependToken + key, spval);
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
