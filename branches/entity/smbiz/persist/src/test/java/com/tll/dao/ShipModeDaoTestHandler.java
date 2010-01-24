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

	private Object pkC, pkA;

	@Override
	public Class<ShipMode> entityClass() {
		return ShipMode.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = currency.getPrimaryKey();

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = account.getPrimaryKey();
	}

	@Override
	public void purgeDependentEntities() {
		purge(Account.class, pkA);
		purge(Currency.class, pkC);
	}

	@Override
	public void assembleTestEntity(ShipMode e) throws Exception {
		e.setAccount(load(Account.class, pkA));
	}

	@Override
	public void verifyLoadedEntityState(ShipMode e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
	}

}
