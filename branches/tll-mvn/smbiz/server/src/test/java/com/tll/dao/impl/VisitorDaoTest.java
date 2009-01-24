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
import com.tll.model.Currency;
import com.tll.model.Visitor;
import com.tll.model.key.PrimaryKey;

/**
 * VisitorDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "VisitorDaoTest")
public class VisitorDaoTest extends AbstractEntityDaoTest<Visitor> {

	PrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public VisitorDaoTest() {
		super(Visitor.class);
	}

	@Override
	protected void assembleTestEntity(Visitor e) throws Exception {
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
	protected void verifyLoadedEntityState(Visitor e) throws Exception {
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getMc());
	}

	@Override
	protected void alterEntity(Visitor e) {
		e.setMc("altered");
	}

	@Override
	protected void verifyEntityAlteration(Visitor e) throws Exception {
		Assert.assertEquals(e.getMc(), "altered");
	}

}
