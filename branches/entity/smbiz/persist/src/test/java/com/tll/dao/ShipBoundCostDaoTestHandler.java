/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ShipBoundCost;
import com.tll.model.ShipMode;

/**
 * ShipBoundCostDaoTestHandler
 * @author jpk
 */
public class ShipBoundCostDaoTestHandler extends AbstractEntityDaoTestHandler<ShipBoundCost> {

	private Object pkC, pkA, pkS;

	@Override
	public Class<ShipBoundCost> entityClass() {
		return ShipBoundCost.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = currency.getPrimaryKey();

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = account.getPrimaryKey();

		ShipMode shipMode = create(ShipMode.class, true);
		shipMode.setAccount(account);
		shipMode = persist(shipMode);
		pkS = shipMode.getPrimaryKey();
	}

	@Override
	public void purgeDependentEntities() {
		purge(ShipMode.class, pkS);
		purge(Account.class, pkA);
		purge(Currency.class, pkC);
	}

	@Override
	public void assembleTestEntity(ShipBoundCost e) throws Exception {
		e.setShipMode(load(ShipMode.class, pkS));
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
		Assert.assertTrue(e.getCost() == 44.44f);
	}

}
