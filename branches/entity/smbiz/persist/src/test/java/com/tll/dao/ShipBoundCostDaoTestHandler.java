/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.GlobalLongPrimaryKey;
import com.tll.model.ShipBoundCost;
import com.tll.model.ShipMode;

/**
 * ShipBoundCostDaoTestHandler
 * @author jpk
 */
public class ShipBoundCostDaoTestHandler extends AbstractEntityDaoTestHandler<ShipBoundCost> {

	private GlobalLongPrimaryKey<Currency> pkC;
	private GlobalLongPrimaryKey<Account> pkA;
	private GlobalLongPrimaryKey<ShipMode> pkS;

	@Override
	public Class<ShipBoundCost> entityClass() {
		return ShipBoundCost.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = new GlobalLongPrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new GlobalLongPrimaryKey<Account>(account);

		ShipMode shipMode = create(ShipMode.class, true);
		shipMode.setAccount(account);
		shipMode = persist(shipMode);
		pkS = new GlobalLongPrimaryKey<ShipMode>(shipMode);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkS);
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(ShipBoundCost e) throws Exception {
		e.setShipMode(load(pkS));
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
