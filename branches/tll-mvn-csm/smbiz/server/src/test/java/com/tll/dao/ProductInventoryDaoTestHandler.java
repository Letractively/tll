/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.Currency;
import com.tll.model.ProductGeneral;
import com.tll.model.ProductInventory;
import com.tll.model.key.PrimaryKey;

/**
 * ProductInventoryDaoTestHandler
 * @author jpk
 */
public class ProductInventoryDaoTestHandler extends AbstractEntityDaoTestHandler<ProductInventory> {

	PrimaryKey<Account> aKey;

	@Override
	public Class<ProductInventory> entityClass() {
		return ProductInventory.class;
	}

	@Override
	public void assembleTestEntity(ProductInventory e) throws Exception {
		final ProductGeneral gp = mockEntityFactory.getEntityCopy(ProductGeneral.class, true);
		e.setProductGeneral(gp);
		entityFactory.setGenerated(e.getProductGeneral());

		Account account;
		if(aKey == null) {
			account = mockEntityFactory.getEntityCopy(Asp.class, true);
			account.setCurrency(entityDao.persist(mockEntityFactory.getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = entityDao.persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = entityDao.load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

	}

	@Override
	public void makeUnique(ProductInventory e) {
		super.makeUnique(e);
		try {
			mockEntityFactory.makeBusinessKeyUnique(e.getProductGeneral());
		}
		catch(final BusinessKeyNotDefinedException e1) {
			Assert.fail(e1.getMessage());
		}
	}

	@Override
	public void teardownTestEntity(ProductInventory e) {
		if(aKey != null) {
			try {
				final Account account = entityDao.load(aKey);
				entityDao.purge(account);
				entityDao.purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}
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
