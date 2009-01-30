/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.SalesTax;
import com.tll.model.key.PrimaryKey;

/**
 * SalesTaxDaoTestHandler
 * @author jpk
 */
public class SalesTaxDaoTestHandler extends AbstractEntityDaoTestHandler<SalesTax> {

	PrimaryKey<Account> aKey;

	@Override
	public Class<SalesTax> entityClass() {
		return SalesTax.class;
	}

	@Override
	public void assembleTestEntity(SalesTax e) throws Exception {
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
	public void teardownTestEntity(SalesTax e) {
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
	public void verifyLoadedEntityState(SalesTax e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getTax());
	}

	@Override
	public void alterTestEntity(SalesTax e) {
		super.alterTestEntity(e);
		e.setTax(33.33f);
	}

	@Override
	public void verifyEntityAlteration(SalesTax e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getTax(), 33.33f);
	}

}
