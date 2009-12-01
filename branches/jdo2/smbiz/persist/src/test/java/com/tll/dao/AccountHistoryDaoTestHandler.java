/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

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

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Asp> pkA;

	@Override
	public Class<AccountHistory> entityClass() {
		return AccountHistory.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		this.pkC = new PrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account.setPaymentInfo(null);
		account.setParent(null);
		account = persist(account);
		this.pkA = new PrimaryKey<Asp>(account);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkA); pkA = null;
		purge(pkC); pkC = null;
	}

	@Override
	public void assembleTestEntity(AccountHistory e) throws Exception {
		e.setAccount(load(pkA));
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
