/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ShipBoundCost;
import com.tll.model.ShipMode;
import com.tll.model.key.PrimaryKey;

/**
 * ShipBoundCostDaoTestHandler
 * @author jpk
 */
public class ShipBoundCostDaoTestHandler extends AbstractEntityDaoTestHandler<ShipBoundCost> {

	PrimaryKey<Account> aKey;
	PrimaryKey<ShipMode> smKey;

	@Override
	public Class<ShipBoundCost> entityClass() {
		return ShipBoundCost.class;
	}

	@Override
	public void assembleTestEntity(ShipBoundCost e) throws Exception {
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

		ShipMode sm;
		if(smKey == null) {
			sm = mockEntityFactory.getEntityCopy(ShipMode.class, true);
			sm.setParent(account);
			sm = entityDao.persist(sm);
			smKey = new PrimaryKey<ShipMode>(sm);
		}
		else {
			sm = entityDao.load(smKey);
		}
		Assert.assertNotNull(sm);
		e.setShipMode(sm);
	}

	@Override
	public void teardownTestEntity(ShipBoundCost e) {
		if(smKey != null) {
			try {
				final ShipMode sm = entityDao.load(smKey);
				entityDao.purge(sm);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			smKey = null;
		}

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
	public void verifyLoadedEntityState(ShipBoundCost e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getShipMode());
	}

	@Override
	public void alterTestEntity(ShipBoundCost e) {
		super.alterTestEntity(e);
		e.setCost(44.44f);
	}

	@Override
	public void verifyEntityAlteration(ShipBoundCost e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getCost(), 44.44f);
	}

}
