/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

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
public class UserDaoTestHandler extends AbstractEntityDaoTestHandler<User> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Authority> tKey;

	@Override
	public Class<User> entityClass() {
		return User.class;
	}

	@Override
	public void assembleTestEntity(User e) throws Exception {
		Account account;
		if(aKey == null) {
			account = mockEntityFactory.getEntityCopy(Asp.class, true);
			account.setCurrency(entityDao.persist(mockEntityFactory.getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = entityDao.persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = entityDao.load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Authority auth;
		if(tKey == null) {
			auth = mockEntityFactory.getEntityCopy(Authority.class, true);
			auth = entityDao.persist(auth);
			tKey = new PrimaryKey<Authority>(auth);
		}
		else {
			auth = entityDao.load(tKey);
		}
		Assert.assertNotNull(auth);
		e.addAuthority(auth);
	}

	@Override
	public void teardownTestEntity(User e) {
		if(aKey != null) {
			try {
				final Account account = entityDao.load(aKey);
				entityDao.purge(account);
				entityDao.purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(tKey != null) {
			try {
				final Authority auth = entityDao.load(tKey);
				entityDao.purge(auth);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			tKey = null;
		}
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
