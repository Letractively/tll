/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.AccountHistory;
import com.tll.model.AccountStatus;
import com.tll.model.Asp;
import com.tll.model.Currency;

/**
 * AccountHistoryDaoTestHandler
 * @author jpk
 */
public class AccountHistoryDaoTestHandler extends AbstractEntityDaoTestHandler<AccountHistory> {

	Currency currency;
	Account account;

	@Override
	public Class<AccountHistory> entityClass() {
		return AccountHistory.class;
	}

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);

		account = create(Asp.class, true);
		account.setCurrency(currency);
		account.setPaymentInfo(null);
		account.setParent(null);
		account = persist(account);
	}

	@Override
	public void purgeDependentEntities() {
		purge(account);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(AccountHistory e) throws Exception {
		e.setAccount(account);
		e.setPymntTrans(null);
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
