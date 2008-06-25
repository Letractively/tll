/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Authority;
import com.tll.model.impl.Currency;
import com.tll.model.impl.User;
import com.tll.model.key.PrimaryKey;

/**
 * NamedEntityDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class UserDaoTest extends NamedEntityDaoTest<User> {

	PrimaryKey aKey;
	PrimaryKey tKey;

	/**
	 * Constructor
	 */
	public UserDaoTest() {
		super(User.class, IUserDao.class);
	}

	@Override
	protected void assembleTestEntity(User e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = account.getPrimaryKey();
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Authority auth;
		if(tKey == null) {
			auth = getMockEntityProvider().getEntityCopy(Authority.class);
			auth = getDao(IAuthorityDao.class).persist(auth);
			tKey = auth.getPrimaryKey();
		}
		else {
			auth = getDao(IAuthorityDao.class).load(tKey);
		}
		Assert.assertNotNull(auth);
		e.addAuthority(auth);
	}

	@Override
	protected void afterMethodHook() {
		if(aKey != null) {
			try {
				final Account account = getDao(IAccountDao.class).load(aKey);
				startNewTransaction();
				setComplete();
				getDao(IAccountDao.class).purge(account);
				getDao(ICurrencyDao.class).purge(account.getCurrency());
				endTransaction();
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(tKey != null) {
			try {
				final Authority auth = getDao(IAuthorityDao.class).load(tKey);
				startNewTransaction();
				setComplete();
				getDao(IAuthorityDao.class).purge(auth);
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

	@Test(groups = "dao")
	public void testSetCredentials() throws Exception {
		User e = getTestEntity();
		dao.persist(e);
		setComplete();
		endTransaction();
		dbRemove.add(e);

		startNewTransaction();
		getDao(IUserDao.class).setCredentials(e.getId(), "newbie@booble.com", "pswd");
		setComplete();
		endTransaction();
		dbRemove.remove(e);

		dao.clear();
		startNewTransaction();
		e = getEntityFromDb(e.getPrimaryKey());
		Assert.assertEquals(e.getUsername(), "newbie@booble.com", "Usernames don't match on user setCredentials() test");
		Assert.assertEquals(e.getPassword(), "pswd", "Passwords don't match on user setCredentials() test");
		endTransaction();
		dbRemove.add(e);
	}
}
