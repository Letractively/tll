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
import com.tll.model.key.PrimaryKey;

/**
 * CustomerAccountDaoTestHandler
 * @author jpk
 */
public class CustomerAccountDaoTestHandler extends AbstractEntityDaoTestHandler<CustomerAccount> {

	PrimaryKey<Currency> pkCurrency;
	PrimaryKey<Account> pkAccount;
	PrimaryKey<Customer> pkCustomer;
	PrimaryKey<Visitor> pkVisitor;

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
		final Currency currency = createAndPersist(Currency.class, true);

		Account account = create(Asp.class, true);
		account.setCurrency(currency);
		account.setParent(null);
		account = persist(account);
		pkAccount = new PrimaryKey<Account>(account);
		pkCurrency = new PrimaryKey<Currency>(account.getCurrency());

		Customer customer = create(Customer.class, true);
		customer.setParent(null);
		customer.setCurrency(currency);
		customer = persist(customer);
		pkCustomer = new PrimaryKey<Customer>(customer);

		Visitor visitor = create(Visitor.class, true);
		visitor.setAccount(account);
		visitor = persist(visitor);
		pkVisitor = new PrimaryKey<Visitor>(visitor);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkVisitor);
		purge(pkCustomer);
		purge(pkAccount);
		purge(pkCurrency);
	}

	@Override
	public void assembleTestEntity(CustomerAccount e) throws Exception {
		e.setCustomer(load(pkCustomer));
		e.setAccount(load(pkAccount));
		e.setInitialVisitorRecord(load(pkVisitor));
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