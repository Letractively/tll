/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.MockEntityProvider;
import com.tll.model.impl.Account;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.ProductGeneral;
import com.tll.model.impl.ProductInventory;
import com.tll.model.key.PrimaryKey;

/**
 * ProductInventoryDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "ProductInventoryDaoTest")
public class ProductInventoryDaoTest extends AbstractDaoTest<ProductInventory> {

	PrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public ProductInventoryDaoTest() {
		super(ProductInventory.class, IProductInventoryDao.class);
	}

	@Override
	protected void assembleTestEntity(ProductInventory e) throws Exception {
		final ProductGeneral gp = getMockEntityProvider().getEntityCopy(ProductGeneral.class, true);
		e.setProductGeneral(gp);
		getEntityAssembler().setGenerated(e.getProductGeneral());

		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class, true);
			account.setCurrency(getDao(ICurrencyDao.class).persist(
					getMockEntityProvider().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

	}

	@Override
	protected void uniquify(ProductInventory e) {
		super.uniquify(e);
		try {
			MockEntityProvider.makeBusinessKeyUnique(e.getProductGeneral());
		}
		catch(final BusinessKeyNotDefinedException e1) {
			Assert.fail(e1.getMessage());
		}
	}

	@Override
	protected void afterMethodHook() {
		if(aKey != null) {
			try {
				final Account account = getDao(IAccountDao.class).load(aKey);
				startNewTransaction();
				setComplete();
				getDao(IAccountDao.class).purge(account);
				getDao(ICurrencyDao.class).purge(account.getCurrency());
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