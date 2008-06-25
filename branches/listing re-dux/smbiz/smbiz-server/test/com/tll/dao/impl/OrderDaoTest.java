/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.Address;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Customer;
import com.tll.model.impl.Order;
import com.tll.model.impl.PaymentInfo;
import com.tll.model.impl.Visitor;
import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;

/**
 * OrderDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "OrderDaoTest")
public class OrderDaoTest extends AbstractDaoTest<Order> {

	IPrimaryKey<Currency> cKey;
	IPrimaryKey<Address> btKey;
	IPrimaryKey<Address> stKey;
	IPrimaryKey<PaymentInfo> piKey;
	IPrimaryKey<Visitor> vKey;
	IPrimaryKey<Customer> cstKey;
	IPrimaryKey<Account> aKey;

	/**
	 * Constructor
	 */
	public OrderDaoTest() {
		super(Order.class, IOrderDao.class);
	}

	@Override
	protected void assembleTestEntity(Order e) throws Exception {
		Currency currency;
		if(cKey == null) {
			currency = getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class));
			cKey = KeyFactory.getPrimaryKey(currency);
		}
		else {
			currency = getDao(ICurrencyDao.class).load(cKey);
		}
		Assert.assertNotNull(currency);
		e.setCurrency(currency);

		Address bta;
		if(btKey == null) {
			bta = getDao(IAddressDao.class).persist(getMockEntityProvider().getEntityCopy(Address.class, 1));
			btKey = KeyFactory.getPrimaryKey(bta);
		}
		else {
			bta = getDao(IAddressDao.class).load(btKey);
		}
		Assert.assertNotNull(bta);
		e.setBillToAddress(bta);

		Address sta;
		if(stKey == null) {
			sta = getDao(IAddressDao.class).persist(getMockEntityProvider().getEntityCopy(Address.class, 2));
			stKey = KeyFactory.getPrimaryKey(sta);
		}
		else {
			sta = getDao(IAddressDao.class).load(stKey);
		}
		Assert.assertNotNull(sta);
		e.setShipToAddress(sta);

		PaymentInfo pi;
		if(piKey == null) {
			pi = getDao(IPaymentInfoDao.class).persist(getMockEntityProvider().getEntityCopy(PaymentInfo.class));
			piKey = KeyFactory.getPrimaryKey(pi);
		}
		else {
			pi = getDao(IPaymentInfoDao.class).load(piKey);
		}
		Assert.assertNotNull(pi);
		e.setPaymentInfo(pi);

		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(currency);
			account.setPaymentInfo(pi);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = KeyFactory.getPrimaryKey(account);
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Visitor v;
		if(vKey == null) {
			v = getMockEntityProvider().getEntityCopy(Visitor.class);
			v.setAccount(account);
			v = getDao(IVisitorDao.class).persist(v);
			vKey = KeyFactory.getPrimaryKey(v);
		}
		else {
			v = getDao(IVisitorDao.class).load(vKey);
		}
		Assert.assertNotNull(v);
		e.setVisitor(v);

		Customer customer;
		if(cstKey == null) {
			customer = getMockEntityProvider().getEntityCopy(Customer.class);
			customer.setCurrency(currency);
			customer.setPaymentInfo(pi);
			customer.setParent(account);
			customer = (Customer) getDao(IAccountDao.class).persist(customer);
			cstKey = KeyFactory.getPrimaryKey(customer);
		}
		else {
			customer = (Customer) getDao(IAccountDao.class).load(cstKey);
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
				getDao(IVisitorDao.class).purge(getDao(IVisitorDao.class).load(vKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			vKey = null;
		}

		if(cstKey != null) {
			try {
				getDao(IAccountDao.class).purge(getDao(IAccountDao.class).load(cstKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cstKey = null;
		}

		if(aKey != null) {
			try {
				getDao(IAccountDao.class).purge(getDao(IAccountDao.class).load(aKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(cKey != null) {
			try {
				getDao(ICurrencyDao.class).purge(getDao(ICurrencyDao.class).load(cKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cKey = null;
		}

		if(btKey != null) {
			try {
				getDao(IAddressDao.class).purge(getDao(IAddressDao.class).load(btKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			btKey = null;
		}

		if(stKey != null) {
			try {
				getDao(IAddressDao.class).purge(getDao(IAddressDao.class).load(stKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			stKey = null;
		}

		if(piKey != null) {
			try {
				getDao(IPaymentInfoDao.class).purge(getDao(IPaymentInfoDao.class).load(piKey));
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
