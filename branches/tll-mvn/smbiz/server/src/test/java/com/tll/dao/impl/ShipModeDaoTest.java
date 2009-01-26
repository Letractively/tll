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
import com.tll.model.ShipMode;
import com.tll.model.key.PrimaryKey;

/**
 * ShipModeDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "ShipModeDaoTest")
public class ShipModeDaoTest extends AbstractEntityDaoTest<ShipMode> {

	PrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public ShipModeDaoTest() {
		super(ShipMode.class, true);
	}

	@Override
	protected void assembleTestEntity(ShipMode e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityFactory().getEntityCopy(Asp.class, true);
			account.setCurrency(getEntityDao().persist(getMockEntityFactory().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);
	}

	@Override
	protected void afterMethodHook() {
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
	protected void verifyLoadedEntityState(ShipMode e) throws Exception {
		Assert.assertNotNull(e.getAccount());
	}

}
