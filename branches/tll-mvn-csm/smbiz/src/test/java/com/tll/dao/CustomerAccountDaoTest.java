/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.AccountStatus;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Customer;
import com.tll.model.CustomerAccount;
import com.tll.model.Visitor;
import com.tll.model.key.PrimaryKey;

/**
 * CustomerAccountDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class CustomerAccountDaoTest extends AbstractEntityDaoTest<CustomerAccount> {

	PrimaryKey<Currency> rKey;
	PrimaryKey<Account> aKey;
	PrimaryKey<Customer> cKey;
	PrimaryKey<Visitor> vKey;

	/**
	 * Constructor
	 */
	public CustomerAccountDaoTest() {
		super(CustomerAccount.class, false);
	}

	@Override
	protected void assembleTestEntity(CustomerAccount e) throws Exception {
		Currency c;
		if(rKey == null) {
			c = getMockEntityFactory().getEntityCopy(Currency.class, true);
			c = getEntityDao().persist(c);
			rKey = new PrimaryKey<Currency>(c);
		}
		else {
			c = getEntityDao().load(rKey);
		}
		Assert.assertNotNull(c);

		Customer customer;
		if(cKey == null) {
			customer = getMockEntityFactory().getEntityCopy(Customer.class, true);
			customer.setParent(null);
			customer.setCurrency(c);
			customer = getEntityDao().persist(customer);
			cKey = new PrimaryKey<Customer>(customer);
		}
		else {
			customer = getEntityDao().load(cKey);
		}
		Assert.assertNotNull(customer);
		e.setCustomer(customer);

		Account account;
		if(aKey == null) {
			account = getMockEntityFactory().getEntityCopy(Asp.class, true);
			account.setPaymentInfo(null);
			account.setParent(null);
			account.setCurrency(c);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Visitor visitor;
		if(vKey == null) {
			visitor = getMockEntityFactory().getEntityCopy(Visitor.class, true);
			visitor.setAccount(account);
			visitor = getEntityDao().persist(visitor);
			vKey = new PrimaryKey<Visitor>(visitor);
		}
		else {
			visitor = getEntityDao().load(vKey);
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
				getEntityDao().purge(getEntityDao().load(vKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			vKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = getEntityDao().load(aKey);
				getEntityDao().purge(account);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(cKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(cKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cKey = null;
		}

		if(rKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(rKey));
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