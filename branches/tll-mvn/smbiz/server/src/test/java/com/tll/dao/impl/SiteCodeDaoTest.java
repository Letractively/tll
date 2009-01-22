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
import com.tll.model.SiteCode;
import com.tll.model.key.PrimaryKey;

/**
 * SiteCodeDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "SiteCodeDaoTest")
public class SiteCodeDaoTest extends NamedEntityDaoTest<SiteCode> {

	PrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public SiteCodeDaoTest() {
		super(SiteCode.class);
	}

	@Override
	protected void assembleTestEntity(SiteCode e) throws Exception {
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
	protected void verifyLoadedEntityState(SiteCode e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
		Assert.assertNotNull(e.getCode());
	}

	@Override
	protected void alterEntity(SiteCode e) {
		super.alterEntity(e);
		e.setCode("altered");
	}

	@Override
	protected void verifyEntityAlteration(SiteCode e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getCode(), "altered");
	}

}
