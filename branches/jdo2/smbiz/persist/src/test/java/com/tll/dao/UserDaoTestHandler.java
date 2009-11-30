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
import com.tll.model.key.PrimaryKey;

/**
 * AbstractEntityDaoTest
 * @author jpk
 */
public class UserDaoTestHandler extends AbstractEntityDaoTestHandler<User> {

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Account> pkA;
	private PrimaryKey<Authority> pkT;

	@Override
	public Class<User> entityClass() {
		return User.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = new PrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new PrimaryKey<Account>(account);

		final Authority auth = createAndPersist(Authority.class, true);
		pkT = new PrimaryKey<Authority>(auth);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkT);
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(User e) throws Exception {
		e.setAccount(load(pkA));
		e.addAuthority(load(pkT));
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
