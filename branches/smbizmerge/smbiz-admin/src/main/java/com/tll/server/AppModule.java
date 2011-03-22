/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigKey;

/**
 * AppModule
 * @author jpk
 */
public class AppModule extends AbstractModule {

	private static final Logger log = LoggerFactory.getLogger(AppModule.class);

	public static enum ConfigKeys implements IConfigKey {

		STAGE("stage"),
		ENVIRONMENT("environment"),
		NOSECURITY_USER_EMAIL("server.nosecurity.user.email");

		private final String key;

		private ConfigKeys(String key) {
			this.key = key;
		}

		@Override
		public String getKey() {
			return key;
		}
	}

	private final Config config;

	/**
	 * Constructor
	 * @param config
	 */
	public AppModule(Config config) {
		super();
		this.config = config;
	}

	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing App module");

		bind(AppContext.class).toProvider(new Provider<AppContext>() {

			@Override
			public AppContext get() {
				final String stage = config.getString(ConfigKeys.STAGE.getKey(), AppContext.DEFAULT_STAGE);
				final String environment = config.getString(ConfigKeys.ENVIRONMENT.getKey(), AppContext.DEFAULT_ENVIRONMENT);
				final String dfltUserEmail = config.getString(ConfigKeys.NOSECURITY_USER_EMAIL.getKey());
				return new AppContext(stage, environment, dfltUserEmail);
			}
		}).in(Scopes.SINGLETON);
	}

}
