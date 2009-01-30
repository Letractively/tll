/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.SiteCode;
import com.tll.model.key.PrimaryKey;

/**
 * SiteCodeDaoTestHandler
 * @author jpk
 */
public class SiteCodeDaoTestHandler extends AbstractEntityDaoTestHandler<SiteCode> {

	PrimaryKey<Account> aKey;

	@Override
	public Class<SiteCode> entityClass() {
		return SiteCode.class;
	}

	@Override
	public void assembleTestEntity(SiteCode e) throws Exception {
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
	}

	@Override
	public void teardownTestEntity(SiteCode e) {
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
	}

	@Override
	public void verifyLoadedEntityState(SiteCode e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
		Assert.assertNotNull(e.getCode());
	}

	@Override
	public void alterTestEntity(SiteCode e) {
		super.alterTestEntity(e);
		e.setCode("altered");
	}

	@Override
	public void verifyEntityAlteration(SiteCode e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getCode(), "altered");
	}

}
