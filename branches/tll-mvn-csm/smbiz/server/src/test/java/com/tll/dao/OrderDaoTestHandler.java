/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

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

	PrimaryKey<Currency> cKey;
	PrimaryKey<Address> btKey;
	PrimaryKey<Address> stKey;
	PrimaryKey<PaymentInfo> piKey;
	PrimaryKey<Visitor> vKey;
	PrimaryKey<Customer> cstKey;
	PrimaryKey<Account> aKey;

	@Override
	public Class<Order> entityClass() {
		return Order.class;
	}

	@Override
	public void assembleTestEntity(Order e) throws Exception {
		Currency currency;
		if(cKey == null) {
			currency = entityDao.persist(mockEntityFactory.getEntityCopy(Currency.class, true));
			cKey = new PrimaryKey<Currency>(currency);
		}
		else {
			currency = entityDao.load(cKey);
		}
		Assert.assertNotNull(currency);
		e.setCurrency(currency);

		Address bta;
		if(btKey == null) {
			bta = entityDao.persist(mockEntityFactory.getEntityCopy(Address.class, true));
			btKey = new PrimaryKey<Address>(bta);
		}
		else {
			bta = entityDao.load(btKey);
		}
		Assert.assertNotNull(bta);
		e.setBillToAddress(bta);

		Address sta;
		if(stKey == null) {
			sta = entityDao.persist(mockEntityFactory.getEntityCopy(Address.class, true));
			stKey = new PrimaryKey<Address>(sta);
		}
		else {
			sta = entityDao.load(stKey);
		}
		Assert.assertNotNull(sta);
		e.setShipToAddress(sta);

		PaymentInfo pi;
		if(piKey == null) {
			pi = entityDao.persist(mockEntityFactory.getEntityCopy(PaymentInfo.class, true));
			piKey = new PrimaryKey<PaymentInfo>(pi);
		}
		else {
			pi = entityDao.load(piKey);
		}
		Assert.assertNotNull(pi);
		e.setPaymentInfo(pi);

		Account account;
		if(aKey == null) {
			account = mockEntityFactory.getEntityCopy(Asp.class, true);
			account.setCurrency(currency);
			account.setPaymentInfo(pi);
			account.setParent(null);
			account = entityDao.persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = entityDao.load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Visitor v;
		if(vKey == null) {
			v = mockEntityFactory.getEntityCopy(Visitor.class, true);
			v.setAccount(account);
			v = entityDao.persist(v);
			vKey = new PrimaryKey<Visitor>(v);
		}
		else {
			v = entityDao.load(vKey);
		}
		Assert.assertNotNull(v);
		e.setVisitor(v);

		Customer customer;
		if(cstKey == null) {
			customer = mockEntityFactory.getEntityCopy(Customer.class, true);
			customer.setCurrency(currency);
			customer.setPaymentInfo(pi);
			customer.setParent(account);
			customer = entityDao.persist(customer);
			cstKey = new PrimaryKey<Customer>(customer);
		}
		else {
			customer = entityDao.load(cstKey);
		}
		Assert.assertNotNull(customer);
		e.setCustomer(customer);
	}

	@Override
	public void teardownTestEntity(Order e) {
		if(vKey != null) {
			try {
				entityDao.purge(entityDao.load(vKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			vKey = null;
		}

		if(cstKey != null) {
			try {
				entityDao.purge(entityDao.load(cstKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cstKey = null;
		}

		if(aKey != null) {
			try {
				entityDao.purge(entityDao.load(aKey));
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

		if(btKey != null) {
			try {
				entityDao.purge(entityDao.load(btKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			btKey = null;
		}

		if(stKey != null) {
			try {
				entityDao.purge(entityDao.load(stKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			stKey = null;
		}

		if(piKey != null) {
			try {
				entityDao.purge(entityDao.load(piKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			piKey = null;
		}
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
