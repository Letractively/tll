package com.tll.config;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.ReloadingStrategy;

/**
 * Config - Configuration store that loads properties from one or more property
 * files.
 * @author jpk
 */
public final class Config implements FileConfiguration {

	// private static final Log log = LogFactory.getLog(Config.class);

	/**
	 * The default config properties file name.
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
	private final PropertiesConfiguration root;

	/**
	 * Constructor
	 * @throws RuntimeException When the configuration is not successfully loaded.
	 */
	private Config() {
		super();
		root = new PropertiesConfiguration();
		root.setDelimiterParsingDisabled(true);
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
	public Iterator<?> getKeys() {
		return root.getKeys();
	}

	@Override
	public Iterator<?> getKeys(String prefix) {
		return root.getKeys(prefix);
	}

	@SuppressWarnings("unchecked")
	@Override
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

	@Override
	public String getBasePath() {
		return root.getBasePath();
	}

	@Override
	public String getEncoding() {
		return root.getEncoding();
	}

	@Override
	public File getFile() {
		return root.getFile();
	}

	@Override
	public String getFileName() {
		return root.getFileName();
	}

	@Override
	public ReloadingStrategy getReloadingStrategy() {
		return root.getReloadingStrategy();
	}

	@Override
	public URL getURL() {
		return root.getURL();
	}

	@Override
	public boolean isAutoSave() {
		return root.isAutoSave();
	}

	/**
	 * Attempts to loads config properties from a properties file at the root of
	 * the classpath named {@link #DEFAULT_CONFIG_PROPERTIES_FILE_NAME}.
	 * @throws ConfigurationException
	 */
	public void loadDefault() throws ConfigurationException {
		root.load(DEFAULT_CONFIG_PROPERTIES_FILE_NAME);
	}

	@Override
	public void load() throws ConfigurationException {
		if(getFileName() == null) {
			loadDefault();
		}
		else {
			root.load();
		}
	}

	@Override
	public void load(File file) throws ConfigurationException {
		root.load(file);
	}

	@Override
	public void load(InputStream in, String encoding) throws ConfigurationException {
		root.load(in, encoding);
	}

	@Override
	public void load(InputStream in) throws ConfigurationException {
		root.load(in);
	}

	@Override
	public void load(Reader in) throws ConfigurationException {
		root.load(in);
	}

	@Override
	public void load(String fileName) throws ConfigurationException {
		root.load(fileName);
	}

	@Override
	public void load(URL url) throws ConfigurationException {
		root.load(url);
	}

	@Override
	public void reload() {
		root.reload();
	}

	@Override
	public void save() throws ConfigurationException {
		root.save();
	}

	@Override
	public void save(File file) throws ConfigurationException {
		root.save(file);
	}

	@Override
	public void save(OutputStream out, String encoding) throws ConfigurationException {
		root.save(out, encoding);
	}

	@Override
	public void save(OutputStream out) throws ConfigurationException {
		root.save(out);
	}

	@Override
	public void save(String fileName) throws ConfigurationException {
		root.save(fileName);
	}

	@Override
	public void save(URL url) throws ConfigurationException {
		root.save(url);
	}

	@Override
	public void save(Writer out) throws ConfigurationException {
		root.save(out);
	}

	@Override
	public void setAutoSave(boolean autoSave) {
		root.setAutoSave(autoSave);
	}

	@Override
	public void setBasePath(String basePath) {
		root.setBasePath(basePath);
	}

	@Override
	public void setEncoding(String encoding) {
		root.setEncoding(encoding);
	}

	@Override
	public void setFile(File file) {
		root.setFile(file);
	}

	@Override
	public void setFileName(String fileName) {
		root.setFileName(fileName);
	}

	@Override
	public void setReloadingStrategy(ReloadingStrategy strategy) {
		root.setReloadingStrategy(strategy);
	}

	@Override
	public void setURL(URL url) {
		root.setURL(url);
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
		Configuration sub = prefix == null ? root : subset(prefix);
		PropertiesConfiguration pc = new PropertiesConfiguration();
		for(Iterator<String> itr = sub.getKeys(); itr.hasNext();) {
			String key = itr.next();
			pc.addProperty(prependToken == null ? key : prependToken + key, sub.getString(key));
		}
		return pc;
	}

	/**
	 * Filters this configuration based on a set of config keys.
	 * @param keyProvider
	 * @return The filtered configuration as a {@link PropertiesConfiguration}.
	 */
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
