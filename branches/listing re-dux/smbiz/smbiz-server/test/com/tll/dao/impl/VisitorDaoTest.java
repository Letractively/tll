/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Visitor;
import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;

/**
 * VisitorDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "VisitorDaoTest")
public class VisitorDaoTest extends AbstractDaoTest<Visitor> {

	IPrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public VisitorDaoTest() {
		super(Visitor.class, IVisitorDao.class);
	}

	@Override
	protected void assembleTestEntity(Visitor e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = KeyFactory.getPrimaryKey(account);
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);
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
	}

	@Override
	protected void verifyLoadedEntityState(Visitor e) throws Exception {
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getMc());
	}

	@Override
	protected void alterEntity(Visitor e) {
		e.setMc("altered");
	}

	@Override
	protected void verifyEntityAlteration(Visitor e) throws Exception {
		Assert.assertEquals(e.getMc(), "altered");
	}

}
