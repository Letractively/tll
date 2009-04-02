/**
 * The Logic Lab
 * @author jpk
 * Apr 1, 2009
 */
package com.tll

import com.tll.config.Config;
import com.tll.ConfigProcessor;

/**
 * ResourceProcessor - Readies resources for the project.
 * @author jpk
 */
public abstract class ResourceProcessor{

	/**
	 * Processes the project resources
	 */
	public static void process(def project) {
		// create the deploy ready config.properties file..
		if(project == null) throw new IllegalArgumentException('Null maven project ref.')
		String td = project.build.outputDirectory.toString()
		File tcfg = new File(td, Config.DEFAULT_CONFIG_PROPERTIES_FILE_NAME)
	    Config cfg = ConfigProcessor.merge(project.basedir.toString() + '/src/main/resources', project.properties.mode)
		//cfg.saveAsPropFile(tcfg, 'model.', 'model.')
		cfg.saveAsPropFile(tcfg)
	}

}
