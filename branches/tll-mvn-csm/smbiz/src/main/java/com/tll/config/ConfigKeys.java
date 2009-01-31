/**
 * The Logic Lab
 * @author jpk
 * Feb 7, 2008
 */
package com.tll.config;

/**
 * ConfigKeys - Configuration property key names used by the app.
 * @author jpk
 */
public enum ConfigKeys implements IConfigKey {

	ENVIRONMENT_PARAM("environment"),
	DEBUG_PARAM("debug"),

	SECURITY_MODE_PARAM("app.security.mode"),
	DAO_MODE_PARAM("db.dao.mode"),

	USER_DEFAULT_EMAIL_PARAM("mail.dflt_user_email");

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
