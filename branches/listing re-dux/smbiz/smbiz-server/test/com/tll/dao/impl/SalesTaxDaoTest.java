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
import com.tll.model.impl.SalesTax;
import com.tll.model.key.PrimaryKey;

/**
 * SalesTaxDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "SalesTaxDaoTest")
public class SalesTaxDaoTest extends AbstractDaoTest<SalesTax> {

	PrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public SalesTaxDaoTest() {
		super(SalesTax.class, ISalesTaxDao.class);
	}

	@Override
	protected void assembleTestEntity(SalesTax e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = new PrimaryKey<Account>(account);
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
	protected void verifyLoadedEntityState(SalesTax e) throws Exception {
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getTax());
	}

	@Override
	protected void alterEntity(SalesTax e) {
		e.setTax(33.33f);
	}

	@Override
	protected void verifyEntityAlteration(SalesTax e) throws Exception {
		Assert.assertEquals(e.getTax(), 33.33f);
	}

}
