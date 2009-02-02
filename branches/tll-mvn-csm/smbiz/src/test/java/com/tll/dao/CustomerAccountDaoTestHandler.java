/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.AccountStatus;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Customer;
import com.tll.model.CustomerAccount;
import com.tll.model.Visitor;
import com.tll.model.key.PrimaryKey;

/**
 * CustomerAccountDaoTestHandler
 * @author jpk
 */
public class CustomerAccountDaoTestHandler extends AbstractEntityDaoTestHandler<CustomerAccount> {

	PrimaryKey<Currency> rKey;
	PrimaryKey<Account> aKey;
	PrimaryKey<Customer> cKey;
	PrimaryKey<Visitor> vKey;

	@Override
	public Class<CustomerAccount> entityClass() {
		return CustomerAccount.class;
	}

	@Override
	public void assembleTestEntity(CustomerAccount e) throws Exception {
		Currency c;
		if(rKey == null) {
			c = mockEntityFactory.getEntityCopy(Currency.class, true);
			c = entityDao.persist(c);
			rKey = new PrimaryKey<Currency>(c);
		}
		else {
			c = entityDao.load(rKey);
		}
		Assert.assertNotNull(c);

		Customer customer;
		if(cKey == null) {
			customer = mockEntityFactory.getEntityCopy(Customer.class, true);
			customer.setParent(null);
			customer.setCurrency(c);
			customer = entityDao.persist(customer);
			cKey = new PrimaryKey<Customer>(customer);
		}
		else {
			customer = entityDao.load(cKey);
		}
		Assert.assertNotNull(customer);
		e.setCustomer(customer);

		Account account;
		if(aKey == null) {
			account = mockEntityFactory.getEntityCopy(Asp.class, true);
			account.setPaymentInfo(null);
			account.setParent(null);
			account.setCurrency(c);
			account = entityDao.persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = entityDao.load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Visitor visitor;
		if(vKey == null) {
			visitor = mockEntityFactory.getEntityCopy(Visitor.class, true);
			visitor.setAccount(account);
			visitor = entityDao.persist(visitor);
			vKey = new PrimaryKey<Visitor>(visitor);
		}
		else {
			visitor = entityDao.load(vKey);
		}
		Assert.assertNotNull(visitor);
		e.setInitialVisitorRecord(visitor);
	}

	@Override
	public void teardownTestEntity(CustomerAccount e) {
		if(vKey != null) {
			try {
				entityDao.purge(entityDao.load(vKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			vKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = entityDao.load(aKey);
				entityDao.purge(account);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(cKey != null) {
			try {
				entityDao.purge(entityDao.load(cKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cKey = null;
		}

		if(rKey != null) {
			try {
				entityDao.purge(entityDao.load(rKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			rKey = null;
		}
	}

	@Override
	public void verifyLoadedEntityState(CustomerAccount e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getCustomer());
		Assert.assertNotNull(e.getParent());
		Assert.assertNotNull(e.getInitialVisitorRecord());
	}

	@Override
	public void alterTestEntity(CustomerAccount e) {
		super.alterTestEntity(e);
		Assert.assertFalse(AccountStatus.PROBATION.equals(e.getStatus()));
		e.setStatus(AccountStatus.PROBATION);
	}

	@Override
	public void verifyEntityAlteration(CustomerAccount e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertTrue(AccountStatus.PROBATION.equals(e.getStatus()));
	}
}
