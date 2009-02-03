/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ShipMode;

/**
 * ShipModeDaoTestHandler
 * @author jpk
 */
public class ShipModeDaoTestHandler extends AbstractEntityDaoTestHandler<ShipMode> {

	Currency currency;
	Account account;

	@Override
	public Class<ShipMode> entityClass() {
		return ShipMode.class;
	}

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);

		account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
	}

	@Override
	public void purgeDependentEntities() {
		purge(account);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(ShipMode e) throws Exception {
		e.setAccount(account);
	}

	@Override
	public void verifyLoadedEntityState(ShipMode e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
	}

}
