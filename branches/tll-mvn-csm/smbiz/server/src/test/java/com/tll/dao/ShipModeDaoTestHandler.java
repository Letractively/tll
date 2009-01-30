/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ShipMode;
import com.tll.model.key.PrimaryKey;

/**
 * ShipModeDaoTestHandler
 * @author jpk
 */
public class ShipModeDaoTestHandler extends AbstractEntityDaoTestHandler<ShipMode> {

	PrimaryKey<Account> aKey;

	@Override
	public Class<ShipMode> entityClass() {
		return ShipMode.class;
	}

	@Override
	public void assembleTestEntity(ShipMode e) throws Exception {
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
	public void teardownTestEntity(ShipMode e) {
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
	public void verifyLoadedEntityState(ShipMode e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
	}

}
