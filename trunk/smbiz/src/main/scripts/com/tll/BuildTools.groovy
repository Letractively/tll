/**
 * The Logic Lab
 * @author jpk
 * Mar 28, 2009
 */
 
package com.tll;

import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.ConfigProcessor;
import com.tll.dao.jdbc.DbShellBuilder;

/**
 * BuildTools - Utility class for project building.
 * @author jpk
 */
public final class BuildTools {
	 
	static final int FLAG_ALL = 0;
	static final int FLAG_ORM = 1;
	static final int FLAG_MOCK = 1 << 1;
	static final int FLAG_SECURITY_ACEGI = 1 << 2;
	static final int FLAG_SECURITY_NONE = 1 << 3;
	 
	static final def NL = System.getProperty("line.separator")

	static final def regex_security_none = /(?s)<!-- START NO SECURITY -->(.*)<!-- END NO SECURITY -->/
	static final def regex_security_acegi = /(?s)<!-- START SECURITY ACEGI -->(.*)<!-- END SECURITY ACEGI -->/
	static final def regex_db_orm = /(?s)<!-- START DB ORM -->(.*)<!-- END DB ORM -->/
	static final def regex_db_mock = /(?s)<!-- START DB MOCK -->(.*)<!-- END DB MOCK -->/
	
	// all di module ref arrays of format: [name, flags]
	// where flags indicates the eligibility of inclusion into the 
	// target web.xml based on the loaded config state 
	static final def DI_MODULES_ALL = [
	                              ['com.tll.di.VelocityModule', FLAG_ALL],
	                              ['com.tll.di.ValidationModule', FLAG_ALL],
	                              ['com.tll.di.MailModule', FLAG_ALL],
	                              ['com.tll.di.RefDataModule', FLAG_ALL],
	                              ['com.tll.di.EmailExceptionHandlerModule', FLAG_ORM],
	                              ['com.tll.di.LogExceptionHandlerModule', FLAG_MOCK],
	                              ['com.tll.di.ModelModule', FLAG_ALL],
	                              ['com.tll.di.MockEntityFactoryModule', FLAG_MOCK],
	                              ['com.tll.di.EntityAssemblerModule', FLAG_ALL],
	                              ['com.tll.di.DbDialectModule', FLAG_ORM],
	                              ['com.tll.di.OrmDaoModule', FLAG_ORM],
	                              ['com.tll.di.MockDaoModule', FLAG_MOCK],
	                              ['com.tll.di.TransactionModule', FLAG_ORM],
	                              ['com.tll.di.EntityServiceFactoryModule', FLAG_ALL],
	                              ['com.tll.di.ClientPersistModule', FLAG_ALL],
	                              ['com.tll.di.ListingModule', FLAG_ALL],
	                              ['com.tll.di.SmbizAcegiModule', FLAG_SECURITY_ACEGI],
	                              ['com.tll.di.AppModule', FLAG_ALL],
	                             ]
	
	// all di handler (bootstrapper) refs
	static final def DI_HANDLERS_ALL = [
	                               ['com.tll.server.rpc.entity.PersistContextBootstrapper', FLAG_ALL],
	                               ['com.tll.server.rpc.listing.ListingContextBootstrapper', FLAG_ALL],
	                               ['com.tll.server.SecurityContextBootstrapper', FLAG_SECURITY_ACEGI],
	                               ['com.tll.server.AppContextBootstrapper', FLAG_ALL],
	                              ]

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
	
	/**
	 * maven project, and ant refs (via gmaven plugin)
	 */
	private def project, ant;
	
	/**
	 * The project base directory.
	 */
	private String basedir;
		
	/**
	 * The generated Config instance employed for the build.
	 */
	private Config config;
	
	/**
	 * The debug, dao mode and security mode flags.
	 */
	private boolean debug, isMock, isSecurity;
	
	/**
	 * Constructor
	 * @param project the maven project ref
	 * @param ant the maven ant ref
	 */
	public BuildTools(def project, def ant) {
		if(project == null || ant == null) {
			throw new IllegalArgumentException('Null project and/or ant ref(s).')
		}
		this.project = project;
		this.ant = ant;
		init();
	}
	
	/**
	 * Init routing called by constructor.
	 */
	private void init() {
		this.basedir = project.basedir.toString()
		// load the config
		String mode = project.properties.mode;
		String dir = basedir + "/src/main/resources";
		this.config = ConfigProcessor.merge(dir, mode, 'local')
		
		// add build specific 'debug' property
		config.addProperty('debug', project.properties.debug)
		
		// require that debug and environment config props exist
		if(config.getString('debug') == null) {
			throw new IllegalStateException("No debug property present.")
		}
		if(config.getString('environment') == null) {
			throw new IllegalStateException("No environment property present.")
		}
		
		this.debug = config.getBoolean('debug')
	
		// retain the dao mode
		String daoMode = project.properties.daoMode
		if(daoMode == null) {
			throw new IllegalStateException('No dao mode found.')
		}
		println "daoMode: ${daoMode}"
		this.isMock = (daoMode == 'mock')
		
		// retain the security mode
		String securityMode = project.properties.securityMode
		if(securityMode == null) {
			throw new IllegalStateException('No security mode found.')
		}
		println "securityMode: ${securityMode}"
		this.isSecurity = (securityMode == 'acegi')
	}
	
	/**
	 * Copies webapp resources to the target war dir.
	 */
	private void copyWebappResources() {
		println 'Copying webapp resources..'
		String sdir = basedir + '/src/main/webapp'
		String tdir = basedir + '/target/war'
		ant.mkdir(dir: tdir)
		ant.copy(todir: tdir) {
			fileset(dir: sdir) {
				exclude(name: '**/web.xml')
			}
		}
		println 'webapp resources copied.'
	}
	
	/**
	 * Generates the web.xml file based on the configuration/project state.
	 */
	private void generateWebXml() {
		println 'Generating web.xml..'
		
		Map props = [:];
		
		// add the di props
		int flags = 0;
		flags = flags | (isMock? FLAG_MOCK : FLAG_ORM);
		flags = flags | (isSecurity? FLAG_SECURITY_ACEGI : FLAG_SECURITY_NONE);
		def sb = new StringBuilder(1024)
		DI_MODULES_ALL.each { elm ->
			int flag = elm[1]
			if(flag == FLAG_ALL || ((flag & flags) == flag)) {
				println "-Module: ${elm[0]}"
				sb.append(NL)
				sb.append(elm[0])
			}
		}
		props.put('di.modules', sb.toString())
		sb.setLength(0)
		DI_HANDLERS_ALL.each { elm ->
			int flag = elm[1]
			if(flag == FLAG_ALL || ((flag & flags) == flag)) {
				println "-Bootstrapper: ${elm[0]}"
				sb.append(NL)
				sb.append(elm[0])
			}
		}
		props.put('di.handlers', sb.toString())
		
		// web client caching policy
		String tkn = debug? '' : '.js .css .gif .jpg .png'
		props.put('oneDayCacheFileExts', tkn)
		
		// read web.xml
		StringBuilder sbuf = new StringBuilder(3000)
		new File(basedir + '/src/main/webapp/WEB-INF', 'web.xml').eachLine{ line ->
			sbuf.append(rplProps(line, props))
		    sbuf.append(NL)
		}
		String s = sbuf.toString()
		
		// security/no security filtering
		if(isSecurity) {
			// remove regex_security_none
			s = s.replaceAll(regex_security_none, '')
			println 'NO SECURITY section filtered out'
		}
		else {
			// remove regex_security_acegi
			s = s.replaceAll(regex_security_acegi, '')
			println 'ACEGI SECURITY section filtered out'
		}
		
		// dao orm/mock filtering
		if(!isMock) {
			// remove mock
			s = s.replaceAll(regex_db_mock, '')
			println 'MOCK DAO section filtered out'
		}
		else {
			// remove orm
			s = s.replaceAll(regex_db_orm, '')
			println 'ORM DAO section filtered out'
		}
		
		s = s.replaceFirst(/(?s)<web-app>\s+/, '<web-app>' + NL + '\t')
		
		// create the resolved web.xml
		String dir = project.build.directory.toString() + '/war/WEB-INF'
		ant.mkdir(dir: dir)
		(new File(dir, 'web.xml')).write(s);
		println 'web.xml created'
	}
	
	/**
	 * Generates GWT Constants.properties file.
	 */
	private void generateGwtConstantsFile() {
	    println 'Generating GWT Constants.properties file..'
		String tgtDir = project.build.outputDirectory.toString() + '/com/tll/client'
	    ant.mkdir(dir: tgtDir)
	    def gwtProps = new HashMap();
	    gwtProps.put("appVersion", project.version)
	    def f = new File(tgtDir, 'Constants.properties')
	    f.createNewFile()
	    StringBuilder sbuf = new StringBuilder(1024)
	    gwtProps.each() {key, value -> 
	      sbuf.append(key)
	      sbuf.append(' ')
	      sbuf.append(value)
	      sbuf.append(NL)
	    }
	    f.write(sbuf.toString())
	    println f.getPath() + ' created'
	}
	
	/**
	 * Saves the generated config to disk that is deploy ready.
	 */
	private void saveConfig() {
	    // create aggregated config.properties file..
	    println 'Creating aggregated config.properties file..'
		String tgtDir = basedir + '/target/war/WEB-INF/classes'
		File f = new File(tgtDir, ConfigRef.DEFAULT_NAME)
	    config.saveAsPropFile(f)
	    println f.getPath() + ' created'
	}

	/**
	 * Stubs the app db if it doesn't exist.
	 */
	private void stubDbIfNecessary() {
		if(!isMock) {
			String tgtDir = project.build.outputDirectory.toString()
			File fSchema = new File(tgtDir, config.getString('db.resource.schema'))
			File fStub = new File(tgtDir, config.getString('db.resource.stub'))
			File fDelete = new File(tgtDir, config.getString('db.resource.delete'))
			def shell = DbShellBuilder.getDbShell(config, 
			  fSchema.toURI().toURL(), fStub.toURI().toURL(), fDelete.toURI().toURL());
			if(shell.create()) {
			  println('Stubbing db..')
			  shell.stub();
			  println('db stubbed')
			}
		}
    }
}
