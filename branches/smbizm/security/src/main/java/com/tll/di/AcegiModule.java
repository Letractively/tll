/*
 * The Logic Lab
 */
package com.tll.di;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.providers.ProviderManager;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationProvider;
import org.springframework.security.providers.dao.DaoAuthenticationProvider;
import org.springframework.security.providers.dao.SaltSource;
import org.springframework.security.providers.dao.UserCache;
import org.springframework.security.providers.dao.salt.ReflectionSaltSource;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.security.vote.AffirmativeBased;
import org.springframework.security.vote.RoleVoter;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;

/**
 * AcegiModule - Acegi flavored security implementation wiring.
 * <p>
 * <em>NOTE: </em>This module depends on a {@link UserCache} binding in the
 * dependency injection context.
 * @author jpk
 */
public abstract class AcegiModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(AcegiModule.class);

	/**
	 * The http request flavored {@link AccessDecisionManager} token used to
	 * extract such an instance from the dependency injection context.
	 */
	public static final String ADM_HTTP_REQUEST = "httpRequestAccessDecisionManager";

	/**
	 * ConfigKeys - Config keys for the Acegi module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		APP_NAME("server.app.name");

		private final String key;

		private ConfigKeys(String key) {
			this.key = key;
		}

		@Override
		public String getKey() {
			return key;
		}
	}

	Config config;

	/**
	 * Constructor
	 */
	public AcegiModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public AcegiModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	/**
	 * Necessary provision to bind {@link UserDetailsService}.
	 */
	protected abstract void bindUserDetailsService();

	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing Acegi Security");

		bindUserDetailsService();

		// SaltSource
		bind(SaltSource.class).toProvider(new Provider<SaltSource>() {

			public SaltSource get() {
				final ReflectionSaltSource rss = new ReflectionSaltSource();
				rss.setUserPropertyToUse("getUsername");
				return rss;
			}

		}).in(Scopes.SINGLETON);

		// PasswordEncoder
		bind(PasswordEncoder.class).to(Md5PasswordEncoder.class).in(Scopes.SINGLETON);

		// DaoAuthenticationProvider
		bind(DaoAuthenticationProvider.class).toProvider(new Provider<DaoAuthenticationProvider>() {

			@Inject
			UserDetailsService uds;
			@Inject
			SaltSource ss;
			@Inject
			PasswordEncoder pe;
			@Inject
			UserCache userCache;

			public DaoAuthenticationProvider get() {
				final DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
				dap.setUserDetailsService(uds);
				dap.setSaltSource(ss);
				dap.setPasswordEncoder(pe);
				assert userCache != null;
				dap.setUserCache(userCache);
				return dap;
			}

		}).in(Scopes.SINGLETON);

		// AnonymousAuthenticationProvider
		bind(AnonymousAuthenticationProvider.class).toProvider(new Provider<AnonymousAuthenticationProvider>() {

			public AnonymousAuthenticationProvider get() {
				final AnonymousAuthenticationProvider aap = new AnonymousAuthenticationProvider();
				aap.setKey(config.getString(ConfigKeys.APP_NAME.getKey()));
				return aap;
			}

		}).in(Scopes.SINGLETON);

		// AuthenticationManager
		bind(AuthenticationManager.class).toProvider(new Provider<AuthenticationManager>() {

			@Inject
			DaoAuthenticationProvider dap;
			@Inject
			AnonymousAuthenticationProvider aap;

			public AuthenticationManager get() {
				final ProviderManager pm = new ProviderManager();
				pm.setProviders(Arrays.asList(new Object[] {
					dap, aap }));
				return pm;
			}

		}).in(Scopes.SINGLETON);

		// UserCache (userCache) (this is bound in the entity service module)

		// LoggerListener NOPE: this is a Spring dependent app context based event
		// listener
		// bind(LoggerListener.class);

		// RoleVoter
		bind(RoleVoter.class).in(Scopes.SINGLETON);

		// AccessDecisionManager (ADM_HTTP_REQUEST)
		bind(AccessDecisionManager.class).annotatedWith(Names.named(ADM_HTTP_REQUEST)).toProvider(
				new Provider<AccessDecisionManager>() {

					@Inject
					RoleVoter roleVoter;

					public AccessDecisionManager get() {
						final AffirmativeBased p = new AffirmativeBased();
						p.setAllowIfAllAbstainDecisions(false);
						p.setDecisionVoters(Arrays.asList(new AccessDecisionVoter[] { roleVoter }));
						return p;
					}

				}).in(Scopes.SINGLETON);
	}

}
