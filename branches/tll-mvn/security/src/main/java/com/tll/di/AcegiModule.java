/*
 * The Logic Lab
 */
package com.tll.di;

import java.util.Arrays;

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.acl.AclManager;
import org.acegisecurity.acl.basic.SimpleAclEntry;
import org.acegisecurity.afterinvocation.AfterInvocationProviderManager;
import org.acegisecurity.intercept.method.aopalliance.MethodSecurityInterceptor;
import org.acegisecurity.providers.ProviderManager;
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationProvider;
import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.dao.UserCache;
import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.acegisecurity.vote.AffirmativeBased;
import org.acegisecurity.vote.RoleVoter;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.service.acl.afterinvocation.BasicAclEntryAfterInvocationFilteringProvider;
import com.tll.service.acl.afterinvocation.FiltererFactory;

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

		APP_NAME("app.name");

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

	@Override
	protected void configure() {

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

		// AfterInvocationProviderManager
		/*
		 * bind(AfterInvocationProviderManager.class).toProvider(new Provider<AfterInvocationProviderManager>() {
		 * @Inject AfterInvocationProvider afterAclRead; @Inject
		 * AfterInvocationProvider afterAclFilterer; public
		 * AfterInvocationProviderManager get() { AfterInvocationProviderManager p =
		 * new AfterInvocationProviderManager(); p.setProviders(Arrays.asList(new
		 * AfterInvocationProvider[] { afterAclRead, afterAclFilterer } ) ); return
		 * p; } }).in(Scopes.SINGLETON);
		 */

		// FiltererFactory
		bind(FiltererFactory.class);

		// BasicAclEntryAfterInvocationFilteringProvider
		bind(BasicAclEntryAfterInvocationFilteringProvider.class).toProvider(
				new Provider<BasicAclEntryAfterInvocationFilteringProvider>() {

					@Inject
					AclManager aclManager;
					@Inject
					FiltererFactory filtererFactory;

					public BasicAclEntryAfterInvocationFilteringProvider get() {
						final BasicAclEntryAfterInvocationFilteringProvider p = new BasicAclEntryAfterInvocationFilteringProvider();
						p.setProcessConfigAttribute("AFTER_ACL_FILTERER");
						p.setAclManager(aclManager);
						p.setRequirePermission(new int[] {
							SimpleAclEntry.ADMINISTRATION, SimpleAclEntry.READ });
						p.setFiltererFactory(filtererFactory);
						return p;
					}

				}).in(Scopes.SINGLETON);

		// TODO finish SecurityModule - use custom annotations for security method
		// interception?

		// MethodSecurityInterceptor (accountSecurity)
		bind(MethodSecurityInterceptor.class).toProvider(new Provider<MethodSecurityInterceptor>() {

			@Inject
			AuthenticationManager authenticationManager;
			@Inject
			@Named("accountAccessDecisionManager")
			AccessDecisionManager accountAccessDecisionManager;
			@Inject
			AfterInvocationProviderManager afterInvocationManager;

			public MethodSecurityInterceptor get() {
				final MethodSecurityInterceptor i = new MethodSecurityInterceptor();
				i.setAuthenticationManager(authenticationManager);
				i.setAccessDecisionManager(accountAccessDecisionManager);
				i.setAfterInvocationManager(afterInvocationManager);

				return i;
			}

		}).in(Scopes.SINGLETON);

		// bindInterceptor(Matchers.any(), methodMatcher, interceptors)

	}

}
