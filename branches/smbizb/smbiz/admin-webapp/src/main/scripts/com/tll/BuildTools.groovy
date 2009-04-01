/**
 * The Logic Lab
 * @author jpk
 * Mar 28, 2009
 */
 
package com.tll;

import com.tll.config.Config;
import com.tll.dao.mock.jdbc.DbShellBuilder;

/**
 * BuildTools - Utility class for project building.
 * @author jpk
 */
public final class BuildTools {
	 
	/**
	 * Single public point of contact.
	 * @param project
	 * @param ant
	 */
	public static void process(def project, def ant) {
		BuildTools b = new BuildTools(project, ant);
		b.generateWebXml();
		b.generateGwtConstantsFile();
		b.saveConfig()
		b.stubTestDbIfNecessary()
	}
	
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
	                              ['com.tll.di.MailModule', FLAG_ALL],
	                              ['com.tll.di.RefDataModule', FLAG_ALL],
	                              ['com.tll.di.ModelModule', FLAG_ALL],
	                              ['com.tll.di.MockEntityFactoryModule', FLAG_MOCK],
	                              ['com.tll.di.EntityAssemblerModule', FLAG_ALL],
	                              ['com.tll.di.OrmDaoModule', FLAG_ORM],
	                              ['com.tll.di.MockDaoModule', FLAG_MOCK],
	                              ['com.tll.di.TransactionModule', FLAG_ORM],
	                              ['com.tll.di.EntityServiceFactoryModule', FLAG_ALL],
	                              ['com.tll.di.AcegiModule', FLAG_SECURITY_ACEGI]
	                             ]
	
	// all di handler (bootstrapper) refs
	static final def DI_HANDLERS_ALL = [
	                               ['com.tll.server.rpc.entity.MEntityServiceBootstrapper', FLAG_ALL],
	                               ['com.tll.server.SecurityContextBootstrapper', FLAG_SECURITY_ACEGI],
	                               ['com.tll.server.AppContextBootstrapper', FLAG_ALL]
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
	 * The generated Config instance employed for the build.
	 */
	private Config config;
	
	/**
	 * The dao mode and security mode tokens obtained from the loaded config.
	 */
	private String daoMode, secMode;
	
	/**
	 * The generated di map containing:
	 * 1. Key 'di.modules': the resolved do module refs
	 * 2. Key 'di.handlers': the resolved di handler refs
	 */
	private Map diMap = [:];
	
	/**
	 * Constructor
	 * @param project the maven project ref
	 * @param ant the maven ant ref
	 */
	private BuildTools(def project, def ant) {
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
		// load the config
		String mode = project.properties.mode;
		String baseDir = project.basedir.toString() + "/src/main/resources";
		this.config = ConfigProcessor.process(baseDir, mode, 'local')
		
		// retain the dao mode and security mode
		this.daoMode = config.getString('db.dao.mode')
		this.secMode = config.getString('security.mode')
		println "dao mode: ${daoMode}, security mode: ${secMode}"

		// generate the di map
		int flags = 0;
		switch(daoMode) {
			case 'ORM':
				flags = flags | FLAG_ORM;
				break;
			case 'MOCK':
				flags = flags | FLAG_MOCK;
				break;
			default:
				throw new IllegalStateException('Unhandled dao mode: ' + daoMode)
		}
		switch(secMode) {
			case 'ACEGI':
				flags = flags | FLAG_SECURITY_ACEGI;
				break;
			case 'NONE':
				flags = flags | FLAG_SECURITY_NONE;
				break;
			default:
				throw new IllegalStateException('Unhandled security mode: ' + secMode)
		}
		def sb = new StringBuilder(1024)
		DI_MODULES_ALL.each { elm ->
			int flag = elm[1]
			if(flag == FLAG_ALL || ((flag & flags) == flag)) {
				println "-Module: ${elm[0]} will be employed"
				sb.append(NL)
				sb.append(elm[0])
			}
		}
		diMap.put('di.modules', sb.toString())
		sb.setLength(0)
		DI_HANDLERS_ALL.each { elm ->
			int flag = elm[1]
			if(flag == FLAG_ALL || ((flag & flags) == flag)) {
				println "-Bootstrapper: ${elm[0]} will be employed"
				sb.append(NL)
				sb.append(elm[0])
			}
		}
		diMap.put('di.handlers', sb.toString())
	}
	 
	/**
	 * Generates the web.xml file based on the configuration/project state.
	 */
	private void generateWebXml() {
		println 'Generating web.xml..'
		StringBuilder sbuf = new StringBuilder(3000)
		new File(project.basedir.toString() + '/src/main/webapp/WEB-INF', 'web.xml').eachLine{ line ->
			sbuf.append(rplProps(line, diMap))
		    sbuf.append(NL)
		}
		String s = sbuf.toString()
		
		// security/no security filtering
		if(secMode == 'ACEGI') {
			// remove regex_security_none
			s = s.replaceAll(regex_security_none, '')
			println 'NO SECURITY section filtered out'
		}
		else if(secMode == 'NONE') {
			// remove regex_security_acegi
			s = s.replaceAll(regex_security_acegi, '')
			println 'ACEGI SECURITY section filtered out'
		}
		
		// dao orm/mock filtering
		if(daoMode == 'ORM') {
			// remove mock
			s = s.replaceAll(regex_db_mock, '')
			println 'MOCK DAO section filtered out'
		}
		else if(daoMode == 'MOCK') {
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
	    println 'GWT Constants.properties file created'
	}
	
	/**
	 * Saves the generated config to disk that is deploy ready.
	 */
	private void saveConfig() {
	    // create aggregated config.properties file..
	    println 'Creating aggregated config.properties file..'
		String tgtDir = project.build.outputDirectory.toString()
	    config.saveAsPropFile(new File(tgtDir + '/config.properties'))
	    println 'config.properties file created'
	}

	/**
	 * Stubs the app test db.
	 */
	private void stubTestDbIfNecessary() {
		if(config.getString('db.dao.mode') == 'ORM') {
			String tgtDir = project.build.outputDirectory.toString()
			File fSchema = new File(tgtDir, config.getString('db.resource.schema'))
			File fStub = new File(tgtDir, config.getString('db.resource.stub'))
			File fDelete = new File(tgtDir, config.getString('db.resource.delete'))
			def shell = DbShellBuilder.getDbShell(config, 
			  fSchema.toURI().toURL(), fStub.toURI().toURL(), fDelete.toURI().toURL());
			if(shell.create()) {
			  println('Stubbing test db..')
			  shell.stub();
			  println('Test db stubbed..')
			}
		}
    }
}
