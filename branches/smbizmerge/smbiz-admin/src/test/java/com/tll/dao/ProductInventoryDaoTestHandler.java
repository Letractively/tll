/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ProductGeneral;
import com.tll.model.ProductInventory;
import com.tll.model.bk.BusinessKeyFactory;

/**
 * ProductInventoryDaoTestHandler
 * @author jpk
 */
public class ProductInventoryDaoTestHandler extends AbstractEntityDaoTestHandler<ProductInventory> {

	private Long pkC, pkA;

	@Override
	public Class<ProductInventory> entityClass() {
		return ProductInventory.class;
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
	public void assembleTestEntity(ProductInventory e) throws Exception {
		e.setProductGeneral(create(ProductGeneral.class, true));
		e.setAccount(load(Account.class, pkA));

	}

	@Override
	public void makeUnique(ProductInventory e) {
		super.makeUnique(e);
		BusinessKeyFactory.makeBusinessKeyUnique(e.getProductGeneral());
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
