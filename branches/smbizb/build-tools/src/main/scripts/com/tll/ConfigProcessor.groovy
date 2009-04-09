/**
 * The Logic Lab
 * @author jpk
 * Mar 31, 2009
 */
 
package com.tll;

import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.config.IConfigFilter;

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
	 *        config-{modifier}.properties will be loaded and merged with the base config 
	 * @return the merged config instance
	 */
	public static Config merge(String baseDir, String... modifiers) {
		if(baseDir == null) throw new IllegalArgumentException('No base dir specified.')
		 
		println "Merging config files in dir: ${baseDir} .."
		File f;
		URL url;
		def refs = [];
	
		// root config file
		url = new File(baseDir, ConfigRef.DEFAULT_NAME).toURI().toURL()
		refs.add(new ConfigRef(url))
		 
		// overriding config files
		if(modifiers != null) {
			modifiers.each { mod -> 
				f = new File(baseDir, "config-${mod}.properties")
				if(f.isFile()) {
					url = f.toURI().toURL()
					refs.add(new ConfigRef(url))
				}
			}
		}

		ConfigRef[] arr = refs.toArray(new ConfigRef[refs.size()]);
		Config c = Config.load(arr);
		return c;
	}
	 
	/**
	 * Merges multiple config property files, 
	 * optionally filters out config properties 
	 * then save resultant config to disk.
	 * @param baseDir
	 * @param tgtDir
	 * @param filter the optional config filter. May be <code>null</code>.
	 * @param modifiers optional modifier tokens for loading additional config property files
	 */
	public static void mergeAndSave(String baseDir, String tgtDir, IConfigFilter filter, String... modifiers) {
		Config cfg = merge(baseDir, modifiers)
		if(filter != null) {
			// filter first
			cfg = cfg.filter(filter)
			println 'config filtered'
		}
		println "Saving merged config.properties file to dir: ${tgtDir}"
		cfg.saveAsPropFile(new File(tgtDir, ConfigRef.DEFAULT_NAME))
	}
}
