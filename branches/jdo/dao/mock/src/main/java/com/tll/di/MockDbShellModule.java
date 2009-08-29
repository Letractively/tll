/**
 * The Logic Lab
 * @author jpk
 * @since Aug 29, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.dao.IDbShell;

/**
 * MockDbShellModule
 * @author jpk
 */
public class MockDbShellModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(MockDbShellModule.class);

	/**
	 * ConfigKeys
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

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

	Config config;

	/**
	 * Constructor
	 */
	public MockDbShellModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public MockDbShellModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing mock db shell module.");

		bind(IDbShell.class).toProvider(new Provider<IDbShell>() {

			@Override
			public IDbShell get() {
				return null;
			}

		});
	}

}
