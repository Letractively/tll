/**
 * The Logic Lab
 * @author jpk
 * Feb 7, 2008
 */
package com.tll.config;

/**
 * ConfigKeys - Configuration property key names used by the app.
 * <p>
 * Not all keys names present in config.properties have to be declared here.
 * Only the ones accessed from a Java context.
 * @author jpk
 */
public enum ConfigKeys implements IConfigKeyProvider {

	ENVIRONMENT_PARAM("environment"),
	DEBUG_PARAM("debug"),

	APP_NAME("app.name"),

	SECURITY_MODE_PARAM("app.security.mode"),
	DAO_MODE_PARAM("db.dao.mode"),
	JPA_MODE_PARAM("db.jpa.mode"),

	USER_DEFAULT_EMAIL_PARAM("mail.dflt_user_email"),

	DB_NAME("db.name"),
	DB_TEST_NAME("db.test.name"),
	DB_NAME_ROOT("db.name.root"),
	DB_JPA_PERSISTENCE_UNIT_NAME("db.jpa.persistenceUnitName"),

	DB_DRIVER("db.driver"),
	DB_URL("db.url"),
	DB_URL_PREFIX("db.urlprefix"),
	DB_USERNAME("db.username"),
	DB_PASSWORD("db.password"),

	DB_SCHEMA_FILE_NAME("db.file.schema"),
	DB_STUB_FILE_NAME("db.file.stub"),
	DB_DELETE_FILE_NAME("db.file.delete");

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

	public String[] getConfigKeys() {
		final ConfigKeys[] cks = values();
		String[] keys = new String[cks.length];
		for(int i = 0; i < cks.length; ++i) {
			keys[i] = cks[i].getKey();
		}
		return keys;
	}

}
