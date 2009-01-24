/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
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
 * OrderDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "OrderDaoTest")
public class OrderDaoTest extends AbstractEntityDaoTest<Order> {

	PrimaryKey<Currency> cKey;
	PrimaryKey<Address> btKey;
	PrimaryKey<Address> stKey;
	PrimaryKey<PaymentInfo> piKey;
	PrimaryKey<Visitor> vKey;
	PrimaryKey<Customer> cstKey;
	PrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public OrderDaoTest() {
		super(Order.class);
	}

	@Override
	protected void assembleTestEntity(Order e) throws Exception {
		Currency currency;
		if(cKey == null) {
			currency = getEntityDao().persist(getMockEntityProvider().getEntityCopy(Currency.class, true));
			cKey = new PrimaryKey<Currency>(currency);
		}
		else {
			currency = getEntityDao().load(cKey);
		}
		Assert.assertNotNull(currency);
		e.setCurrency(currency);

		Address bta;
		if(btKey == null) {
			bta = getEntityDao().persist(getMockEntityProvider().getEntityCopy(Address.class, true));
			btKey = new PrimaryKey<Address>(bta);
		}
		else {
			bta = getEntityDao().load(btKey);
		}
		Assert.assertNotNull(bta);
		e.setBillToAddress(bta);

		Address sta;
		if(stKey == null) {
			sta = getEntityDao().persist(getMockEntityProvider().getEntityCopy(Address.class, true));
			stKey = new PrimaryKey<Address>(sta);
		}
		else {
			sta = getEntityDao().load(stKey);
		}
		Assert.assertNotNull(sta);
		e.setShipToAddress(sta);

		PaymentInfo pi;
		if(piKey == null) {
			pi = getEntityDao().persist(getMockEntityProvider().getEntityCopy(PaymentInfo.class, true));
			piKey = new PrimaryKey<PaymentInfo>(pi);
		}
		else {
			pi = getEntityDao().load(piKey);
		}
		Assert.assertNotNull(pi);
		e.setPaymentInfo(pi);

		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class, true);
			account.setCurrency(currency);
			account.setPaymentInfo(pi);
			account.setParent(null);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Visitor v;
		if(vKey == null) {
			v = getMockEntityProvider().getEntityCopy(Visitor.class, true);
			v.setAccount(account);
			v = getEntityDao().persist(v);
			vKey = new PrimaryKey<Visitor>(v);
		}
		else {
			v = getEntityDao().load(vKey);
		}
		Assert.assertNotNull(v);
		e.setVisitor(v);

		Customer customer;
		if(cstKey == null) {
			customer = getMockEntityProvider().getEntityCopy(Customer.class, true);
			customer.setCurrency(currency);
			customer.setPaymentInfo(pi);
			customer.setParent(account);
			customer = getEntityDao().persist(customer);
			cstKey = new PrimaryKey<Customer>(customer);
		}
		else {
			customer = getEntityDao().load(cstKey);
		}
		Assert.assertNotNull(customer);
		e.setCustomer(customer);
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

		if(cstKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(cstKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cstKey = null;
		}

		if(aKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(aKey));
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

		if(btKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(btKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			btKey = null;
		}

		if(stKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(stKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			stKey = null;
		}

		if(piKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(piKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			piKey = null;
		}

		endTransaction();
	}

	@Override
	protected void verifyLoadedEntityState(Order e) throws Exception {
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
	protected void alterEntity(Order e) {
		e.setNotes("altered");
	}

	@Override
	protected void verifyEntityAlteration(Order e) throws Exception {
		Assert.assertEquals(e.getNotes(), "altered");
	}

}
