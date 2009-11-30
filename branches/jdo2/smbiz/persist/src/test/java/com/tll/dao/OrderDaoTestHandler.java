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
import com.tll.model.key.PrimaryKey;

/**
 * OrderDaoTestHandler
 * @author jpk
 */
public class OrderDaoTestHandler extends AbstractEntityDaoTestHandler<Order> {

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Address> pkAdr1, pkAdr2;
	private PrimaryKey<PaymentInfo> pkPI;
	private PrimaryKey<Account> pkA;
	private PrimaryKey<Visitor> pkV;
	private PrimaryKey<Customer> pkCust;

	@Override
	public Class<Order> entityClass() {
		return Order.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency c = createAndPersist(Currency.class, true);
		pkC = new PrimaryKey<Currency>(c);

		final Address adr1 = createAndPersist(Address.class, true);
		final Address adr2 = createAndPersist(Address.class, true);
		pkAdr1 = new PrimaryKey<Address>(adr1);
		pkAdr2 = new PrimaryKey<Address>(adr2);

		final PaymentInfo pi = createAndPersist(PaymentInfo.class, true);
		pkPI = new PrimaryKey<PaymentInfo>(pi);

		final Asp asp = create(Asp.class, true);
		asp.setCurrency(c);
		asp.setPaymentInfo(pi);
		pkA = new PrimaryKey<Account>(asp);

		Visitor v = create(Visitor.class, true);
		v.setAccount(asp);
		v = persist(v);
		pkV = new PrimaryKey<Visitor>(v);

		Customer cust = create(Customer.class, true);
		cust.setCurrency(c);
		cust.setPaymentInfo(pi);
		cust.setParent(asp);
		cust = persist(cust);
		pkCust = new PrimaryKey<Customer>(cust);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkCust);
		purge(pkV);
		purge(pkA);
		purge(pkPI);
		purge(pkAdr2);
		purge(pkAdr1);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(Order e) throws Exception {
		e.setCurrency(load(pkC));
		e.setBillToAddress(load(pkAdr1));
		e.setShipToAddress(load(pkAdr2));
		e.setPaymentInfo(load(pkPI));
		e.setAccount(load(pkA));
		e.setVisitor(load(pkV));
		e.setCustomer(load(pkCust));
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
