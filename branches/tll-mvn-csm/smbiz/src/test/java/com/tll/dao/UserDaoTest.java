/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
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
@Test(groups = "dao")
public class UserDaoTest extends AbstractEntityDaoTest<User> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Authority> tKey;

	/**
	 * Constructor
	 */
	public UserDaoTest() {
		super(User.class, true);
	}

	@Override
	protected void assembleTestEntity(User e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityFactory().getEntityCopy(Asp.class, true);
			account.setCurrency(getEntityDao().persist(getMockEntityFactory().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Authority auth;
		if(tKey == null) {
			auth = getMockEntityFactory().getEntityCopy(Authority.class, true);
			auth = getEntityDao().persist(auth);
			tKey = new PrimaryKey<Authority>(auth);
		}
		else {
			auth = getEntityDao().load(tKey);
		}
		Assert.assertNotNull(auth);
		e.addAuthority(auth);
	}

	@Override
	protected void afterMethodHook() {
		if(aKey != null) {
			try {
				final Account account = getEntityDao().load(aKey);
				startNewTransaction();
				setComplete();
				getEntityDao().purge(account);
				getEntityDao().purge(account.getCurrency());
				endTransaction();
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(tKey != null) {
			try {
				final Authority auth = getEntityDao().load(tKey);
				startNewTransaction();
				setComplete();
				getEntityDao().purge(auth);
				endTransaction();
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			tKey = null;
		}
	}

	@Override
	protected void verifyLoadedEntityState(User e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertTrue(e.getAuthoritys() != null && e.getAuthoritys().size() > 0);
	}

	@Override
	protected void alterEntity(User e) {
		super.alterEntity(e);

		final Authority a = e.getAuthoritys().iterator().next();
		e.removeAuthority(a);
	}

	@Override
	protected void verifyEntityAlteration(User e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertTrue(e.getAuthoritys() != null && e.getAuthoritys().size() == 0);
	}
}