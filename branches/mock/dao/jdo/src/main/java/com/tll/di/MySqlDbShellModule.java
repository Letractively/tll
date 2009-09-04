/**
 * The Logic Lab
 * @author jpk
 * @since Aug 29, 2009
 */
package com.tll.di;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.dao.IDbShell;
import com.tll.dao.jdo.JdbcDbShell;
import com.tll.dao.jdo.MySqlDialectHandler;

/**
 * MySqlDbShellModule
 * @author jpk
 */
public class MySqlDbShellModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(MySqlDbShellModule.class);

	/**
	 * ConfigKeys
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

	Config config;

	/**
	 * Constructor
	 */
	public MySqlDbShellModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public MySqlDbShellModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing MySql db shell module.");

		bind(IDbShell.class).toProvider(new Provider<IDbShell>() {

			@Override
			public IDbShell get() {
				final String dbType = config.getString(ConfigKeys.DB_TYPE.getKey());
				if(!IDbShell.DB_TYPE_MYSQL.equals(dbType)) {
					throw new IllegalStateException("Non-MySql db type");
				}

				final ClassLoader cl = Thread.currentThread().getContextClassLoader();
				final String dbSchemaPath = config.getString(ConfigKeys.DB_RESOURCE_SCHEMA.getKey());
				final String dbDataStubPath = config.getString(ConfigKeys.DB_RESOURCE_STUB.getKey());
				final String dbDataDeletePath = config.getString(ConfigKeys.DB_RESOURCE_DELETE.getKey());
				final URL schema = cl.getResource(dbSchemaPath);
				if(schema == null) {
					throw new IllegalStateException("Can't resolve db schema resource from path: " + dbSchemaPath);
				}
				final URL stub = cl.getResource(dbDataStubPath);
				if(stub == null) {
					throw new IllegalStateException("Can't resolve db stub resource from path: " + dbDataStubPath);
				}
				final URL delete = cl.getResource(dbDataDeletePath);
				if(delete == null) {
					throw new IllegalStateException("Can't resolve db delete resource from path: " + dbDataDeletePath);
				}

				final String rootDbName = config.getString(ConfigKeys.DB_NAME_ROOT.getKey());
				final String dbName = config.getString(ConfigKeys.DB_NAME.getKey());
				final String urlPrefix = config.getString(ConfigKeys.DB_URL_PREFIX.getKey());
				final String username = config.getString(ConfigKeys.DB_USERNAME.getKey());
				final String password = config.getString(ConfigKeys.DB_PASSWORD.getKey());

				// instantiate MySql dialect handler
				final MySqlDialectHandler dh;
				try {
					dh = MySqlDialectHandler.class.newInstance();
				}
				catch(final IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
				catch(final InstantiationException e) {
					throw new IllegalStateException(e);
				}

				return new JdbcDbShell(rootDbName, dbName, urlPrefix, username, password, schema, stub, delete, dh);
			}
		});
	}

}
