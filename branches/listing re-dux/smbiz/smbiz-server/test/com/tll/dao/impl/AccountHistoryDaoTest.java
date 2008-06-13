/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.AccountHistory;
import com.tll.model.impl.AccountStatus;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;

/**
 * AccountHistoryDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "AccountHistoryDaoTest")
public class AccountHistoryDaoTest extends AbstractDaoTest<AccountHistory> {

	IPrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public AccountHistoryDaoTest() {
		super(AccountHistory.class, IAccountHistoryDao.class);
	}

	@Override
	protected void assembleTestEntity(AccountHistory e) throws Exception {

		e.setPymntTrans(null);

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
		e.setAccount(account);
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
	protected void verifyLoadedEntityState(AccountHistory e) throws Exception {
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	protected void alterEntity(AccountHistory e) {
		e.setStatus(AccountStatus.PROBATION);
	}

	@Override
	protected void verifyEntityAlteration(AccountHistory e) throws Exception {
		Assert.assertEquals(e.getStatus(), AccountStatus.PROBATION);
	}

}
