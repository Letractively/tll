/**
 * The Logic Lab
 * @author jpk
 * Feb 9, 2009
 */
package com.tll.db;

import com.tll.config.Config;
import com.tll.config.IConfigKey;


/**
 * DbDialectHandlerBuilder
 * @author jpk
 */
public class DbDialectHandlerBuilder {

	/**
	 * ConfigKeys - The config property definitions required for db dialect
	 * module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DB_TYPE("db.type");

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
	 * Resolves an {@link IDbDialectHandler} impl type from a db type held in the
	 * given configuration.
	 * @param config The configuration from which needed properties are extracted
	 * @return The associated db dialect handler
	 * @throws IllegalArgumentException When the db type name is not recognized.
	 */
	public static Class<? extends IDbDialectHandler> getDbDialectHandlerTypeFromDbType(Config config)
	throws IllegalArgumentException {
		final String dbType = config.getString(ConfigKeys.DB_TYPE.getKey());
		if(IDbShell.DB_TYPE_MYSQL.equals(dbType)) {
			return MySqlDialectHandler.class;
		}
		// NOTE: db4o doesn't required a db shell
		/*
		else if(IDbShell.DB_TYPE_DB4O.equals(dbType)) {

		}
		 */
		throw new IllegalStateException("Unhandled db type: " + dbType);
	}

	/**
	 * Provides new {@link IDbDialectHandler} instances.
	 * @param config The configuration
	 * @return A new {@link IDbDialectHandler} impl instance based on the db type
	 *         specified in the configuration.
	 * @throws Exception Upon instantiation error or un-recognized db type
	 */
	public static IDbDialectHandler getDbDialectHandler(Config config) throws Exception {
		return getDbDialectHandlerTypeFromDbType(config).newInstance();
	}
}
