/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ProductCategory;

/**
 * ProductCategoryDaoTestHandler
 * @author jpk
 */
public class ProductCategoryDaoTestHandler extends AbstractEntityDaoTestHandler<ProductCategory> {

	private Object pkC, pkA;

	@Override
	public Class<ProductCategory> entityClass() {
		return ProductCategory.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = currency.getId();

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = account.getId();
	}

	@Override
	public void purgeDependentEntities() {
		purge(Account.class, pkA);
		purge(Currency.class, pkC);
	}

	@Override
	public void assembleTestEntity(ProductCategory e) throws Exception {
		e.setAccount(load(Account.class, pkA));
	}

	@Override
	public void verifyLoadedEntityState(ProductCategory e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
	}

}
