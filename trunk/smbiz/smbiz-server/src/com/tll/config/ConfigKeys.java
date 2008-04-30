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
public abstract class ConfigKeys {

	public static final String ENVIRONMENT_PARAM = "environment";
	public static final String DEBUG_PARAM = "debug";

	public static final String APP_NAME = "app.name";

	public static final String SECURITY_MODE_PARAM = "app.security.mode";
	public static final String DAO_MODE_PARAM = "db.dao.mode";
	public static final String JPA_MODE_PARAM = "db.jpa.mode";

	public static final String USER_DEFAULT_EMAIL_PARAM = "mail.dflt_user_email";

	public static final String DB_NAME = "db.name";
	public static final String DB_TEST_NAME = "db.test.name";
	public static final String DB_NAME_ROOT = "db.name.root";
	public static final String DB_JPA_PERSISTENCE_UNIT_NAME = "db.jpa.persistenceUnitName";

	public static final String DB_DRIVER = "db.driver";
	public static final String DB_URL = "db.url";
	public static final String DB_URL_PREFIX = "db.urlprefix";
	public static final String DB_USERNAME = "db.username";
	public static final String DB_PASSWORD = "db.password";

	public static final String DB_SCHEMA_FILE_NAME = "db.file.schema";
	public static final String DB_STUB_FILE_NAME = "db.file.stub";
	public static final String DB_DELETE_FILE_NAME = "db.file.delete";

}
