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

	private Object pkC, pkAdr1, pkAdr2, pkPI, pkA, pkV, pkCust;

	@Override
	public Class<Order> entityClass() {
		return Order.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency c = createAndPersist(Currency.class, true);
		pkC = c.getId();

		final Address adr1 = createAndPersist(Address.class, true);
		final Address adr2 = createAndPersist(Address.class, true);
		pkAdr1 = adr1.getId();
		pkAdr2 = adr2.getId();

		final PaymentInfo pi = createAndPersist(PaymentInfo.class, true);
		pkPI = pi.getId();

		final Asp asp = create(Asp.class, true);
		asp.setCurrency(c);
		asp.setPaymentInfo(pi);
		pkA = asp.getId();

		Visitor v = create(Visitor.class, true);
		v.setAccount(asp);
		v = persist(v);
		pkV = v.getId();

		Customer cust = create(Customer.class, true);
		cust.setCurrency(c);
		cust.setPaymentInfo(pi);
		cust.setParent(asp);
		cust = persist(cust);
		pkCust = cust.getId();
	}

	@Override
	public void purgeDependentEntities() {
		purge(Customer.class, pkCust);
		purge(Visitor.class, pkV);
		purge(Account.class,pkA);
		purge(PaymentInfo.class, pkPI);
		purge(Address.class, pkAdr2);
		purge(Address.class, pkAdr1);
		purge(Currency.class, pkC);
	}

	@Override
	public void assembleTestEntity(Order e) throws Exception {
		e.setCurrency(load(Currency.class, pkC));
		e.setBillToAddress(load(Address.class, pkAdr1));
		e.setShipToAddress(load(Address.class, pkAdr2));
		e.setPaymentInfo(load(PaymentInfo.class, pkPI));
		e.setAccount(load(Account.class, pkA));
		e.setVisitor(load(Visitor.class, pkV));
		e.setCustomer(load(Customer.class, pkCust));
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
