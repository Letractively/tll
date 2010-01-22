/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.GlobalLongPrimaryKey;
import com.tll.model.Visitor;

/**
 * VisitorDaoTestHandler
 * @author jpk
 */
public class VisitorDaoTestHandler extends AbstractEntityDaoTestHandler<Visitor> {

	private GlobalLongPrimaryKey<Currency> pkC;
	private GlobalLongPrimaryKey<Account> pkA;

	@Override
	public Class<Visitor> entityClass() {
		return Visitor.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = new GlobalLongPrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new GlobalLongPrimaryKey<Account>(account);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(Visitor e) throws Exception {
		e.setAccount(load(pkA));
	}

	@Override
	public void verifyLoadedEntityState(Visitor e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getMc());
	}

	@Override
	public void alterTestEntity(Visitor e) {
		super.alterTestEntity(e);
		e.setMc("altered");
	}

	@Override
	public void verifyEntityAlteration(Visitor e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getMc(), "altered");
	}

}
