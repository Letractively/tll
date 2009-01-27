/**
 * The Logic Lab
 * @author jpk
 * Jan 24, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.dialect.MySqlDialectHandler;

/**
 * DbDialectModule - Resolves the db "type" from the configuration and wires it
 * up accordingly.
 * <p>
 * The db dialect is used by the dao and db shell layers.
 * @author jpk
 */
public class DbDialectModule extends GModule {
	
	/**
	 * The db type indicating db dialect: MySql
	 */
	public static final String DB_TYPE_MYSQL = "mysql";

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

	@Override
	protected void configure() {
		String dbType = Config.instance().getString(ConfigKeys.DB_TYPE.getKey());
		if(DB_TYPE_MYSQL.equals(dbType)) {
			bind(IDbDialectHandler.class).to(MySqlDialectHandler.class).in(Scopes.SINGLETON);
		}
		else {
			throw new IllegalStateException("Unhandled db type: " + dbType);
		}
	}

}
