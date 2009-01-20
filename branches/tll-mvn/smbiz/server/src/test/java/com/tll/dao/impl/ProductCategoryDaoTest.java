/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.ProductCategory;
import com.tll.model.key.PrimaryKey;

/**
 * ProductCategoryDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "ProductCategoryDaoTest")
public class ProductCategoryDaoTest extends NamedEntityDaoTest<ProductCategory> {

	PrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public ProductCategoryDaoTest() {
		super(ProductCategory.class, IProductCategoryDao.class);
	}

	@Override
	protected void assembleTestEntity(ProductCategory e) throws Exception {
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
	protected void afterMethodHook() {
		super.afterMethodHook();

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
	protected void verifyLoadedEntityState(ProductCategory e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
	}

}
