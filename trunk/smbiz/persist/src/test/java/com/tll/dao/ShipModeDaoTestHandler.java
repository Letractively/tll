/*
 * The Logic Lab
 */
package com.tll.dao;

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

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Account> pkA;

	@Override
	public Class<ShipMode> entityClass() {
		return ShipMode.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = new PrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new PrimaryKey<Account>(account);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(ShipMode e) throws Exception {
		e.setAccount(load(pkA));
	}

	@Override
	public void verifyLoadedEntityState(ShipMode e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
	}

}
