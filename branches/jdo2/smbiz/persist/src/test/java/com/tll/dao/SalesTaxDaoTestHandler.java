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

	Currency currency;
	Account account;

	@Override
	public Class<SalesTax> entityClass() {
		return SalesTax.class;
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
	public void assembleTestEntity(SalesTax e) throws Exception {
		e.setAccount(account);
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
