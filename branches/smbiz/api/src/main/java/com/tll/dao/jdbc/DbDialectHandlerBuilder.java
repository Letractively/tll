/**
 * The Logic Lab
 * @author jpk
 * Feb 9, 2009
 */
package com.tll.dao.jdbc;

import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.dialect.MySqlDialectHandler;


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
	 * The db type indicating db dialect: MySql
	 */
	public static final String DB_TYPE_MYSQL = "mysql";

	/**
	 * Resolves an {@link IDbDialectHandler} impl type from a db type held in the
	 * given configuration.
	 * @param config The configuration from which needed properties are extracted
	 * @return The associated db dialect handler
	 * @throws IllegalArgumentException When the db type name is not recognized.
	 */
	public static Class<? extends IDbDialectHandler> getDbDialectHandlerTypeFromDbType(Config config)
			throws IllegalArgumentException {
		String dbType = Config.instance().getString(ConfigKeys.DB_TYPE.getKey());
		if(DB_TYPE_MYSQL.equals(dbType)) {
			return MySqlDialectHandler.class;
		}
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
