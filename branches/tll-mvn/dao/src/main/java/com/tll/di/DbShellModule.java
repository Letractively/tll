package com.tll.di;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.jdbc.DbShell;

/**
 * DbShellModule
 * @author jpk
 */
public class DbShellModule extends GModule {

	/**
	 * ConfigKeys - Configuration property keys.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DB_NAME("db.name"),
		DB_TEST_NAME("db.test.name"),
		DB_NAME_ROOT("db.name.root"),

		DB_URL_PREFIX("db.urlprefix"),
		DB_USERNAME("db.username"),
		DB_PASSWORD("db.password"),

		DB_SCHEMA_FILE_NAME("db.file.schema"),
		DB_STUB_FILE_NAME("db.file.stub"),
		DB_DELETE_FILE_NAME("db.file.delete");

		private final String key;

		/**
		 * Constructor
		 * @param key
		 */
		private ConfigKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	final String rootDbName, dbName, urlPrefix, username, password, dbSchemaFileName, dbDataDeleteFileName,
			dbDataStubFileName;

	/**
	 * Constructor
	 * @param dbName The name of the db for which to provide a shell.
	 */
	public DbShellModule(String dbName) {
		super();
		final Config config = Config.instance();
		// this.driverClassName = config.getString(ConfigKeys.DB_DRIVER.getKey());
		this.rootDbName = config.getString(ConfigKeys.DB_NAME_ROOT.getKey());
		this.dbName = dbName;
		this.urlPrefix = config.getString(ConfigKeys.DB_URL_PREFIX.getKey());
		this.username = config.getString(ConfigKeys.DB_USERNAME.getKey());
		this.password = config.getString(ConfigKeys.DB_PASSWORD.getKey());
		this.dbSchemaFileName = config.getString(ConfigKeys.DB_SCHEMA_FILE_NAME.getKey());
		this.dbDataDeleteFileName = config.getString(ConfigKeys.DB_DELETE_FILE_NAME.getKey());
		this.dbDataStubFileName = config.getString(ConfigKeys.DB_STUB_FILE_NAME.getKey());
		log.info("Employing Db shell for: " + dbName);
	}

	@Override
	protected void configure() {

		bind(DbShell.class).toProvider(new Provider<DbShell>() {

			@Inject
			IDbDialectHandler exceptionTranslator;

			public DbShell get() {
				return new DbShell(rootDbName, dbName, urlPrefix, username, password, dbSchemaFileName, dbDataDeleteFileName,
						dbDataStubFileName, exceptionTranslator);
			}

		}).in(Scopes.SINGLETON);
	}
}