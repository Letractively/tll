/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.SalesTax;

/**
 * SalesTaxDaoTestHandler
 * @author jpk
 */
public class SalesTaxDaoTestHandler extends AbstractEntityDaoTestHandler<SalesTax> {

	private Long pkC, pkA;

	@Override
	public Class<SalesTax> entityClass() {
		return SalesTax.class;
	}

	@Override
	public void doPersistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = currency.getId();

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = account.getId();
	}

	@Override
	public void doPurgeDependentEntities() {
		purge(Account.class, pkA);
		purge(Currency.class, pkC);
	}

	@Override
	public void assembleTestEntity(SalesTax e) throws Exception {
		e.setAccount(load(Account.class, pkA));
	}

	@Override
	public void verifyLoadedEntityState(SalesTax e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertTrue(e.getTax() != 0f);
	}

	@Override
	public void alterTestEntity(SalesTax e) {
		super.alterTestEntity(e);
		e.setTax(33.33f);
	}

	@Override
	public void verifyEntityAlteration(SalesTax e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertTrue(e.getTax() == 33.33f);
	}

}
