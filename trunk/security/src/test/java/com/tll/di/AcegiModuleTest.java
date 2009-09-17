/**
 * The Logic Lab
 * @author jpk
 * @since Apr 3, 2009
 */
package com.tll.di;

import org.springframework.dao.DataAccessException;
import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationProvider;
import org.springframework.security.providers.dao.DaoAuthenticationProvider;
import org.springframework.security.providers.dao.UserCache;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.tll.config.Config;

/**
 * AcegiModuleTest
 * @author jpk
 */
@Test
public class AcegiModuleTest {

	static class TestAcegiModule extends AcegiModule {

		/**
		 * Constructor
		 * @param config
		 */
		public TestAcegiModule(Config config) {
			super(config);
		}

		@Override
		protected void bindUserDetailsService() {
			bind(UserDetailsService.class).toInstance(new UserDetailsService() {

				@Override
				public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
					return null;
				}
			});
		}
	}

	public void testAcegiModuleLoad() throws Exception {
		final Config config = Config.load();
		final Injector i = Guice.createInjector(new Module() {

			@Override
			public void configure(Binder binder) {
				// ad hoc UserCache impl
				binder.bind(UserCache.class).toProvider(new Provider<UserCache>() {

					public UserCache get() {
						return new UserCache() {

							@Override
							public void removeUserFromCache(String username) {
							}

							@Override
							public void putUserInCache(UserDetails user) {
							}

							@Override
							public UserDetails getUserFromCache(String username) {
								return null;
							}
						};
					}

				}).in(Scopes.SINGLETON);
			}
		}, new TestAcegiModule(config));

		// verify dao auth provider
		final DaoAuthenticationProvider p = i.getInstance(DaoAuthenticationProvider.class);
		assert p != null : "Unable to obtain a dao auth provider";

		// verify AnonymousAuthenticationProvider
		final AnonymousAuthenticationProvider ap = i.getInstance(AnonymousAuthenticationProvider.class);
		assert ap != null : "Unable to obtain AnonymousAuthenticationProvider";

		// verify AuthenticationManager
		final AuthenticationManager am = i.getInstance(AuthenticationManager.class);
		assert am != null : "Unable to obtain AuthenticationManager";

		// AccessDecisionManager (ADM_HTTP_REQUEST)
		final AccessDecisionManager adm =
			i.getInstance(Key.get(AccessDecisionManager.class, Names.named(AcegiModule.ADM_HTTP_REQUEST)));
		assert adm != null : "Unable to obtain AccessDecisionManager (ADM_HTTP_REQUEST)";
	}
}
