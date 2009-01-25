/**
 * The Logic Lab
 * @author jpk
 * Feb 9, 2008
 */
package com.tll.dao.jdbc;

import java.io.File;
import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.dao.IDbDialectHandler;

/**
 * DbShell - Simple JDBC based way to communicate with a data store.
 * <p>
 * Intended to create, delete, stub and otherwise generally affect a targeted
 * database. Helpful for testing.
 * @author jpk
 */
public final class DbShell {

	private static final Log log = LogFactory.getLog(DbShell.class);

	private static final String NL = System.getProperty("line.separator");

	private static final String SQL_LINE_COMMENT_PREFIX = "--";

	private static final char SQL_COMMAND_DELIM_CHAR = ';';

	/**
	 * The name of the db
	 */
	private final String dbName;

	/**
	 * The file name on the classpath holding the DDL commands representing the db
	 * schema.
	 */
	private final String dbSchemaFileName;

	/**
	 * The file name on the classpath holding the SQL commands that inserts the
	 * initial data set.
	 */
	private final String dbDataStubFileName;

	/**
	 * The file name on the classpath holding the SQL commands that clears all
	 * data from the database.
	 */
	private final String dbDataDeleteFileName;

	/**
	 * The data source pointing to the "root" database repository.
	 * <p>
	 * DDL type SQL commands such as database creation and deletion are issued
	 * against this data source.
	 */
	private final DataSource rootDataSource;

	/**
	 * The data source pointing to the application database within the database
	 * repository.
	 * <p>
	 * Application database specific DDL/SQL commands will use this data source.
	 */
	private final DataSource dataSource;

	/**
	 * Db dialect specific exception translator used for handling exceptions.
	 */
	private final IDbDialectHandler exceptionTranslator;

	/**
	 * Constructor
	 * @param rootDbName
	 * @param dbName
	 * @param urlPrefix
	 * @param username
	 * @param password
	 * @param dbSchemaFileName
	 * @param dbDataDeleteFileName
	 * @param dbDataStubFileName
	 * @param exceptionTranslator
	 */
	@Inject
	public DbShell(String rootDbName, String dbName, String urlPrefix, String username, String password,
			String dbSchemaFileName, String dbDataDeleteFileName, String dbDataStubFileName,
			IDbDialectHandler exceptionTranslator) {
		super();

		String rootUrl = urlPrefix + '/' + rootDbName;
		String url = urlPrefix + '/' + dbName;

		this.rootDataSource = new SingleConnectionDataSource(rootUrl, username, password, false);
		this.dataSource = new SingleConnectionDataSource(url, username, password, false);
		this.dbName = dbName;
		this.dbSchemaFileName = dbSchemaFileName;
		this.dbDataDeleteFileName = dbDataDeleteFileName;
		this.dbDataStubFileName = dbDataStubFileName;
		this.exceptionTranslator = exceptionTranslator;

		if(log.isInfoEnabled()) {
			log.info("db shell instantiated for db: " + dbName);
		}
	}

	/**
	 * ShellImpl
	 * @author jpk
	 */
	private static final class ShellImpl extends JdbcDaoSupport {

		/**
		 * Constructor
		 * @param dataSource
		 */
		public ShellImpl(DataSource dataSource) {
			super();
			assert dataSource != null;
			setDataSource(dataSource);
		}

	}

	/**
	 * Generic execution of SQL commands for the given data source.
	 * @param dataSource
	 * @param sql The SQL command to be executed
	 * @throws DataAccessException
	 */
	private void executeSql(DataSource dataSource, String sql) throws DataAccessException {
		ShellImpl shell = new ShellImpl(dataSource);
		JdbcTemplate jdbc = shell.getJdbcTemplate();
		jdbc.execute(sql);
	}

	/**
	 * Executes SQL commands held in the given file against the given data source.
	 * @param dataSource
	 * @param f
	 */
	private void executeSqlCommandsFromFile(DataSource dataSource, File f) {
		String s;
		try {
			s = FileUtils.readFileToString(f);
		}
		catch(IOException e) {
			throw new SystemError("Unable to read sql/ddl file contents: " + e.getMessage(), e);
		}

		// remove comments
		String[] lines = s.split(NL);
		StringBuffer sb = new StringBuffer();
		for(String line : lines) {
			String tline = line.trim();
			if(!tline.startsWith(SQL_LINE_COMMENT_PREFIX)) {
				sb.append(tline);
				sb.append(' ');
			}
		}

		if(log.isDebugEnabled()) {
			log.debug("Executing SQL command file: " + f.getName() + "...");
		}
		String[] sqls = StringUtils.split(sb.toString(), SQL_COMMAND_DELIM_CHAR);
		for(String sql : sqls) {
			String cmd = StringUtils.trim(sql);
			if(!cmd.isEmpty()) executeSql(dataSource, sql);
		}
	}

	/**
	 * Creates the database. If the db already exists, nothing happens.
	 * @return <code>true</code> if the db was actually created as a result of
	 *         calling this method and <code>false<code> if the db already exists.
	 */
	public boolean create() {

		// create the db
		try {
			executeSql(rootDataSource, "create database " + dbName);
			if(log.isInfoEnabled()) log.info(dbName + " database created.");
		}
		catch(DataAccessException dae) {
			if(!exceptionTranslator.isCreateAlreadyExist(dae)) {
				throw dae;
			}
			// presume we have already created the db
			return false;
		}

		// create db schema
		try {
			executeSqlCommandsFromFile(dataSource, (new ClassPathResource(dbSchemaFileName)).getFile());
			if(log.isInfoEnabled()) log.info(dbName + " database schema created.");
		}
		catch(IOException e) {
			throw new SystemError("Unable to create db schema: " + e.getMessage(), e);
		}
		return true;
	}

	/**
	 * Deletes the database. If the db doesn't exist, nothing happens.
	 * @return <code>true</code> if the db was actually deleted as a result of
	 *         calling this method and
	 *         <code>false<code> if the db is found not to exist.
	 */
	public boolean delete() {
		try {
			executeSql(rootDataSource, "drop database " + dbName);
			if(log.isInfoEnabled()) log.info(dbName + " database dropped.");
			return true;
		}
		catch(DataAccessException dae) {
			if(!exceptionTranslator.isDropNonExistant(dae)) {
				throw dae;
			}
		}
		return false;
	}

	/**
	 * Clears the database of all data. If the db doesn't exist, nothing happens.
	 * @return <code>true</code> if the db was actually cleared as a result of
	 *         calling this method and
	 *         <code>false<code> if the db is <code>not</code> cleared by way of
	 *         this method.
	 */
	public boolean clear() {
		try {
			ClassPathResource resource = new ClassPathResource(dbDataDeleteFileName);
			executeSqlCommandsFromFile(dataSource, resource.getFile());
			if(log.isInfoEnabled()) log.info(dbName + " database cleared.");
			return true;
		}
		catch(DataAccessException dae) {
			if(!exceptionTranslator.isDropNonExistant(dae)) {
				throw dae;
			}
		}
		catch(IOException e) {
			throw new SystemError(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * Adds data to the db with the data set gotten from loading the db stub file.
	 * The db <em>must</em> already exist else an error is raised.
	 * @return <code>true</code> if the db was actually stubbed with the stub data
	 *         as a result of calling this method.
	 */
	public boolean stub() {

		try {
			executeSqlCommandsFromFile(dataSource, (new ClassPathResource(dbDataStubFileName)).getFile());
			if(log.isInfoEnabled()) log.info(dbName + " database stubbed.");
			return true;
		}
		catch(IOException e) {
			throw new SystemError(e.getMessage(), e);
		}
		catch(DataAccessException dae) {
			if(!exceptionTranslator.isUnknownDatabase(dae)) {
				throw dae;
			}
		}
		return false;
	}

	/**
	 * Stubs or re-stubs the data in the db creating the db if not already created
	 * and/or clearing the the db if it contains existing data.
	 */
	public void restub() {
		// fist try to clear
		if(!clear()) {
			create();
		}
		stub();
	}
}
