/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

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
@Test(groups = "dao")
public class ProdCatDaoTestHandler extends AbstractEntityDaoTestHandler<ProdCat> {

	PrimaryKey<Account> aKey;
	PrimaryKey<ProductInventory> pKey;
	PrimaryKey<ProductCategory> cKey;

	@Override
	public Class<ProdCat> entityClass() {
		return ProdCat.class;
	}

	@Override
	public void assembleTestEntity(ProdCat e) throws Exception {
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

		ProductInventory product;
		if(pKey == null) {
			product = mockEntityFactory.getEntityCopy(ProductInventory.class, true);
			final ProductGeneral gp = mockEntityFactory.getEntityCopy(ProductGeneral.class, true);
			product.setProductGeneral(gp);
			entityFactory.setGenerated(product.getProductGeneral());
			product.setParent(account);
			product = entityDao.persist(product);
			pKey = new PrimaryKey<ProductInventory>(product);
		}
		else {
			product = entityDao.load(pKey);
		}
		Assert.assertNotNull(product);
		e.setProduct(product);

		ProductCategory category;
		if(cKey == null) {
			category = mockEntityFactory.getEntityCopy(ProductCategory.class, true);
			category.setParent(account);
			category = entityDao.persist(category);
			cKey = new PrimaryKey<ProductCategory>(category);
		}
		else {
			category = entityDao.load(cKey);
		}
		Assert.assertNotNull(category);
		e.setCategory(category);
	}

	@Override
	public void teardownTestEntity(ProdCat e) {
		if(pKey != null) {
			try {
				entityDao.purge(entityDao.load(pKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			pKey = null;
		}

		if(cKey != null) {
			try {
				entityDao.purge(entityDao.load(cKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cKey = null;
		}

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
