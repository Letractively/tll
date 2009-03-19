/**
 * The Logic Lab
 * @author jpk Nov 21, 2007
 */
package com.tll.server;

import com.tll.config.IConfigKey;

/**
 * SecurityMode
 * @author jpk
 */
public enum SecurityMode {
	/**
	 * Use the Acegi security framework.
	 */
	ACEGI,
	/**
	 * Use NO security framework.
	 */
	NONE;

	/**
	 * ConfigKeys - Config keys for the security module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {
		SECURITY_MODE_PARAM("security.mode");

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