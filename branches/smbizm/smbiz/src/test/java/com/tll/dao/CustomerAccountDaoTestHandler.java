/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.AccountStatus;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Customer;
import com.tll.model.CustomerAccount;
import com.tll.model.Visitor;

/**
 * CustomerAccountDaoTestHandler
 * @author jpk
 */
public class CustomerAccountDaoTestHandler extends AbstractEntityDaoTestHandler<CustomerAccount> {

	Currency currency;
	Account account;
	Customer customer;
	Visitor visitor;

	@Override
	public Class<CustomerAccount> entityClass() {
		return CustomerAccount.class;
	}

	@Override
	public boolean supportsPaging() {
		// since we can't (currently) create unique multiple instances since the
		// only bk is the binding between account/customer only
		return false;
	}

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);

		account = create(Asp.class, true);
		account.setCurrency(currency);
		account.setParent(null);
		account = persist(account);

		customer = create(Customer.class, true);
		customer.setParent(null);
		customer.setCurrency(currency);
		customer = persist(customer);

		visitor = create(Visitor.class, true);
		visitor.setAccount(account);
		visitor = persist(visitor);
	}

	@Override
	public void purgeDependentEntities() {
		purge(visitor);
		purge(customer);
		purge(account);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(CustomerAccount e) throws Exception {
		e.setCustomer(customer);
		e.setAccount(account);
		e.setInitialVisitorRecord(visitor);
	}

	@Override
	public void verifyLoadedEntityState(CustomerAccount e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getCustomer());
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
