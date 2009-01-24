/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.Currency;
import com.tll.model.ProductGeneral;
import com.tll.model.ProductInventory;
import com.tll.model.key.PrimaryKey;

/**
 * ProductInventoryDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "ProductInventoryDaoTest")
public class ProductInventoryDaoTest extends AbstractEntityDaoTest<ProductInventory> {

	PrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public ProductInventoryDaoTest() {
		super(ProductInventory.class);
	}

	@Override
	protected void assembleTestEntity(ProductInventory e) throws Exception {
		final ProductGeneral gp = getMockEntityProvider().getEntityCopy(ProductGeneral.class, true);
		e.setProductGeneral(gp);
		getEntityFactory().setGenerated(e.getProductGeneral());

		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class, true);
			account.setCurrency(getEntityDao().persist(getMockEntityProvider().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

	}

	@Override
	protected void uniquify(ProductInventory e) {
		super.uniquify(e);
		try {
			getMockEntityProvider().makeBusinessKeyUnique(e.getProductGeneral());
		}
		catch(final BusinessKeyNotDefinedException e1) {
			Assert.fail(e1.getMessage());
		}
	}

	@Override
	protected void afterMethodHook() {
		if(aKey != null) {
			try {
				final Account account = getEntityDao().load(aKey);
				startNewTransaction();
				setComplete();
				getEntityDao().purge(account);
				getEntityDao().purge(account.getCurrency());
				endTransaction();
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}
	}

	@Override
	protected void verifyLoadedEntityState(ProductInventory e) throws Exception {
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
		Assert.assertNotNull(e.getProductGeneral());
	}

	@Override
	protected void alterEntity(ProductInventory e) {
		e.setSku("altered");
	}

	@Override
	protected void verifyEntityAlteration(ProductInventory e) throws Exception {
		Assert.assertEquals(e.getSku(), "altered");
	}

}
