/**
 * The Logic Lab
 * @author jpk Feb 9, 2009 Feb 9, 2009
 */

package com.tll.db;

import java.net.URI;
import java.net.URL;

import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.model.IEntityGraphBuilder;

/**
 * DbShellExec - Creates {@link IDbShell} instances from a given {@link Config}
 * .
 * @author jpk
 */
public final class DbShellBuilder {

	/**
	 * ConfigKeys - Configuration property keys.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DB_TYPE("db.type"),
		DB_NAME("db.name"),

		DB_NAME_ROOT("db.name.root"),
		DB_URL("db.url"),
		DB_URL_PREFIX("db.urlprefix"),
		DB_USERNAME("db.username"),
		DB_PASSWORD("db.password"),

		DB_RESOURCE_SCHEMA("db.resource.schema"),
		DB_RESOURCE_STUB("db.resource.stub"),
		DB_RESOURCE_DELETE("db.resource.delete"),
		DB_ENTITYGRAPH_BUILDER_CLASSNAME("db.entitygraphbuilder.classname");

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
	 * Builds a {@link IDbShell} instance entirely from properties held in the
	 * given configuration.
	 * <p>
	 * Any sql/ddl resources specified in the config are considered as classpath
	 * resources and are loaded as such.
	 * @param config The configuration
	 * @return A new {@link IDbShell}.
	 * @throws Exception Upon error during build
	 */
	public static IDbShell getDbShell(Config config) throws Exception {
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
		final String dbType = config.getString(ConfigKeys.DB_TYPE.getKey());

		if(IDbShell.DB_TYPE_MYSQL.equals(dbType)) {
			final String dbSchemaPath = config.getString(ConfigKeys.DB_RESOURCE_SCHEMA.getKey());
			final String dbDataStubPath = config.getString(ConfigKeys.DB_RESOURCE_STUB.getKey());
			final String dbDataDeletePath = config.getString(ConfigKeys.DB_RESOURCE_DELETE.getKey());
			final URL schema = cl.getResource(dbSchemaPath);
			if(schema == null) {
				throw new Exception("Can't resolve db schema resource from path: " + dbSchemaPath);
			}
			final URL stub = cl.getResource(dbDataStubPath);
			if(stub == null) {
				throw new Exception("Can't resolve db stub resource from path: " + dbDataStubPath);
			}
			final URL delete = cl.getResource(dbDataDeletePath);
			if(delete == null) {
				throw new Exception("Can't resolve db delete resource from path: " + dbDataDeletePath);
			}

			final String rootDbName = config.getString(ConfigKeys.DB_NAME_ROOT.getKey());
			final String dbName = config.getString(ConfigKeys.DB_NAME.getKey());
			final String urlPrefix = config.getString(ConfigKeys.DB_URL_PREFIX.getKey());
			final String username = config.getString(ConfigKeys.DB_USERNAME.getKey());
			final String password = config.getString(ConfigKeys.DB_PASSWORD.getKey());

			return new JdbcDbShell(rootDbName, dbName, urlPrefix, username, password, schema, stub, delete,
					DbDialectHandlerBuilder.getDbDialectHandler(config));
		}
		else if(IDbShell.DB_TYPE_DB4O.equals(dbType)) {
			final String dbName = config.getString(ConfigKeys.DB_NAME.getKey());
			final URI uri = new URI(dbName);
			// constitute the entity graph builder
			final String egbcn = config.getString(ConfigKeys.DB_ENTITYGRAPH_BUILDER_CLASSNAME.getKey());
			IEntityGraphBuilder egb = null;
			final Class<?> c = Class.forName(egbcn);
			egb = (IEntityGraphBuilder) c.newInstance();
			return new Db4oDbShell(uri, egb);
		}

		throw new Exception("Unhandled db type: " + dbType);
	}
}
