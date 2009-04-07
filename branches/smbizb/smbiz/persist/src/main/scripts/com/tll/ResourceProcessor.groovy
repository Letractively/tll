/**
 * The Logic Lab
 * @author jpk
 * Apr 1, 2009
 */
package com.tll

import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.config.IConfigFilter;
import com.tll.ConfigProcessor;

class MockConfigFilter implements IConfigFilter {
	 public boolean accept(String keyName) {
		 return keyName.startsWith("model");
	 }
}

class OrmConfigFilter implements IConfigFilter {
	 public boolean accept(String keyName) {
		 return (keyName.startsWith("db") || keyName == 'model.entityAssembler.classname')
	 }
}

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
		String od = project.build.outputDirectory.toString()
	    Config cfg = ConfigProcessor.merge(project.basedir.toString() + '/src/main/resources', project.properties.mode)
	    
	    // create orm config prop file
		cfg.filter(new OrmConfigFilter()).saveAsPropFile(new File(od + '/orm', ConfigRef.DEFAULT_NAME))
	    
	    // create mock config prop file
    	cfg.filter(new MockConfigFilter()).saveAsPropFile(new File(od + '/mock', ConfigRef.DEFAULT_NAME))
	}
}
