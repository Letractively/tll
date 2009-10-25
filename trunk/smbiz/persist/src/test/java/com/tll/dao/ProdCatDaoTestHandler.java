/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ProdCat;
import com.tll.model.ProductCategory;
import com.tll.model.ProductGeneral;
import com.tll.model.ProductInventory;

/**
 * ProdCatDaoTestHandler
 * @author jpk
 */
public class ProdCatDaoTestHandler extends AbstractEntityDaoTestHandler<ProdCat> {

	Currency currency;
	Account account;
	ProductInventory product;
	ProductCategory category;

	@Override
	public Class<ProdCat> entityClass() {
		return ProdCat.class;
	}

	@Override
	public boolean supportsPaging() {
		// since we can't (currently) create unique multiple instances since the
		// only bk
		// is the binding between product/category only
		return false;
	}

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);

		account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);

		product = create(ProductInventory.class, true);
		product.setProductGeneral(create(ProductGeneral.class, true));
		product.setParent(account);
		product = persist(product);

		category = create(ProductCategory.class, true);
		category.setParent(account);
		category = persist(category);
	}

	@Override
	public void purgeDependentEntities() {
		purge(category);
		purge(product);
		purge(account);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(ProdCat e) throws Exception {
		e.setProduct(product);
		e.setCategory(category);
	}

	@Override
	public void verifyLoadedEntityState(ProdCat e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getProduct());
		Assert.assertNotNull(e.getCategory());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	public void alterTestEntity(ProdCat e) {
		super.alterTestEntity(e);
		Assert.assertTrue(e.getIsFeaturedProduct());
		e.setIsFeaturedProduct(false);
	}

	@Override
	public void verifyEntityAlteration(ProdCat e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertFalse(e.getIsFeaturedProduct());
	}

}
