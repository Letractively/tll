/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.server.AppContext;
import com.tll.service.entity.account.AddAccountService;

/**
 * AppModule
 * @author jpk
 */
public class AppModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(AppModule.class);

	/**
	 * ConfigKeys - Configuration property keys for the app context.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		STAGE("stage"),
		ENVIRONMENT("environment"),
		NOSECURITY_USER_EMAIL("server.nosecurity.user.email");

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
	public AppModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public AppModule(Config config) {
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
		log.info("Employing App module");

		bind(AppContext.class).toProvider(new Provider<AppContext>() {

			@Inject
			AddAccountService aas;

			@Override
			public AppContext get() {
				final String stage = config.getString(ConfigKeys.STAGE.getKey(), AppContext.DEFAULT_STAGE);
				final String environment = config.getString(ConfigKeys.ENVIRONMENT.getKey(), AppContext.DEFAULT_ENVIRONMENT);
				final String dfltUserEmail = config.getString(ConfigKeys.NOSECURITY_USER_EMAIL.getKey());
				return new AppContext(stage, environment, dfltUserEmail, aas);
			}
		}).in(Scopes.SINGLETON);
	}

}
