/*
 * The Logic Lab
 */
package com.tll.di;

import java.util.Arrays;

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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.tll.config.Config;
import com.tll.config.IConfigKey;

/**
 * AcegiModule - Acegi flavored security implementation wiring.
 * @author jpk
 */
public class AcegiModule extends GModule {

	/**
	 * ConfigKeys - Config keys for the Acegi module.
	 * @author jpk
	 */
	private static enum ConfigKeys implements IConfigKey {

		APP_NAME("app.name"),
		USER_DETAILS_SERVICE_CLASSNAME("security.acegi.userDetailsService.classname");

		private final String key;

		private ConfigKeys(String key) {
			this.key = key;
		}

		@Override
		public String getKey() {
			return key;
		}
	}

	/**
	 * Constructor
	 */
	public AcegiModule() {
		super();
		log.info("Employing Acegi Security");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {

		// UserDetailsService
		final String cn = Config.instance().getString(ConfigKeys.USER_DETAILS_SERVICE_CLASSNAME.getKey());
		if(cn == null) {
			throw new IllegalStateException("No user details service class name specified in the configuration");
		}
		try {
			Class<? extends UserDetailsService> clz = (Class<? extends UserDetailsService>) Class.forName(cn);
			bind(UserDetailsService.class).to(clz).in(Scopes.SINGLETON);
		}
		catch(ClassNotFoundException e) {
			throw new IllegalStateException("No user details service found for name: " + cn);
		}

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
				aap.setKey(Config.instance().getString(ConfigKeys.APP_NAME.getKey()));
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

		// AccessDecisionManager (httpRequestAccessDecisionManager)
		bind(AccessDecisionManager.class).annotatedWith(Names.named("httpRequestAccessDecisionManager")).toProvider(
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
