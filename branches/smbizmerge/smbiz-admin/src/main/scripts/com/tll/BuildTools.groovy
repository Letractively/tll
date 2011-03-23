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
import com.tll.dao.db4o.SmbizDb4oDaoModule;
import com.tll.dao.IDbShell;
import com.tll.dao.db4o.test.Db4oDbShellModule;
import com.tll.model.egraph.IEntityGraphPopulator;
import com.tll.model.egraph.EGraphModule;
import com.tll.model.SmbizEGraphModule;
import com.tll.model.SmbizEntityGraphBuilder;

/**
 * Utility class for smbiz project building.
 * @author jpk
 */
public final class BuildTools {

	/**
	 * Process resources necessary for war assembly. 
	 */
	static void processWarResources(def project, def ant) {
		BuildTools b = new BuildTools(project, ant);
		b.createDeployConfigFile();
		b.createDeployWebXmlFile();
		b.copyWebappResources();
		//b.stubDbIfNecessary()
	}

	static final String DEFAULT_STAGE = 'debug'
	static final String DEFAULT_DAO_IMPL = 'db4o'
	static final String DEFAULT_SECURITY_IMPL = 'none'

	static final def NL = System.getProperty("line.separator")

	static final def regex_db_db4o = /(?s)<!-- START DB DB4O -->(.*)<!-- END DB DB4O -->/
	static final def regex_db_jdo = /(?s)<!-- START DB JDO -->(.*)<!-- END DB JDO -->/
	static final def regex_security_acegi = /(?s)<!-- START SECURITY ACEGI -->(.*)<!-- END SECURITY ACEGI -->/
	static final def regex_security_none = /(?s)<!-- START NO SECURITY -->(.*)<!-- END NO SECURITY -->/

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
	 * The project base and target webapp directory.
	 */
	private String basedir, webappDir;

	/**
	 * The generated Config instance employed for the build.
	 */
	private Config config;

	/**
	 * The project properties.
	 */
	private String stage, daoImpl, securityImpl;

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
		this.webappDir = project.properties.webappDirectory
		this.stage = project.properties.stage

		// obtain the dao impl
		this.daoImpl = project.properties.daoImpl
		if(daoImpl == null) {
			daoImpl = DEFAULT_DAO_IMPL
			println 'No dao impl specified in project properties reverted to default'
		}
		println "daoImpl: ${daoImpl}"

		// obtain the security impl
		this.securityImpl = project.properties.securityImpl
		if(securityImpl == null) {
			securityImpl = DEFAULT_SECURITY_IMPL
			println 'No security impl specified in project properties reverted to default'
		}
		println "securityImpl: ${securityImpl}"

		// finally load the merged single config
		this.config = ConfigProcessor.merge(this.basedir + "/src/main/resources", this.stage, 'local')
		config.setProperty('stage', this.stage)
	}

	/**
	 * Copies webapp resources to the target war dir.
	 */
	public void copyWebappResources() {
		println 'Copying webapp resources..'
		String sdir = this.basedir + '/src/main/webapp'
		String tdir = this.webappDir
		ant.mkdir(dir: tdir)
		ant.copy(todir: tdir, preservelastmodified:true) {
			fileset(dir: sdir) { exclude(name: '**/web.xml') }
		}
		println 'webapp resources copied.'
	}

	/**
	 * Generates the web.xml file based on the configuration/project state.
	 */
	public void createDeployWebXmlFile() {
		println 'Generating deploy web.xml..'

		Map props = [:];

		// read web.xml
		StringBuilder sbuf = new StringBuilder(3000)
		new File(this.basedir + '/src/main/webapp/WEB-INF', 'web.xml').eachLine{ line ->
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
				s = s.replaceAll(regex_db_db4o, '')
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
		String dir = this.webappDir + '/WEB-INF'
		ant.mkdir(dir: dir)
		(new File(dir, 'web.xml')).write(s);
		println 'web.xml created'
	}

	/**
	 * Saves the generated config to disk that is deploy ready.
	 */
	public void createDeployConfigFile() {
		// create aggregated config.properties file..
		println 'Creating consolidated config.properties file..'
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
				String db4oFilepath = project.build.outputDirectory.toString() + '/' + this.config.getString('db.db4o.filename')
				File f = new File(db4oFilepath);
				if(!f.exists()) {
					// NOTE: we need to temporarily change a few db props to avoid db4o's dbfilelocked exception
					def origTrans = this.config.getProperty('db.transaction.bindToSpringAtTransactional')
					def db4oFilename = this.config.getProperty('db.db4o.filename')
					this.config.setProperty('db.db4o.filename', db4oFilepath)
					this.config.setProperty('db.transaction.bindToSpringAtTransactional', false)
					dbShell = Guice.createInjector(
						new SmbizDb4oDaoModule(this.config), 
						new SmbizEGraphModule(), 
						new Db4oDbShellModule()).getInstance(IDbShell.class);
					this.config.setProperty('db.db4o.filename', db4oFilename)
					this.config.setProperty('db.transaction.bindToSpringAtTransactional', origTrans)
				}
				break
			case 'jdo':
			default:
				// TODO implement jdo
				throw new UnsupportedOperationException()
		}
		
		if(dbShell == null) {
			println 'sbmiz db exists (no db stub necessary)'
			return;
		}
		
		println 'creating sbmiz db..'
		dbShell.create()
		dbShell.addData()
		println 'sbmiz db created'
	}
}
