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

	private Long pkC, pkA, pkT;

	@Override
	public Class<User> entityClass() {
		return User.class;
	}

	@Override
	public void doPersistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = currency.getId();

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = account.getId();

		final Authority auth = createAndPersist(Authority.class, true);
		pkT = auth.getId();
	}

	@Override
	public void doPurgeDependentEntities() {
		purge(Authority.class, pkT); pkT = null;
		purge(Account.class, pkA); pkA = null;
		purge(Currency.class, pkC); pkC = null;
	}

	@Override
	public void assembleTestEntity(User e) throws Exception {
		e.setAccount(load(Account.class, pkA));
		e.addAuthority(load(Authority.class, pkT));
	}

	@Override
	public void verifyLoadedEntityState(User e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertTrue(e.getAuthoritys() != null && e.getAuthoritys().size() > 0);
	}

	@Override
	public void alterTestEntity(User e) {
		super.alterTestEntity(e);
		// JDO does *not* support deleting orphaned entities in a related collection!
		// so we omit this here
		//final Authority a = e.getAuthoritys().iterator().next();
		//e.removeAuthority(a);

		e.setName("newness");
	}

	@Override
	public void verifyEntityAlteration(User e) throws Exception {
		// JDO does *not* support deleting orphaned entities in a related collection!
		// so we omit this here
		//super.verifyEntityAlteration(e);
		//Assert.assertTrue(e.getAuthoritys() != null && e.getAuthoritys().size() == 0);

		Assert.assertTrue("newness".equals(e.getName()));
	}
}
