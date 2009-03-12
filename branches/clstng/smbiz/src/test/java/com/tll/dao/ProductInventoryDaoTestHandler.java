/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.mock.model.MockEntityFactory;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ProductGeneral;
import com.tll.model.ProductInventory;

/**
 * ProductInventoryDaoTestHandler
 * @author jpk
 */
public class ProductInventoryDaoTestHandler extends AbstractEntityDaoTestHandler<ProductInventory> {

	Currency currency;
	Account account;

	@Override
	public Class<ProductInventory> entityClass() {
		return ProductInventory.class;
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
	public void assembleTestEntity(ProductInventory e) throws Exception {
		e.setProductGeneral(create(ProductGeneral.class, true));
		e.setAccount(account);

	}

	@Override
	public void makeUnique(ProductInventory e) {
		super.makeUnique(e);
		MockEntityFactory.makeBusinessKeyUnique(e.getProductGeneral());
	}

	@Override
	public void verifyLoadedEntityState(ProductInventory e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
		Assert.assertNotNull(e.getProductGeneral());
	}

	@Override
	public void alterTestEntity(ProductInventory e) {
		super.alterTestEntity(e);
		e.setSku("altered");
	}

	@Override
	public void verifyEntityAlteration(ProductInventory e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getSku(), "altered");
	}

}
