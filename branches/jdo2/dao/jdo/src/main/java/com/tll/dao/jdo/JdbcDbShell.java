/**
 * The Logic Lab
 * @author jpk
 * @since Oct 28, 2009
 */
package com.tll.dao.jdo;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.google.inject.Inject;
import com.tll.dao.IDbShell;

/**
 * JdbcDbShell - Simple JDBC based way to communicate with a data store.
 * <p>
 * Intended to create, delete, stub and otherwise generally affect a targeted
 * database. Helpful for testing.
 * @author jpk
 */
public class JdbcDbShell implements IDbShell {

	/**
	 * IDbDialectHandler - Handles dialect specific db ops mainly translating
	 * dialect-specific db exceptions attempting to add detail to the unchecked
	 * exception.
	 * @author jpk
	 */
	public interface IDbDialectHandler {

		/**
		 * Determines if the given exception is due to an attempt to issue SQL
		 * commands to an unknown (prob. non-existant) database.
		 * @param re
		 * @return true/false
		 */
		boolean isUnknownDatabase(Exception re);

		/**
		 * Determines if the given exception is due to an attempt to create an
		 * already created database.
		 * @param re
		 * @return true/false
		 */
		boolean isCreateAlreadyExist(Exception re);

		/**
		 * Determines if the given exception is due to an attempt to drop an a
		 * non-existant database.
		 * @param re
		 * @return true/false
		 */
		boolean isDropNonExistant(Exception re);
	} // IDbDialectHandler

	/**
	 * MySql implementation of {@link IDbDialectHandler}
	 * @author jpk
	 */
	public static class MySqlDialectHandler implements IDbDialectHandler {

		public static final int ER_DB_CREATE_EXISTS = 1007;

		/**
		 * Occurrs when a target db doesn't exist for drop.
		 */
		public static final int ER_DB_DROP_NOEXIST = 1008;

		public static final int ER_BAD_DB_ERROR = 1049;

		public static final int ER_DUP_ENTRY = 1062;

		/**
		 * Occurrs when a target table doesn't exist.
		 */
		public static final int ER_TABLE_NO_EXIST = 1146;

		public static final int ER_DUP_ENTRY_AUTOINCREMENT_CASE = 1559;

		public static final int ER_DUP_ENTRY_WITH_KEY_NAME = 1582;

		/**
		 * Attempts to extract an {@link SQLException} from the given exception
		 * @param e the exception to check
		 * @return The exception itself if it is an {@link SQLException} or if its
		 *         cause is an {@link SQLException} or <code>null</code> if no
		 *         {@link SQLException} could be obtained.
		 */
		protected SQLException extractSQLException(Exception e) {
			SQLException sqle = null;
			if(e instanceof SQLException) {
				sqle = (SQLException) e;
			}
			else if(e.getCause() instanceof SQLException) {
				sqle = (SQLException) e.getCause();
			}
			return sqle;
		}

		/**
		 * Extracts the SQL error code from the given exception. <br>
		 * <b>NOTE: </b>This method is certainly <em>not</em> fool-proof and really
		 * is a "poor man's" way of trying to find it.
		 * @param e the exception to check
		 * @return the sql error code or <code>null</code> if it wasn't found.
		 */
		protected Integer extractSqlErrorCode(Exception e) {
			final SQLException sqle = extractSQLException(e);
			return sqle == null ? null : Integer.valueOf(sqle.getErrorCode());
		}

		/**
		 * Does the given exception contain one of the given sql error codes?
		 * @param e the exception to check
		 * @param targetErrorCodes array of error codes whereby one is being sought
		 * @return true/false
		 */
		protected boolean isErrorOfAType(Exception e, int[] targetErrorCodes) {
			final Integer ec = extractSqlErrorCode(e);
			if(ec != null) {
				final int iv = ec.intValue();
				for(final int i : targetErrorCodes) {
					if(iv == i) return true;
				}
			}
			// can't extract error code so fall back to interrogating the error messsage
			final String msg = e.getMessage();
			if(msg != null) {
				for(final int i : targetErrorCodes) {
					if(msg.indexOf(Integer.toString(i)) >= 0) {
						return true;
					}
				}
			}
			// can't find it
			return false;
		}

		public boolean isUnknownDatabase(Exception e) {
			return isErrorOfAType(e, new int[] {
				ER_BAD_DB_ERROR
			});
		}

		public boolean isCreateAlreadyExist(Exception e) {
			return isErrorOfAType(e, new int[] {
				ER_DB_CREATE_EXISTS
			});
		}

		public boolean isDropNonExistant(Exception e) {
			return isErrorOfAType(e, new int[] {
				ER_DB_DROP_NOEXIST, ER_TABLE_NO_EXIST
			});
		}
	}

	private static final Log log = LogFactory.getLog(JdbcDbShell.class);

	private static final String NL = System.getProperty("line.separator");

	private static final String SQL_LINE_COMMENT_PREFIX = "--";

	private static final char SQL_COMMAND_DELIM_CHAR = ';';

	/**
	 * Generic execution of SQL commands for the given data source.
	 * @param theDataSource
	 * @param sql The SQL command to be executed
	 * @throws DataAccessException
	 */
	private static void executeSql(DataSource theDataSource, String sql) throws DataAccessException {
		final ShellImpl shell = new ShellImpl(theDataSource);
		final JdbcTemplate jdbc = shell.getJdbcTemplate();
		jdbc.execute(sql);
	}

	/**
	 * Executes sql/ddl commands held in the given resource against the given data
	 * source.
	 * @param dataSource
	 * @param url The sql/ddl resource to load and invoke
	 */
	private static void executeDbCommandsFromResource(DataSource dataSource, URL url) {
		String s;
		try {
			s = IOUtils.toString(url.openStream());
		}
		catch(final IOException e) {
			throw new IllegalStateException("Unable to read sql/ddl resource: " + e.getMessage(), e);
		}

		// remove comments
		final String[] lines = s.split(NL);
		final StringBuffer sb = new StringBuffer();
		for(final String line : lines) {
			final String tline = line.trim();
			if(!tline.startsWith(SQL_LINE_COMMENT_PREFIX)) {
				sb.append(tline);
				sb.append(' ');
			}
		}

		if(log.isDebugEnabled()) {
			log.debug("Executing SQL/DDL commands: " + url.getPath() + "...");
		}
		final String[] sqls = StringUtils.split(sb.toString(), SQL_COMMAND_DELIM_CHAR);
		for(final String sql : sqls) {
			final String cmd = StringUtils.trim(sql);
			if(!cmd.isEmpty()) executeSql(dataSource, sql);
		}
	}

	/**
	 * The name of the db
	 */
	private final String dbName;

	/**
	 * The URLs that point to the ddl schema, sql stub and the sql delete
	 * resources respectively. <br>
	 * NOTE: these resources are in the form of {@link URI} to accommodate the use
	 * case of being contained within a jar file (or other some such).
	 */
	private final URL dbSchemaResource, dbStubResource, dbDelResource;

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
	private final IDbDialectHandler dbDialectHandler;

	/**
	 * Constructor
	 * @param rootDbName
	 * @param dbName
	 * @param urlPrefix
	 * @param username
	 * @param password
	 * @param dbSchemaResource
	 * @param dbStubResource
	 * @param dbDelResource
	 * @param dbDialectHandler
	 */
	@Inject
	public JdbcDbShell(String rootDbName, String dbName, String urlPrefix, String username, String password,
			URL dbSchemaResource, URL dbStubResource, URL dbDelResource, IDbDialectHandler dbDialectHandler) {
		super();

		final String rootUrl = urlPrefix + '/' + rootDbName;
		final String url = urlPrefix + '/' + dbName;

		this.rootDataSource = new SingleConnectionDataSource(rootUrl, username, password, false);
		this.dataSource = new SingleConnectionDataSource(url, username, password, false);
		this.dbName = dbName;

		this.dbSchemaResource = dbSchemaResource;
		this.dbStubResource = dbStubResource;
		this.dbDelResource = dbDelResource;

		this.dbDialectHandler = dbDialectHandler;

		if(log.isInfoEnabled()) {
			log.info("Jdbc shell instantiated for db: " + dbName);
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
	 * Creates the database. If the db already exists, nothing happens.
	 * @return <code>true</code> if the db was actually created as a result of
	 *         calling this method and <code>false<code> if the db already exists.
	 */
	@Override
	public boolean create() {

		// create the db
		try {
			executeSql(rootDataSource, "create database " + dbName);
			if(log.isInfoEnabled()) log.info(dbName + " database created.");
		}
		catch(final DataAccessException dae) {
			if(!dbDialectHandler.isCreateAlreadyExist(dae)) {
				throw dae;
			}
			// presume we have already created the db
			return false;
		}

		// create db schema
		executeDbCommandsFromResource(dataSource, dbSchemaResource);
		if(log.isInfoEnabled()) log.info(dbName + " database schema created.");
		return true;
	}

	/**
	 * Deletes the database. If the db doesn't exist, nothing happens.
	 * @return <code>true</code> if the db was actually deleted as a result of
	 *         calling this method and
	 *         <code>false<code> if the db is found not to exist.
	 */
	@Override
	public boolean delete() {
		try {
			executeSql(rootDataSource, "drop database " + dbName);
			if(log.isInfoEnabled()) log.info(dbName + " database dropped.");
			return true;
		}
		catch(final DataAccessException dae) {
			if(!dbDialectHandler.isDropNonExistant(dae)) {
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
	@Override
	public boolean clear() {
		try {
			executeDbCommandsFromResource(dataSource, dbDelResource);
			if(log.isInfoEnabled()) log.info(dbName + " database cleared.");
			return true;
		}
		catch(final DataAccessException dae) {
			if(!dbDialectHandler.isDropNonExistant(dae)) {
				throw dae;
			}
		}
		return false;
	}

	/**
	 * Adds data to the db with the data set gotten from loading the db stub
	 * resource. The db <em>must</em> already exist else an error is raised.
	 * @return <code>true</code> if the db was actually stubbed with the stub data
	 *         as a result of calling this method.
	 */
	@Override
	public boolean stub() {

		try {
			executeDbCommandsFromResource(dataSource, dbStubResource);
			if(log.isInfoEnabled()) log.info(dbName + " database stubbed.");
			return true;
		}
		catch(final DataAccessException dae) {
			if(!dbDialectHandler.isUnknownDatabase(dae)) {
				throw dae;
			}
		}
		return false;
	}

	/**
	 * Stubs or re-stubs the data in the db creating the db if not already created
	 * and/or clearing the the db if it contains existing data.
	 */
	@Override
	public void restub() {
		// fist try to clear
		if(!clear()) {
			create();
		}
		stub();
	}
}
