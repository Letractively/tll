/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.SiteCode;
import com.tll.model.key.PrimaryKey;

/**
 * SiteCodeDaoTestHandler
 * @author jpk
 */
public class SiteCodeDaoTestHandler extends AbstractEntityDaoTestHandler<SiteCode> {

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Account> pkA;

	@Override
	public Class<SiteCode> entityClass() {
		return SiteCode.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = new PrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new PrimaryKey<Account>(account);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(SiteCode e) throws Exception {
		e.setAccount(load(pkA));
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
