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
import com.tll.model.key.PrimaryKey;

/**
 * ProdCatDaoTestHandler
 * @author jpk
 */
public class ProdCatDaoTestHandler extends AbstractEntityDaoTestHandler<ProdCat> {

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Account> pkA;
	private PrimaryKey<ProductInventory> pkP;
	private PrimaryKey<ProductCategory> pkCa;

	@Override
	public Class<ProdCat> entityClass() {
		return ProdCat.class;
	}

	@Override
	public boolean supportsPaging() {
		// since we can't (currently) create unique multiple instances since the
		// only bk
		// is the binding between pkP/pkCa only
		return false;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = new PrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new PrimaryKey<Account>(account);

		ProductInventory product = create(ProductInventory.class, true);
		product.setProductGeneral(create(ProductGeneral.class, true));
		product.setParent(account);
		product = persist(product);
		pkP = new PrimaryKey<ProductInventory>(product);

		ProductCategory category = create(ProductCategory.class, true);
		category.setParent(account);
		category = persist(category);
		pkCa = new PrimaryKey<ProductCategory>(category);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkCa);
		purge(pkP);
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(ProdCat e) throws Exception {
		e.setProduct(load(pkP));
		e.setCategory(load(pkCa));
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
