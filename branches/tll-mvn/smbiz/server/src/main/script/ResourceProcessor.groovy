/**
 * The Logic Lab
 * @author jpk
 * Jan 16, 2009
 * 
 * Processes server side resources for packaging.
 */

 def config = com.tll.config.Config.instance();
 def property = config.asMap(null, null);
 
 def createDeployConfigPropsFile = {
 	println()
 	println('Creating deploy config.properties file...')
	File f = new File("${property.'project.dir.build.web'}/WEB-INF/classes/config.properties")
	//ConfigKeys cks = ConfigKeys.getClass().newInstance();
	//config.saveAsPropFile(f, cks.getConfigKeys())
	config.saveAsPropFile(f, null, null)
 }
 
 //creates a log4j.properties file in the deploy WEB-INF/classes dir
 def createBuildLog4JPropsFile = {
 	println()
 	println('Creating build log4j.properties file...')
 	File f = new File(property.'project.dir.build.web' + '/WEB-INF/classes/log4j.properties')
 	f.delete()
 	config.saveAsPropFile(f, 'log4j', 'log4j.')
 }

 // Adds persistence related properties to the built persistence.xml file
 def createBuildPersistenceXmlFile = {
 	println()
 	println('Creating build persistence.xml file...')
 	
 	// pre-load the persistence specific props that are to be injected
 	def map = config.asMap('hibernate', 'hibernate.')
 			
 	// get the prefix and suffix strings
 	File f = new File("${property.'project.server.dir'}/resources/persistence.xml")
 	StringBuilder sbuf = new StringBuilder()
 	f.eachLine{ String line -> 
 		// replace any occurrences of a property place-holder
 		sbuf.append(rplProps(line, property))
 		sbuf.append(NL)
 	}
 	
 	// write the file to the build dir
 	f = new File("${property.'project.dir.build.web'}/WEB-INF/classes/META-INF/persistence.xml")
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
	ant.copy(todir: "${property.'project.dir.build.web'}/WEB-INF/classes/META-INF", preservelastmodified:true) {
		fileset(dir: "${property.'project.server.dir'}/resources") {
			include(name:"aop.xml")
			include(name:"orm.xml")
		}
	}
 }

 def copyClasspathResourceFiles = {
	println()
	println('Copying remaining classpath resource files...')
	ant.copy(todir: "${property.'project.dir.build.web'}/WEB-INF/classes", preservelastmodified:true, flatten:true) {
		fileset(dir: "${property.'project.server.dir'}/resources") {
			include(name:"ehcache.xml")
			include(name:"mock-entitiess.xml")
			include(name:"ValidatorMessages.properties")
			include(name:"/db/*.sql")
			include(name:"/db/*.ddl")
		}
	}
 }
 
 def buildDeployWebXml = {
	println()
	println('Building deploy web.xml file...')
	String webXmlPath = (property.'project.server.dir' + '/resources/').replace('\\', '/') + 'web.xml'
	String webXmlDeployPath = (property.'project.dir.build.web').replace('\\', '/') + '/WEB-INF/web.xml'
	
	def infile = new File(webXmlPath)
	StringBuilder sbuf = new StringBuilder(3000)
	infile.eachLine{ line -> 
		sbuf.append(rplProps(line, property))
		sbuf.append(NL)
	}
	def fWebXmlDeploy = new File(webXmlDeployPath)
	
	// Extract GWTShellServlet && servlet-context from web.xml before copying
	def regex_deploy = /(?s)<!-- START Hosted Mode Add-ons -->(.*)<!-- END Hosted Mode Add-ons -->/
	fWebXmlDeploy.write(sbuf.toString().replaceAll(regex_deploy, '').replaceFirst(/(?s)<web-app>\s+/, "<web-app>" + NL + '\t'))
 }
 
 def buildGwtConstantsFile = {
	println()
	println('Building GWT Constants.properties file...')

	def gwtProps = new HashMap();
	gwtProps.put("appVersion", property.'app.version')
	gwtProps.put("environment", property.'environment')
	gwtProps.put("debug", property.'debug')

	String path = property.'project.client.dir' + '/src/' + property.'gwt.path.constants.file'
	def f = new File(path)
	f.createNewFile()
	StringBuilder sbuf = new StringBuilder(1000)
	gwtProps.each() {key, value -> 
		sbuf.append(key)
		sbuf.append(' ')
		sbuf.append(value)
		sbuf.append(NL)
	}
	f.write(sbuf.toString())
 }
 
 createDeployConfigPropsFile()	 
 createBuildLog4JPropsFile()
 copyMetaInfFiles()
 copyClasspathResourceFiles()
 buildDeployWebXml()
 buildGwtConstantsFile()
 