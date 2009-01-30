/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Visitor;
import com.tll.model.key.PrimaryKey;

/**
 * VisitorDaoTestHandler
 * @author jpk
 */
public class VisitorDaoTestHandler extends AbstractEntityDaoTestHandler<Visitor> {

	PrimaryKey<Account> aKey;

	@Override
	public Class<Visitor> entityClass() {
		return Visitor.class;
	}

	@Override
	public void assembleTestEntity(Visitor e) throws Exception {
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
	public void teardownTestEntity(Visitor e) {
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
