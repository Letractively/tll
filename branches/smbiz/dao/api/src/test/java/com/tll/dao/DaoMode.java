package com.tll.dao;

import com.tll.config.IConfigKey;

/**
 * DaoMode
 */
public enum DaoMode {
	/**
	 * MOCK (in memory Object graph).
	 */
	MOCK,
	/**
	 * ORM (Object Relational Mapping)
	 */
	ORM;

	/**
	 * Does this dao mode indicate the employment of an actual disk-based
	 * datastore?
	 * @return true/false
	 */
	public boolean isDatastore() {
		return this == DaoMode.ORM;
	}

	/**
	 * ConfigKeys - Configuration property keys for the dao module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DAO_MODE_PARAM("db.dao.mode");

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
}