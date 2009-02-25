/**
 * The Logic Lab
 * @author jpk
 * Feb 9, 2009 Feb 9, 2009
 */

package com.tll.dao.jdbc;

import java.net.URL;

import com.tll.config.Config;
import com.tll.config.IConfigKey;

/**
 * DbShellExec - Creates {@link DbShell} instances from a given {@link Config} .
 * @author jpk
 */
public final class DbShellBuilder {

	/**
	 * ConfigKeys - Configuration property keys.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DB_NAME("db.name"),
		DB_NAME_ROOT("db.name.root"),

		DB_URL_PREFIX("db.urlprefix"),
		DB_USERNAME("db.username"),
		DB_PASSWORD("db.password"),

		DB_RESOURCE_SCHEMA("db.resource.schema"),
		DB_RESOURCE_STUB("db.resource.stub"),
		DB_RESOURCE_DELETE("db.resource.delete");

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

	/**
	 * Builds a {@link DbShell} instance entirely from properties held in the
	 * given configuration.
	 * <p>
	 * The db sql/ddl resources specified in the config are considered as
	 * classpath resources and are loaded as such.
	 * @param config The configuration
	 * @return A new {@link DbShell}.
	 * @throws Exception Upon error during build
	 * @see #getDbShell(Config, URL, URL, URL)
	 */
	public static DbShell getDbShell(Config config) throws Exception {
		String dbSchemaPath = config.getString(ConfigKeys.DB_RESOURCE_SCHEMA.getKey());
		String dbDataStubPath = config.getString(ConfigKeys.DB_RESOURCE_STUB.getKey());
		String dbDataDeletePath = config.getString(ConfigKeys.DB_RESOURCE_DELETE.getKey());
		
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL schema = cl.getResource(dbSchemaPath);
		if(schema == null) {
			throw new Exception("Can't resolve db schema resource from path: " + dbSchemaPath);
		}
		URL stub = cl.getResource(dbDataStubPath);
		if(stub == null) {
			throw new Exception("Can't resolve db stub resource from path: " + dbDataStubPath);
		}
		URL delete = cl.getResource(dbDataDeletePath);
		if(delete == null) {
			throw new Exception("Can't resolve db delete resource from path: " + dbDataDeletePath);
		}
		
		return getDbShell(config, schema, stub, delete);
	}

	/**
	 * @param config The configuration holding necessary properties to assemble a
	 *        jdbc data source.
	 * @param schemaDef The db schema ddl definition resource
	 * @param stubDef The sql inserts definition resource serving to stub the db.
	 * @param deleteDef The sql delete statements definition resource serving to
	 *        clear all db data.
	 * @return A new {@link DbShell}.
	 * @throws Exception Upon error during build
	 */
	public static DbShell getDbShell(Config config, URL schemaDef, URL stubDef, URL deleteDef) throws Exception {
		String rootDbName = config.getString(ConfigKeys.DB_NAME_ROOT.getKey());
		String dbName = config.getString(ConfigKeys.DB_NAME.getKey());
		String urlPrefix = config.getString(ConfigKeys.DB_URL_PREFIX.getKey());
		String username = config.getString(ConfigKeys.DB_USERNAME.getKey());
		String password = config.getString(ConfigKeys.DB_PASSWORD.getKey());
		
		return new DbShell(rootDbName, dbName, urlPrefix, username, password, schemaDef, stubDef, deleteDef,
				DbDialectHandlerBuilder
				.getDbDialectHandler(config));
	}
}
