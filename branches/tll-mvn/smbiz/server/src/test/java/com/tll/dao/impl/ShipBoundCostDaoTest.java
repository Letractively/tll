/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ShipBoundCost;
import com.tll.model.ShipMode;
import com.tll.model.key.PrimaryKey;

/**
 * ShipBoundCostDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "ShipBoundCostDaoTest")
public class ShipBoundCostDaoTest extends AbstractEntityDaoTest<ShipBoundCost> {

	PrimaryKey<Account> aKey;
	PrimaryKey<ShipMode> smKey;

	/**
	 * Constructor
	 */
	public ShipBoundCostDaoTest() {
		super(ShipBoundCost.class);
	}

	@Override
	protected void assembleTestEntity(ShipBoundCost e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class, true);
			account.setCurrency(getEntityDao().persist(getMockEntityProvider().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);

		ShipMode sm;
		if(smKey == null) {
			sm = getMockEntityProvider().getEntityCopy(ShipMode.class, true);
			sm.setParent(account);
			sm = getEntityDao().persist(sm);
			smKey = new PrimaryKey<ShipMode>(sm);
		}
		else {
			sm = getEntityDao().load(smKey);
		}
		Assert.assertNotNull(sm);
		e.setShipMode(sm);
	}

	@Override
	protected void afterMethodHook() {

		if(smKey != null) {
			try {
				final ShipMode sm = getEntityDao().load(smKey);
				startNewTransaction();
				setComplete();
				getEntityDao().purge(sm);
				endTransaction();
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			smKey = null;
		}

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
