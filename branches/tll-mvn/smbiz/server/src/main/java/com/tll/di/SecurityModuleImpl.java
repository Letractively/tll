/*
 * The Logic Lab
 */
package com.tll.di;

import java.util.Arrays;

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.acl.AclManager;
import org.acegisecurity.acl.AclProviderManager;
import org.acegisecurity.acl.basic.BasicAclProvider;
import org.acegisecurity.acl.basic.SimpleAclEntry;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.dao.salt.ReflectionSaltSource;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.acegisecurity.vote.AffirmativeBased;
import org.acegisecurity.vote.RoleVoter;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.tll.dao.IAclDao;
import com.tll.service.acl.AccountRelatedAclProviderManager;
import com.tll.service.acl.AccountRelatedBasicAclEntryVoter;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.service.entity.impl.user.IUserService;

/**
 * SecurityModuleImpl - Acegi flavored security implementation wiring.
 * @author jpk
 */
public class SecurityModuleImpl extends GModule {

	/**
	 * Constructor
	 */
	public SecurityModuleImpl() {
		super();
		log.info("Employing Acegi Security");
	}

	@Override
	protected void configure() {

		// UserDetailsService
		bind(UserDetailsService.class).toProvider(new Provider<UserDetailsService>() {

			@Inject
			IEntityServiceFactory esf;

			public UserDetailsService get() {
				return esf.instance(IUserService.class);
			}
		}).in(Scopes.SINGLETON);

		// SaltSource
		bind(SaltSource.class).toProvider(new Provider<SaltSource>() {

			public SaltSource get() {
				final ReflectionSaltSource rss = new ReflectionSaltSource();
				rss.setUserPropertyToUse("getUsername");
				return rss;
			}

		}).in(Scopes.SINGLETON);

		// security-auth.xml:
		// *****************

		// AccountRelatedAclProviderManager
		bind(AccountRelatedAclProviderManager.class).toProvider(new Provider<AccountRelatedAclProviderManager>() {

			@Inject
			IAclDao aclDao;

			public AccountRelatedAclProviderManager get() {
				final AccountRelatedAclProviderManager m = new AccountRelatedAclProviderManager();
				m.setBasicAclDao(aclDao);
				return m;
			}

		}).in(Scopes.SINGLETON);

		// AclManager
		bind(AclManager.class).toProvider(new Provider<AclManager>() {

			@Inject
			AccountRelatedAclProviderManager accountRelatedProviderManager;

			public AclProviderManager get() {
				final AclProviderManager apm = new AclProviderManager();
				apm.setProviders(Arrays.asList(new BasicAclProvider[] { accountRelatedProviderManager }));
				return apm;
			}

		}).in(Scopes.SINGLETON);

		// AccountRelatedBasicAclEntryVoter (READ)
		bind(AccountRelatedBasicAclEntryVoter.class).annotatedWith(Names.named("READ")).toProvider(
				new Provider<AccountRelatedBasicAclEntryVoter>() {

					public AccountRelatedBasicAclEntryVoter get() {
						final AccountRelatedBasicAclEntryVoter voter = new AccountRelatedBasicAclEntryVoter();
						voter.setRequirePermission(new int[] {
							SimpleAclEntry.ADMINISTRATION, SimpleAclEntry.READ });
						voter.setProcessConfigAttribute("ACL_ACCOUNT_RELATED_READ");
						return voter;
					}

				}).in(Scopes.SINGLETON);

		// AccountRelatedBasicAclEntryVoter (DELETE)
		bind(AccountRelatedBasicAclEntryVoter.class).annotatedWith(Names.named("DELETE")).toProvider(
				new Provider<AccountRelatedBasicAclEntryVoter>() {

					public AccountRelatedBasicAclEntryVoter get() {
						final AccountRelatedBasicAclEntryVoter voter = new AccountRelatedBasicAclEntryVoter();
						voter.setRequirePermission(new int[] {
							SimpleAclEntry.ADMINISTRATION, SimpleAclEntry.DELETE });
						voter.setProcessConfigAttribute("ACL_ACCOUNT_RELATED_DELETE");
						return voter;
					}

				}).in(Scopes.SINGLETON);

		// AccessDecisionManager (accountAccessDecisionManager)
		bind(AccessDecisionManager.class).annotatedWith(Names.named("accountAccessDecisionManager")).toProvider(
				new Provider<AccessDecisionManager>() {

					@Inject
					RoleVoter roleVoter;
					@Inject
					@Named("READ")
					AccountRelatedBasicAclEntryVoter accountRelatedReadVoter;
					@Inject
					@Named("DELETE")
					AccountRelatedBasicAclEntryVoter accountRelatedDeleteVoter;

					public AccessDecisionManager get() {
						final AffirmativeBased p = new AffirmativeBased();
						p.setAllowIfAllAbstainDecisions(false);
						p.setDecisionVoters(Arrays.asList(new AccessDecisionVoter[] {
							roleVoter, accountRelatedReadVoter, accountRelatedDeleteVoter }));
						return p;
					}

				}).in(Scopes.SINGLETON);

	}

}
