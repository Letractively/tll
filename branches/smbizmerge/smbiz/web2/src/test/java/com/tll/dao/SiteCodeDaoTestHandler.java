/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.SiteCode;

/**
 * SiteCodeDaoTestHandler
 * @author jpk
 */
public class SiteCodeDaoTestHandler extends AbstractEntityDaoTestHandler<SiteCode> {

	private Long pkC, pkA;

	@Override
	public Class<SiteCode> entityClass() {
		return SiteCode.class;
	}

	@Override
	public void doPersistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = currency.getId();

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = account.getId();
	}

	@Override
	public void doPurgeDependentEntities() {
		purge(Account.class, pkA);
		purge(Currency.class, pkC);
	}

	@Override
	public void assembleTestEntity(SiteCode e) throws Exception {
		e.setAccount(load(Account.class, pkA));
	}

	@Override
	public void verifyLoadedEntityState(SiteCode e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
		Assert.assertNotNull(e.getCode());
	}

	@Override
	public void alterTestEntity(SiteCode e) {
		super.alterTestEntity(e);
		e.setCode("altered");
	}

	@Override
	public void verifyEntityAlteration(SiteCode e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getCode(), "altered");
	}

}
