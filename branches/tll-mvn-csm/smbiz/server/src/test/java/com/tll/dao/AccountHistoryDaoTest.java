/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.AccountHistory;
import com.tll.model.AccountStatus;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.key.PrimaryKey;

/**
 * AccountHistoryDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "AccountHistoryDaoTest")
public class AccountHistoryDaoTest extends AbstractEntityDaoTest<AccountHistory> {

	PrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public AccountHistoryDaoTest() {
		super(AccountHistory.class, true);
	}

	@Override
	protected void assembleTestEntity(AccountHistory e) throws Exception {

		e.setPymntTrans(null);

		Account account;
		if(aKey == null) {
			account = getMockEntityFactory().getEntityCopy(Asp.class, true);
			account.setCurrency(getEntityDao().persist(getMockEntityFactory().getEntityCopy(Currency.class, true)));
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
