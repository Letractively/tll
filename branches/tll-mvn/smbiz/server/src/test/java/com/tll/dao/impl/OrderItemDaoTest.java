/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Order;
import com.tll.model.OrderItem;
import com.tll.model.key.PrimaryKey;

/**
 * OrderItemDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "OrderItemDaoTest")
public class OrderItemDaoTest extends NamedEntityDaoTest<OrderItem> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Order> oKey;

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
			account = getMockEntityProvider().getEntityCopy(Asp.class, true);
			account.setCurrency(getDao(ICurrencyDao.class).persist(
					getMockEntityProvider().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);

		Order order;
		if(oKey == null) {
			order = getMockEntityProvider().getEntityCopy(Order.class, true);
			order.setCurrency(account.getCurrency());
			order.setPaymentInfo(null);
			order.setAccount(account);
			order = getDao(IOrderDao.class).persist(order);
			oKey = new PrimaryKey<Order>(order);
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
