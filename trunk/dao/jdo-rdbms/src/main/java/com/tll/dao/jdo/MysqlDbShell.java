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
import java.util.ArrayList;

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
import com.tll.dao.IDbErrorDeterminer;
import com.tll.dao.IDbShell;

/**
 * MysqlDbShell - Simple JDBC based way to communicate with a data store.
 * <p>
 * Intended to create, delete, stub and otherwise generally affect a targeted
 * database. Helpful for testing.
 * @author jpk
 */
public class MysqlDbShell implements IDbShell {

	/**
	 * MySqlErrorDeterminer
	 * @author jpk
	 */
	static class MySqlErrorDeterminer implements IDbErrorDeterminer {

		static final int ER_DB_CREATE_EXISTS = 1007;

		/**
		 * Occurrs when a target db doesn't exist for drop.
		 */
		static final int ER_DB_DROP_NOEXIST = 1008;

		static final int ER_DUP_ENTRY = 1062;

		/**
		 * Occurrs when a target table is attempted to be created but already
		 * exists.
		 */
		static final int ER_TABLE_EXISTS = 1050;

		/**
		 * Occurrs when a target table doesn't exist.
		 */
		static final int ER_TABLE_NO_EXIST = 1146;

		static final int ER_DUP_ENTRY_AUTOINCREMENT_CASE = 1559;

		static final int ER_DUP_ENTRY_WITH_KEY_NAME = 1582;

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

		public boolean isCreateAlreadyExist(Exception e) {
			return isErrorOfAType(e, new int[] {
				ER_DB_CREATE_EXISTS
			});
		}

		public boolean isDropNonExistant(Exception e) {
			return isErrorOfAType(e, new int[] {
				ER_DB_DROP_NOEXIST
			});
		}

		public boolean isTableNonExistant(Exception e) {
			return isErrorOfAType(e, new int[] {
				ER_TABLE_NO_EXIST
			});
		}

		public boolean isTableAlreadyExists(Exception e) {
			return isErrorOfAType(e, new int[] {
				ER_TABLE_EXISTS
			});
		}

	} // MySqlErrorDeterminer

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

	} // ShellImpl

	private static final Log log = LogFactory.getLog(MysqlDbShell.class);

	private static final String NL = System.getProperty("line.separator");

	private static final String SQL_LINE_COMMENT_PREFIX = "--";

	private static final char SQL_COMMAND_DELIM_CHAR = ';';

	/**
	 * Parses SQL commands from a given resource removing comments.
	 * @param url the resource ref
	 * @return Array of Strings where each element is a single sql command
	 */
	private static String[] parseSqlCommandsFromResource(URL url) {
		if(log.isDebugEnabled()) {
			log.debug("Parsing SQL/DDL commands from: '" + url.getPath() + "'...");
		}
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

		// consolidate by sql command by splitting at sql command delim occurrences
		final ArrayList<String> rlist = new ArrayList<String>();
		final String[] sqls = StringUtils.split(sb.toString(), SQL_COMMAND_DELIM_CHAR);
		for(int i = 0; i < sqls.length; i++) {
			final String sql = sqls[i].trim();
			if(sql.length() > 0) {
				rlist.add(sql);
				if(log.isDebugEnabled()) log.debug("Ingested SQL command:" + sqls[i]);
			}
		}
		return rlist.toArray(new String[] {});
	}

	/**
	 * Generic execution of SQL commands for the given data source.
	 * @param theDataSource
	 * @param sql The SQL command to be executed
	 * @throws DataAccessException
	 */
	private static void executeSql(DataSource theDataSource, String sql) throws DataAccessException {
		final ShellImpl shell = new ShellImpl(theDataSource);
		final JdbcTemplate jdbc = shell.getJdbcTemplate();
		log.debug("SQL>: '" + sql + "'");
		jdbc.execute(sql);
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
	 * Parsed SQL command tokens corresponding to {@link #dbSchemaResource},
	 * {@link #dbStubResource} and {@link #dbDelResource} respectively. <br>
	 * Lazily initialized and once initialized, they take precedence over their
	 * corresponding resource from which they were generated.
	 */
	private String[] schemaSqls, stubSqls, deleteSqls;

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
	private final IDbErrorDeterminer errorDeterminer;

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
	 */
	@Inject
	public MysqlDbShell(String rootDbName, String dbName, String urlPrefix, String username, String password,
			URL dbSchemaResource, URL dbStubResource, URL dbDelResource) {
		super();

		final String rootUrl = urlPrefix + '/' + rootDbName;
		final String url = urlPrefix + '/' + dbName;

		this.rootDataSource = new SingleConnectionDataSource(rootUrl, username, password, false);
		this.dataSource = new SingleConnectionDataSource(url, username, password, false);
		this.dbName = dbName;

		this.dbSchemaResource = dbSchemaResource;
		this.dbStubResource = dbStubResource;
		this.dbDelResource = dbDelResource;

		this.errorDeterminer = new MySqlErrorDeterminer();

		if(log.isInfoEnabled()) {
			log.info("Jdbc shell instantiated for db: " + dbName);
		}
	}

	@Override
	public void create() {
		// create the db
		try {
			executeSql(rootDataSource, "create database " + dbName);
			if(log.isInfoEnabled()) log.info(dbName + " database created.");
		}
		catch(final DataAccessException e) {
			if(errorDeterminer.isCreateAlreadyExist(e)) {
				if(log.isDebugEnabled()) log.debug("database already exists: " + dbName);
				return;
			}
			throw e;
		}

		// create db schema
		if(schemaSqls == null) {
			schemaSqls = parseSqlCommandsFromResource(dbSchemaResource);
		}
		for(final String sql : schemaSqls) {
			try {
				executeSql(dataSource, sql);
			}
			catch(final DataAccessException e) {
				if(errorDeterminer.isTableAlreadyExists(e)) {
					if(log.isDebugEnabled()) log.debug("table already exists (skipping): " + sql);
					continue;
				}
				throw e;
			}
		}

		if(log.isInfoEnabled()) log.info(dbName + " database schema created.");
	}

	@Override
	public void drop() {
		try {
			executeSql(rootDataSource, "drop database " + dbName);
			if(log.isInfoEnabled()) log.info(dbName + " database dropped.");
		}
		catch(final DataAccessException e) {
			if(errorDeterminer.isDropNonExistant(e)) {
				if(log.isDebugEnabled()) log.debug("database doesn't exisst: " + dbName);
				return;
			}
			throw e;
		}
	}

	@Override
	public void clearData() {
		if(deleteSqls == null) {
			deleteSqls = parseSqlCommandsFromResource(dbDelResource);
		}
		for(final String sql : deleteSqls) {
			try {
				executeSql(dataSource, sql);
			}
			catch(final DataAccessException e) {
				if(errorDeterminer.isTableNonExistant(e)) {
					if(log.isDebugEnabled()) log.debug("table doesn't exist (skipping): " + sql);
					continue;
				}
				throw e;
			}
		}
		if(log.isInfoEnabled()) log.info(dbName + " database data cleared.");
	}

	@Override
	public void addData() {
		if(stubSqls == null) {
			stubSqls = parseSqlCommandsFromResource(dbStubResource);
		}
		for(final String sql : stubSqls) {
			// no catching here - we are fail-fast
			executeSql(dataSource, sql);
		}
		if(log.isInfoEnabled()) log.info(dbName + " database stubbed.");
	}
}
