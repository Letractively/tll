/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.GlobalLongPrimaryKey;
import com.tll.model.SalesTax;

/**
 * SalesTaxDaoTestHandler
 * @author jpk
 */
public class SalesTaxDaoTestHandler extends AbstractEntityDaoTestHandler<SalesTax> {

	private GlobalLongPrimaryKey<Currency> pkC;
	private GlobalLongPrimaryKey<Account> pkA;

	@Override
	public Class<SalesTax> entityClass() {
		return SalesTax.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = new GlobalLongPrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new GlobalLongPrimaryKey<Account>(account);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(SalesTax e) throws Exception {
		e.setAccount(load(pkA));
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
