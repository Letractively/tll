/**
 * The Logic Lab
 * @author jpk
 * Jan 16, 2009
 * 
 * Builds test resources.
 */
 
 String srcResourceDir = project.basedir.toString() + "/src/test/resources/"
 String tgtResourceDir = project.basedir.toString() + "/target/test-classes/" 
 
 def config = com.tll.config.Config.instance();
 config.load(srcResourceDir);

 def property = config.asMap(null, null);
 
 def NL = System.getProperty("line.separator")
 
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
  
 // Adds persistence related properties to the built persistence.xml file
 def createBuildPersistenceXmlFile = {
 	println()
 	println('Creating persistence.xml file...')
 	
 	// pre-load the persistence specific props that are to be injected
 	def map = config.asMap('hibernate', 'hibernate.')
 			
 	// get the prefix and suffix strings
 	File f = new File(srcResourceDir, "persistence.xml")
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
 
 def copyMetaInfFiles = {
 	println()
 	println('Copying META-INF files...')
	
 	createBuildPersistenceXmlFile()
 	
	// remaining classes/META-INF files
	println()
	println('Copying aop.xml and orm.xml files...')
	ant.copy(todir: "${tgtResourceDir}/META-INF", preservelastmodified:true) {
		fileset(dir: srcResourceDir) {
			//include(name:"aop.xml")
			include(name:"orm.xml")
		}
	}
 }

 def copyClasspathResourceFiles = {
	println()
	println('Copying classpath resource files...')
	ant.copy(todir: tgtResourceDir, preservelastmodified:true, flatten:true) {
		fileset(dir: srcResourceDir) {
			include(name: 'config.properties')
			include(name: 'log4j.properties')
			include(name: 'ehcache.xml')
			include(name:'mock-entitiess.xml')
			//include(name:'ValidatorMessages.properties')
			include(name: '**/*.sql')
			include(name: '**/*.ddl')
		}
	}
 }
 
 init()
 copyMetaInfFiles()
 copyClasspathResourceFiles()
 