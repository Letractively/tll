/**
 * The Logic Lab
 * @author jpk
 * Jan 16, 2009
 * 
 * Processes resources for packaging.
 */

 String srcResourceDir = project.basedir.toString() + "/src/main/resources/"
 String tgtResourceDir = project.basedir.toString() + "/target/classes/" 
 
 def config = com.tll.config.Config.instance();
 config.load(srcResourceDir);
 
 def property = config.asMap(null, null);
 
 /**
  * Replaces all occurrences of ${prop.name} with the property value held in the provided property map
  * NOTE: No variable interpolation is performed.
  * @param str The string that is searched for property place-holders
  * @param props The property map
  * @return String containing resolved property values 
  */
 def rplProps = {String str, Map props -> 
 	String rval = str;
 	props.each { key, val -> rval = rval.replace('${' + key + '}', val) }
 	return rval;
 }
  
 def init = {
  	File f = new File(tgtResourceDir);
  	f.mkdir();
 }
  
 /**
  * Interpolates the variable placeholders in the JPA persistence.xml file 
  * copying the interpolated file to the target dir.
  */
 def interpolateJpaPersistenceFile = {
   	println()
   	println('Interpolating persistence.xml file...')
   	
   	// pre-load the persistence specific props that are to be injected
   	def map = config.asMap('hibernate', 'hibernate.')
   			
   	// get the prefix and suffix strings
   	File f = new File(srcResourceDir, "META-INF/persistence.xml")
   	StringBuilder sbuf = new StringBuilder()
   	f.eachLine{ String line -> 
   		// replace any occurrences of a property place-holder
   		sbuf.append(rplProps(line, property))
   		sbuf.append(NL)
   	}
   	
   	// write the file to the build dir
   	f = new File(tgtResourceDir, 'META-INF')
   	f.mkdir()
   	f = new File(tgtResourceDir, 'META-INF/persistence.xml')
   	f.delete()
   	f.write(sbuf.toString())
 }
 
 init()
 interpolateJpaPersistenceFile()	 
 