/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Address;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Customer;
import com.tll.model.Order;
import com.tll.model.PaymentInfo;
import com.tll.model.Visitor;

/**
 * OrderDaoTestHandler
 * @author jpk
 */
public class OrderDaoTestHandler extends AbstractEntityDaoTestHandler<Order> {

	Currency currency;
	Address address1;
	Address address2;
	PaymentInfo paymentInfo;
	Account account;
	Visitor visitor;
	Customer customer;

	@Override
	public Class<Order> entityClass() {
		return Order.class;
	}

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);

		address1 = createAndPersist(Address.class, true);
		address2 = createAndPersist(Address.class, true);

		paymentInfo = createAndPersist(PaymentInfo.class, true);

		account = create(Asp.class, true);
		account.setCurrency(currency);
		account.setPaymentInfo(paymentInfo);
		account = persist(account);

		visitor = create(Visitor.class, true);
		visitor.setAccount(account);
		visitor = persist(visitor);

		customer = create(Customer.class, true);
		customer.setCurrency(currency);
		customer.setPaymentInfo(paymentInfo);
		customer.setParent(account);
		customer = persist(customer);
	}

	@Override
	public void purgeDependentEntities() {
		purge(customer);
		purge(visitor);
		purge(account);
		purge(paymentInfo);
		purge(address2);
		purge(address1);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(Order e) throws Exception {
		e.setCurrency(currency);
		e.setBillToAddress(address1);
		e.setShipToAddress(address2);
		e.setPaymentInfo(paymentInfo);
		e.setAccount(account);
		e.setVisitor(visitor);
		e.setCustomer(customer);
	}

	@Override
	public void verifyLoadedEntityState(Order e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getParent());
		Assert.assertNotNull(e.getBillToAddress());
		Assert.assertNotNull(e.getShipToAddress());
		Assert.assertNotNull(e.getCurrency());
		Assert.assertNotNull(e.getPaymentInfo());
		Assert.assertNotNull(e.getVisitor());
		Assert.assertNotNull(e.getCustomer());
	}

	@Override
	public void alterTestEntity(Order e) {
		super.alterTestEntity(e);
		e.setNotes("altered");
	}

	@Override
	public void verifyEntityAlteration(Order e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getNotes(), "altered");
	}

}
