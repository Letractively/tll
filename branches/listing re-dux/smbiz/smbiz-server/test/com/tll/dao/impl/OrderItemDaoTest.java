/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Order;
import com.tll.model.impl.OrderItem;
import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;

/**
 * OrderItemDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "OrderItemDaoTest")
public class OrderItemDaoTest extends NamedEntityDaoTest<OrderItem> {

	IPrimaryKey<Account> aKey;
	IPrimaryKey<Order> oKey;

	/**
	 * Constructor
	 */
	public OrderItemDaoTest() {
		super(OrderItem.class, IOrderItemDao.class);
	}

	@Override
	protected void assembleTestEntity(OrderItem e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = KeyFactory.getPrimaryKey(account);
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);

		Order order;
		if(oKey == null) {
			order = getMockEntityProvider().getEntityCopy(Order.class);
			order.setCurrency(account.getCurrency());
			order.setPaymentInfo(null);
			order.setAccount(account);
			order = getDao(IOrderDao.class).persist(order);
			oKey = KeyFactory.getPrimaryKey(order);
		}
		else {
			order = getDao(IOrderDao.class).load(oKey);
		}
		Assert.assertNotNull(order);
		e.setOrder(order);
	}

	@Override
	protected void afterMethodHook() {
		startNewTransaction();
		setComplete();

		if(oKey != null) {
			try {
				final Order order = getDao(IOrderDao.class).load(oKey);
				getDao(IOrderDao.class).purge(order);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			oKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = getDao(IAccountDao.class).load(aKey);
				getDao(IAccountDao.class).purge(account);
				getDao(ICurrencyDao.class).purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		endTransaction();
	}

	@Override
	protected void verifyLoadedEntityState(OrderItem e) throws Exception {
		Assert.assertNotNull(e.getOrder());
		Assert.assertNotNull(e.getParent());
	}

}