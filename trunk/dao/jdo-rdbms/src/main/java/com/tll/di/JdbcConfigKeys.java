/**
 * The Logic Lab
 * @author jpk
 * @since Oct 28, 2009
 */
package com.tll.di;

import com.tll.config.IConfigKey;


/**
 * JdbcConfigKeys
 * @author jpk
 */
public enum JdbcConfigKeys implements IConfigKey {
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

	private JdbcConfigKeys(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

}
