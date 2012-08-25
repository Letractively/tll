/**
 * The Logic Lab
 * @author jpk
 * Mar 28, 2009
 */

package com.tll;

import java.net.URI

import com.google.inject.Guice
import com.tll.config.Config
import com.tll.config.ConfigRef
import com.tll.dao.IDbShell
import com.tll.dao.IEntityDao
import com.tll.dao.db4o.Db4oConfigKeys;
import com.tll.dao.db4o.Db4oDbShell
import com.tll.dao.db4o.SmbizDb4oDaoModule
import com.tll.dao.db4o.test.Db4oDbShellModule
import com.tll.model.SmbizEGraphModule

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
		b.createDeployConfigFile()
		b.stubDbIfNecessary()
	}

	static final String DEFAULT_STAGE = 'debug'
	static final String DEFAULT_DAO_IMPL = 'db4o'
	static final String DEFAULT_SECURITY_IMPL = 'none'

	static final def NL = System.getProperty("line.separator")

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
		println 'checking for existance of db..'
		// get a db shell instance
		Db4oDbShell dbShell = null;
		switch(daoImpl) {
			case 'db4o':
				URI uri = Db4oDbShell.resolveDb4oFileLocationFromConfig(config);
				println 'resolved smbiz db location: ' + uri.getPath()
				String db4oFilepath = uri.getPath()
				File f = new File(db4oFilepath);
				if(!f.exists()) {
					println 'Stubbing smbiz db4o db: ' + db4oFilepath
					def bconfig = new Config()
					bconfig.setProperty('db.db4o.filepath', db4oFilepath)
					bconfig.setProperty('db.transaction.bindToSpringAtTransactional', false)
					def injector = Guice.createInjector(
						new SmbizDb4oDaoModule(bconfig), 
						new SmbizEGraphModule(), 
						new Db4oDbShellModule());
					dbShell = injector.getInstance(IDbShell.class)
					def dbSess = injector.getInstance(IEntityDao.class).getObjectContainer(); 
					println ">>>>> creating sbmiz db4o db: '${db4oFilepath}'.."
					dbShell.create()
					dbShell.addData(dbSess)
					dbSess.close();
					println "sbmiz db db4o file: '" + db4oFilepath + "' created."
				}
				break
			case 'jdo':
				// TODO implement jdo
			default:
				throw new UnsupportedOperationException()
		}
	}
}
