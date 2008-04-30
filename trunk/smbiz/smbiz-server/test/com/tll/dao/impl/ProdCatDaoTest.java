/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.ProdCat;
import com.tll.model.impl.ProductCategory;
import com.tll.model.impl.ProductGeneral;
import com.tll.model.impl.ProductInventory;
import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;

/**
 * ProdCatDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class ProdCatDaoTest extends AbstractDaoTest<ProdCat> {

	IPrimaryKey<Account> aKey;
	IPrimaryKey<ProductInventory> pKey;
	IPrimaryKey<ProductCategory> cKey;

	/**
	 * Constructor
	 */
	public ProdCatDaoTest() {
		super(ProdCat.class, IProdCatDao.class, false);
	}

	@Override
	protected void assembleTestEntity(ProdCat e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = KeyFactory.getPrimaryKey(account);
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);

		ProductInventory product;
		if(pKey == null) {
			product = getMockEntityProvider().getEntityCopy(ProductInventory.class);
			final ProductGeneral gp = getMockEntityProvider().getEntityCopy(ProductGeneral.class);
			product.setProductGeneral(gp);
			getEntityAssembler().setGenerated(product.getProductGeneral());
			product.setParent(account);
			product = getDao(IProductInventoryDao.class).persist(product);
			pKey = KeyFactory.getPrimaryKey(product);
		}
		else {
			product = getDao(IProductInventoryDao.class).load(pKey);
		}
		Assert.assertNotNull(product);
		e.setProduct(product);

		ProductCategory category;
		if(cKey == null) {
			category = getMockEntityProvider().getEntityCopy(ProductCategory.class);
			category.setParent(account);
			category = getDao(IProductCategoryDao.class).persist(category);
			cKey = KeyFactory.getPrimaryKey(category);
		}
		else {
			category = getDao(IProductCategoryDao.class).load(cKey);
		}
		Assert.assertNotNull(category);
		e.setCategory(category);
	}

	@Override
	protected void afterMethodHook() {
		startNewTransaction();
		setComplete();

		if(pKey != null) {
			try {
				getDao(IProductInventoryDao.class).purge(getDao(IProductInventoryDao.class).load(pKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			pKey = null;
		}

		if(cKey != null) {
			try {
				getDao(IProductCategoryDao.class).purge(getDao(IProductCategoryDao.class).load(cKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = getDao(IAccountDao.class).load(aKey);
				getDao(IAccountDao.class).purge(account);
				getDao(ICurrencyDao.class).purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		endTransaction();
	}

	@Override
	protected void verifyLoadedEntityState(ProdCat e) throws Exception {
		Assert.assertNotNull(e.getProduct());
		Assert.assertNotNull(e.getCategory());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	protected void alterEntity(ProdCat e) {
		Assert.assertTrue(e.getIsFeaturedProduct());
		e.setIsFeaturedProduct(false);
	}

	@Override
	protected void verifyEntityAlteration(ProdCat e) throws Exception {
		Assert.assertFalse(e.getIsFeaturedProduct());
	}

}
