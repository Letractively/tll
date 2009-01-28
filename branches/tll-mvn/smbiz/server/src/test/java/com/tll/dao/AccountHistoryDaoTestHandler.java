/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.AccountHistory;
import com.tll.model.AccountStatus;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.key.PrimaryKey;

/**
 * AccountHistoryDaoTestHandler
 * @author jpk
 */
public class AccountHistoryDaoTestHandler extends AbstractEntityDaoTestHandler<AccountHistory> {

	PrimaryKey<Account> aKey;

	@Override
	public Class<AccountHistory> entityClass() {
		return AccountHistory.class;
	}

	@Override
	public void assembleTestEntity(AccountHistory e) throws Exception {

		e.setPymntTrans(null);

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
	public void teardownTestEntity(AccountHistory e) {
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
	public void verifyLoadedEntityState(AccountHistory e) throws Exception {
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	public void alterTestEntity(AccountHistory e) {
		super.alterTestEntity(e);
		e.setStatus(AccountStatus.PROBATION);
	}

	@Override
	public void verifyEntityAlteration(AccountHistory e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getStatus(), AccountStatus.PROBATION);
	}

}
