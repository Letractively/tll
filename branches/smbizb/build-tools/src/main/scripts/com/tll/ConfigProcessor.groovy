/**
 * The Logic Lab
 * @author jpk
 * Mar 31, 2009
 */
 
package com.tll;

import com.tll.config.Config;

/**
 * ConfigProcessor - 
 * Ingests one or more config property files into a single com.tll.config.Config instance.  
 * @author jpk
 */
public class ConfigProcessor{

	/**
	 * Merges one or more config property files returning a single Config instance. 
	 * @param baseDir the dir in which the config property file(s) reside
	 * @param modifiers one or more optional config file name modifiers 
	 * 	  	  where, if specified, means: a config property file of name convention:
	 *        config-{modifier}.properties
	 * @return the config instance
	 */
	public static Config merge(String baseDir, String... modifiers) {
		if(baseDir == null) throw new IllegalArgumentException('No base dir specified.')
		 
		println "Merging config files in dir: ${baseDir} .."
		Config cfg = Config.instance();
		String fn
		File f
	
		// load root config file
		fn = Config.DEFAULT_CONFIG_PROPERTIES_FILE_NAME
		f = new File(baseDir, fn)
		if(f.isFile()) {
			println "Loading root config properties: ${fn}" 
			cfg.loadProperties(f.toURI().toURL(), true, false)
		}
		 
		// load overriding config prop files
		if(modifiers != null) {
			modifiers.each { mod -> 
				fn = "config-${mod}.properties";
				f = new File(baseDir, fn)
				if(f.isFile()) {
					println "Loading config properties: ${fn}" 
					cfg.loadProperties(f.toURI().toURL(), true, true)
				}
			}
		}

		return cfg;
	}
	 
	public static void mergeAndSave(String baseDir, String tgtDir, String... modifiers) {
		Config cfg = merge(baseDir, modifiers)
		println "Saving merged config.properties file to dir: ${tgtDir}"
    	cfg.saveAsPropFile(new File(tgtDir, Config.DEFAULT_CONFIG_PROPERTIES_FILE_NAME))
	}
}
