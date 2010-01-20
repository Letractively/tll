package com.tll.di.test;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.dao.IDbShell;
import com.tll.dao.jdo.JdbcDbShell;
import com.tll.di.JdbcConfigKeys;
import com.tll.util.ClassUtil;

/**
 * JdbcDbShellModule - Db4o db shell impl module.
 * @author jpk
 */
public class JdbcDbShellModule extends AbstractModule implements IConfigAware {

	static final Log log = LogFactory.getLog(JdbcDbShellModule.class);

	protected Config config;

	/**
	 * Constructor
	 */
	public JdbcDbShellModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public JdbcDbShellModule(Config config) {
		super();
		this.config = config;
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		log.info("Loading Jdbc db shell module...");
		if(config == null) throw new IllegalStateException("No config instance specified.");

		// IDbShell
		bind(IDbShell.class).toProvider(new Provider<IDbShell>() {

			@Override
			public IDbShell get() {
				final String dbType = config.getString(JdbcConfigKeys.DB_TYPE.getKey());

				final String rootDbName = config.getString(JdbcConfigKeys.DB_NAME_ROOT.getKey());
				final String dbName = config.getString(JdbcConfigKeys.DB_NAME.getKey());
				final String urlPrefix = config.getString(JdbcConfigKeys.DB_URL_PREFIX.getKey());
				final String username = config.getString(JdbcConfigKeys.DB_USERNAME.getKey());
				final String password = config.getString(JdbcConfigKeys.DB_PASSWORD.getKey());

				final String dbSchemaPath = config.getString(JdbcConfigKeys.DB_RESOURCE_SCHEMA.getKey());
				final String dbDataStubPath = config.getString(JdbcConfigKeys.DB_RESOURCE_STUB.getKey());
				final String dbDataDeletePath = config.getString(JdbcConfigKeys.DB_RESOURCE_DELETE.getKey());
				if(dbSchemaPath == null || dbDataStubPath == null || dbDataDeletePath == null) {
					throw new IllegalStateException("One or more unspecified JDBC shell related config properties");
				}

				final URL schema = ClassUtil.getResource(dbSchemaPath);
				if(schema == null) {
					throw new IllegalStateException("Can't resolve db schema resource from path: " + dbSchemaPath);
				}
				final URL stub = ClassUtil.getResource(dbDataStubPath);
				if(stub == null) {
					throw new IllegalStateException("Can't resolve db stub resource from path: " + dbDataStubPath);
				}
				final URL delete = ClassUtil.getResource(dbDataDeletePath);
				if(delete == null) {
					throw new IllegalStateException("Can't resolve db delete resource from path: " + dbDataDeletePath);
				}

				// mysql?
				if("mysql".equals(dbType)) {
					return new JdbcDbShell(rootDbName, dbName, urlPrefix, username, password, schema, stub, delete);
				}

				throw new IllegalStateException("Unhandled db type: " + dbType);
			}

		}).in(Scopes.SINGLETON);
	}

}