/**
 * The Logic Lab
 * @author jpk
 * Mar 28, 2009
 */
 
package com.tll;

import com.google.inject.Guice;

import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.ConfigProcessor;
import com.tll.dao.IDbShell;
import com.tll.di.SmbizDb4oDaoModule;
import com.tll.di.test.Db4oDbShellModule;
import com.tll.di.SmbizModelBuildModule;
import com.tll.model.egraph.IEntityGraphPopulator;
import com.tll.di.EGraphModule;

// gmaven does't pick up test classes in the maven classpath 
// so we need to re-define the smbiz test deps here tragically

import com.tll.di.SmbizEGraphModule;
import com.tll.model.SmbizEntityGraphBuilder;
import com.tll.model.*;
import com.google.inject.Inject;
import com.tll.model.egraph.AbstractEntityGraphPopulator;
import com.tll.model.egraph.EntityBeanFactory;
import com.tll.util.EnumUtil;

/**
 * BuildTools - Utility class for smbiz project building.
 * @author jpk
 */
public final class BuildTools {
	 
	/**
	 * Process resources necessary for war assembly. 
	 */
	static void processWarResources(def project, def ant) {
		BuildTools b = new BuildTools(project, ant);
	    b.saveConfig();
	    b.generateGwtConstantsFile();
	    b.generateWebXml();
	    b.copyWebappResources();
      	b.stubDbIfNecessary()
	}
	
	static final String DEFAULT_STAGE = 'debug'
	static final String DEFAULT_DAO_IMPL = 'db40'
	static final String DEFAULT_SECURITY_IMPL = 'none'
	 
	static final int FLAG_ALL = 0;
	static final int FLAG_DB_DB4O = 1;
	static final int FLAG_DB_JDO = 1 << 1;
	static final int FLAG_SECURITY_ACEGI = 1 << 2;
	static final int FLAG_SECURITY_NONE = 1 << 3;
	 
	static final def NL = System.getProperty("line.separator")

	static final def regex_db_db4o = /(?s)<!-- START DB DB4O -->(.*)<!-- END DB DB4O -->/
	static final def regex_db_jdo = /(?s)<!-- START DB JDO -->(.*)<!-- END DB JDO -->/
	static final def regex_security_acegi = /(?s)<!-- START SECURITY ACEGI -->(.*)<!-- END SECURITY ACEGI -->/
	static final def regex_security_none = /(?s)<!-- START NO SECURITY -->(.*)<!-- END NO SECURITY -->/
	
	// all di module ref arrays of format: [name, flags]
	// where flags indicates the eligibility of inclusion into the 
	// target web.xml based on the loaded config state 
	static final def DI_MODULES_ALL = [
	  ['com.tll.di.VelocityModule', FLAG_ALL],
	  ['com.tll.di.MailModule', FLAG_ALL],
	  ['com.tll.di.RefDataModule', FLAG_ALL],
	  ['com.tll.di.EmailExceptionHandlerModule', FLAG_DB_JDO],
	  ['com.tll.di.LogExceptionHandlerModule', FLAG_DB_DB4O],
		['com.tll.di.ModelModule', FLAG_ALL],
		['com.tll.di.SmbizModelBuildModule', FLAG_ALL],
	  ['com.tll.di.SmbizCacheModule', FLAG_ALL],
	  ['com.tll.di.SmbizDb4oDaoModule', FLAG_DB_DB4O],
	  ['com.tll.di.SmbizEntityServiceFactoryModule', FLAG_ALL],
	  ['com.tll.di.SmbizMarshalModule', FLAG_ALL],
	  ['com.tll.di.SmbizClientPersistModule', FLAG_ALL],
	  ['com.tll.di.SmbizListingModule', FLAG_ALL],
	  ['com.tll.di.SmbizAcegiModule', FLAG_SECURITY_ACEGI],
	  ['com.tll.di.AppModule', FLAG_ALL],
	]
	
	// all di handler (bootstrapper) refs
	static final def DI_HANDLERS_ALL = [
	  ['com.tll.server.Db4oBootstrapper', FLAG_DB_DB4O],
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
	 * The project properties.
	 */
	private String stage, daoImpl, securityImpl;
	
	private int daoImplFlag, securityImplFlag;
	
	private int flags;
	
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
		this.config = ConfigProcessor.merge(basedir + "/src/main/resources", project.properties.mode, 'local')
		
		// obtain the stage
		this.stage = project.properties.stage
	
		// obtain the dao impl
		this.daoImpl = project.properties.daoImpl
		if(daoImpl == null) {
			daoImpl = DEFAULT_DAO_IMPL
			println 'No dao impl specified in project properties reverted to default' 
		}
		println "daoImpl: ${daoImpl}"
		switch(daoImpl) {
			case 'db4o': daoImplFlag = FLAG_DB_DB4O; break
			case 'jdo': daoImplFlag = FLAG_DB_JDO; break
			default: throw new IllegalArgumentException("Unhandled dao impl: ${daoImpl}")
		}
		
		// obtain the security impl
		this.securityImpl = project.properties.securityImpl
		if(securityImpl == null) {
			securityImpl = DEFAULT_SECURITY_IMPL
			println 'No security impl specified in project properties reverted to default' 
		}
		println "securityImpl: ${securityImpl}"
		switch(securityImpl) {
			case 'acegi': securityImplFlag = FLAG_SECURITY_ACEGI; break
			case 'none': securityImplFlag = FLAG_SECURITY_NONE; break
			default: throw new IllegalArgumentException("Unhandled security impl: ${securityImpl}")
		}

		// set 'debug' property to build config
		config.setProperty('stage', this.stage)
		
		// set the flags
		this.flags = 0 | daoImplFlag | securityImplFlag;
	}
	
	/**
	 * Copies webapp resources to the target war dir.
	 */
	public void copyWebappResources() {
		println 'Copying webapp resources..'
		String sdir = basedir + '/src/main/webapp'
		String tdir = basedir + '/target/war'
		ant.mkdir(dir: tdir)
		ant.copy(todir: tdir, preservelastmodified:true) {
			fileset(dir: sdir) {
				exclude(name: '**/web.xml')
			}
		}
		println 'webapp resources copied.'
	}
	
	/**
	 * Generates the web.xml file based on the configuration/project state.
	 */
	public void generateWebXml() {
		println 'Generating web.xml..'
		
		Map props = [:];
		
		// add the di props
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
		String tkn = (this.stage == 'debug')? '' : '.js .css .gif .jpg .png'
		props.put('oneDayCacheFileExts', tkn)
		
		// read web.xml
		StringBuilder sbuf = new StringBuilder(3000)
		new File(basedir + '/src/main/webapp/WEB-INF', 'web.xml').eachLine{ line ->
			sbuf.append(rplProps(line, props))
		    sbuf.append(NL)
		}
		String s = sbuf.toString()
		
		// dao impl filtering
		println "  applying dao impl: ${daoImpl}"
		switch(daoImpl) {
			case 'db4o':
				s = s.replaceAll(regex_db_jdo, '')
				break;
			case 'jdo':
				s = s.replaceAll(regex_db_db40, '')
				break
		}
		
		// security impl filtering
		println "  applying security impl: ${securityImpl}"
		switch(securityImpl) {
			case 'none':
			s = s.replaceAll(regex_security_acegi, '')
			break
			case 'acegi':
				s = s.replaceAll(regex_security_none, '')
				break
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
	public void generateGwtConstantsFile() {
	    println 'Generating GWT Constants.properties file..'
		String tgtDir = project.build.outputDirectory.toString() + '/com/tll/client'
	    ant.mkdir(dir: tgtDir)
	    def gwtProps = new HashMap();
	    gwtProps.put("appVersion", project.version)
	    def f = new File(tgtDir, 'SmbizConstants.properties')
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
	public void saveConfig() {
	    // create aggregated config.properties file..
	    println 'Creating consolidated config.properties file..'
		//String tgtDir = basedir + '/target/war/WEB-INF/classes'
		String tgtDir = project.build.outputDirectory.toString()
		File f = new File(tgtDir, ConfigRef.DEFAULT_NAME)
	    config.saveAsPropFile(f)
	    println f.getPath() + ' created'
	}

	/**
	 * Stubs the app db if it doesn't exist.
	 */
	private void stubDbIfNecessary() {
		
		 // get a db shell instance
		 IDbShell dbShell = null;
		 switch(daoImpl) {
			case 'db4o':
				Config cfg = new Config();
				String db4oFilepath = project.build.outputDirectory.toString() + '/' + config.getString('db.db4o.filename')
				File f = new File(db4oFilepath);
				if(!f.exists()) {
					println 'Stubbing smbiz db4o db..'
					cfg.addProperty('db.db4o.filename', db4oFilepath);
					cfg.setProperty('db.db4o.springTransactions', false)
					dbShell = Guice.createInjector(new SmbizModelModule(), 
							new SmbizEGraphModule(), 
							new SmbizDb4oDaoModule(cfg), new Db4oDbShellModule()).getInstance(IDbShell.class);
					dbShell.restub();
				}
				break
			case 'jdo':
				// TODO implement
				throw new UnsupportedOperationException()
				/*
				String tgtDir = project.build.outputDirectory.toString()
				File fSchema = new File(tgtDir, config.getString('db.resource.schema'))
				File fStub = new File(tgtDir, config.getString('db.resource.stub'))
				File fDelete = new File(tgtDir, config.getString('db.resource.delete'))
				def shell = DbShellBuilder.getDbShell(config, 
				  fSchema.toURI().toURL(), fStub.toURI().toURL(), fDelete.toURI().toURL());
				*/
				break
		}
    }
}
