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

	Currency currency;
	Account account;

	@Override
	public Class<ProductCategory> entityClass() {
		return ProductCategory.class;
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
	public void assembleTestEntity(ProductCategory e) throws Exception {
		e.setAccount(account);
	}

	@Override
	public void verifyLoadedEntityState(ProductCategory e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
	}

}
