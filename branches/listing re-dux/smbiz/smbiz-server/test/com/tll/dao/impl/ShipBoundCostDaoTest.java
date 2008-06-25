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
import com.tll.model.impl.ShipBoundCost;
import com.tll.model.impl.ShipMode;
import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;

/**
 * ShipBoundCostDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "ShipBoundCostDaoTest")
public class ShipBoundCostDaoTest extends AbstractDaoTest<ShipBoundCost> {

	IPrimaryKey<Account> aKey;
	IPrimaryKey<ShipMode> smKey;

	/**
	 * Constructor
	 */
	public ShipBoundCostDaoTest() {
		super(ShipBoundCost.class, IShipBoundCostDao.class);
	}

	@Override
	protected void assembleTestEntity(ShipBoundCost e) throws Exception {
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

		ShipMode sm;
		if(smKey == null) {
			sm = getMockEntityProvider().getEntityCopy(ShipMode.class);
			sm.setParent(account);
			sm = getDao(IShipModeDao.class).persist(sm);
			smKey = KeyFactory.getPrimaryKey(sm);
		}
		else {
			sm = getDao(IShipModeDao.class).load(smKey);
		}
		Assert.assertNotNull(sm);
		e.setShipMode(sm);
	}

	@Override
	protected void afterMethodHook() {

		if(smKey != null) {
			try {
				final ShipMode sm = getDao(IShipModeDao.class).load(smKey);
				startNewTransaction();
				setComplete();
				getDao(IShipModeDao.class).purge(sm);
				endTransaction();
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			smKey = null;
		}

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
	protected void verifyLoadedEntityState(ShipBoundCost e) throws Exception {
		Assert.assertNotNull(e.getShipMode());
	}

	@Override
	protected void alterEntity(ShipBoundCost e) {
		e.setCost(44.44f);
	}

	@Override
	protected void verifyEntityAlteration(ShipBoundCost e) throws Exception {
		Assert.assertEquals(e.getCost(), 44.44f);
	}

}
