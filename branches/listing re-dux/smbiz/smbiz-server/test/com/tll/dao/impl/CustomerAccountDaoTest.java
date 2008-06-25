/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.AccountStatus;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Customer;
import com.tll.model.impl.CustomerAccount;
import com.tll.model.impl.Visitor;
import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;

/**
 * CustomerAccountDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class CustomerAccountDaoTest extends AbstractDaoTest<CustomerAccount> {

	IPrimaryKey<Currency> rKey;
	IPrimaryKey<Account> aKey;
	IPrimaryKey<Customer> cKey;
	IPrimaryKey<Visitor> vKey;

	/**
	 * Constructor
	 */
	public CustomerAccountDaoTest() {
		super(CustomerAccount.class, ICustomerAccountDao.class, false);
	}

	@Override
	protected void assembleTestEntity(CustomerAccount e) throws Exception {
		Currency c;
		if(rKey == null) {
			c = getMockEntityProvider().getEntityCopy(Currency.class);
			c = getDao(ICurrencyDao.class).persist(c);
			rKey = KeyFactory.getPrimaryKey(c);
		}
		else {
			c = getDao(ICurrencyDao.class).load(rKey);
		}
		Assert.assertNotNull(c);

		Customer customer;
		if(cKey == null) {
			customer = getMockEntityProvider().getEntityCopy(Customer.class);
			customer.setParent(null);
			customer.setCurrency(c);
			customer = (Customer) getDao(IAccountDao.class).persist(customer);
			cKey = KeyFactory.getPrimaryKey(customer);
		}
		else {
			customer = (Customer) getDao(IAccountDao.class).load(cKey);
		}
		Assert.assertNotNull(customer);
		e.setCustomer(customer);

		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setPaymentInfo(null);
			account.setParent(null);
			account.setCurrency(c);
			account = getDao(IAccountDao.class).persist(account);
			aKey = KeyFactory.getPrimaryKey(account);
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Visitor visitor;
		if(vKey == null) {
			visitor = getMockEntityProvider().getEntityCopy(Visitor.class);
			visitor.setAccount(account);
			visitor = getDao(IVisitorDao.class).persist(visitor);
			vKey = KeyFactory.getPrimaryKey(visitor);
		}
		else {
			visitor = getDao(IVisitorDao.class).load(vKey);
		}
		Assert.assertNotNull(visitor);
		e.setInitialVisitorRecord(visitor);
	}

	@Override
	protected void afterMethodHook() {
		startNewTransaction();
		setComplete();

		if(vKey != null) {
			try {
				getDao(IVisitorDao.class).purge(getDao(IVisitorDao.class).load(vKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			vKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = getDao(IAccountDao.class).load(aKey);
				getDao(IAccountDao.class).purge(account);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(cKey != null) {
			try {
				getDao(IAccountDao.class).purge(getDao(IAccountDao.class).load(cKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cKey = null;
		}

		if(rKey != null) {
			try {
				getDao(ICurrencyDao.class).purge(getDao(ICurrencyDao.class).load(rKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			rKey = null;
		}

		endTransaction();
	}

	@Override
	protected void verifyLoadedEntityState(CustomerAccount e) throws Exception {
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getCustomer());
		Assert.assertNotNull(e.getParent());
		Assert.assertNotNull(e.getInitialVisitorRecord());
	}

	@Override
	protected void alterEntity(CustomerAccount e) {
		Assert.assertFalse(AccountStatus.PROBATION.equals(e.getStatus()));
		e.setStatus(AccountStatus.PROBATION);
	}

	@Override
	protected void verifyEntityAlteration(CustomerAccount e) throws Exception {
		Assert.assertTrue(AccountStatus.PROBATION.equals(e.getStatus()));
	}

}
