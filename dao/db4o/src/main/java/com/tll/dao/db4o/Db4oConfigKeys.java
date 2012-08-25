package com.tll.dao.db4o;

import com.tll.config.IConfigKey;

/**
 * Standalone definition of db4o configuration properties.
 * @author jpk
 */
public enum Db4oConfigKeys implements IConfigKey {

	/** absolute path ref to the db4o db file */
	DB4O_FILEREF("db.db4o.filepath"),

	/** the non-path name of the db4o db file */
	DB4O_FILENAME("db.db4o.filename"),

	DB_TRANS_TIMEOUT("db.transaction.timeout"),

	/**
	 * Fulfill transations by binding to Spring's @Transactional method
	 * annotations? true/false
	 */
	DB_TRANS_BINDTOSPRING("db.transaction.bindToSpringAtTransactional");

	private final String key;

	private Db4oConfigKeys(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}
} // ConfigKeys