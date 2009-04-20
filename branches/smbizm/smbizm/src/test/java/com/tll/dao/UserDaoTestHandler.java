/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Authority;
import com.tll.model.Currency;
import com.tll.model.User;

/**
 * AbstractEntityDaoTest
 * @author jpk
 */
public class UserDaoTestHandler extends AbstractEntityDaoTestHandler<User> {

	Currency currency;
	Account account;
	Authority auth;

	@Override
	public Class<User> entityClass() {
		return User.class;
	}

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);

		account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);

		auth = createAndPersist(Authority.class, true);
	}

	@Override
	public void purgeDependentEntities() {
		purge(auth);
		purge(account);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(User e) throws Exception {
		e.setAccount(account);
		e.addAuthority(auth);
	}

	@Override
	public void verifyLoadedEntityState(User e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertTrue(e.getAuthoritys() != null && e.getAuthoritys().size() > 0);
	}

	@Override
	public void alterTestEntity(User e) {
		super.alterTestEntity(e);
		final Authority a = e.getAuthoritys().iterator().next();
		e.removeAuthority(a);
	}

	@Override
	public void verifyEntityAlteration(User e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertTrue(e.getAuthoritys() != null && e.getAuthoritys().size() == 0);
	}
}
