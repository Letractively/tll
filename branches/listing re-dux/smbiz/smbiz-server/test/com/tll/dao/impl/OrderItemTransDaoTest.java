/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Order;
import com.tll.model.impl.OrderItem;
import com.tll.model.impl.OrderItemTrans;
import com.tll.model.impl.OrderItemTransOp;
import com.tll.model.impl.OrderTrans;
import com.tll.model.key.PrimaryKey;

/**
 * OrderItemTransDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class OrderItemTransDaoTest extends AbstractDaoTest<OrderItemTrans> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Order> oKey;
	PrimaryKey<OrderTrans> otKey;

	/**
	 * Constructor
	 */
	public OrderItemTransDaoTest() {
		super(OrderItemTrans.class, IOrderItemTransDao.class, false);
	}

	@Override
	protected void assembleTestEntity(OrderItemTrans e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class)));
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
		OrderItem oi;
		if(oKey == null) {
			order = getMockEntityProvider().getEntityCopy(Order.class);
			order.setCurrency(account.getCurrency());
			order.setPaymentInfo(null);
			order.setAccount(account);

			// order item
			oi = getMockEntityProvider().getEntityCopy(OrderItem.class);
			oi.setOrder(order);
			order.addOrderItem(oi);
			order = getDao(IOrderDao.class).persist(order);
			oKey = new PrimaryKey<Order>(order);
			oi = order.getOrderItem(oi.getId()); // get non-transient version
		}
		else {
			order = getDao(IOrderDao.class).load(oKey);
			oi = order.getOrderItems().iterator().next();
		}
		Assert.assertNotNull(order);
		Assert.assertNotNull(oi);
		e.setOrderItem(oi);

		OrderTrans ot;
		if(otKey == null) {
			ot = getMockEntityProvider().getEntityCopy(OrderTrans.class);
			ot.setOrder(order);
			ot.setPymntTrans(null);
			ot = getDao(IOrderTransDao.class).persist(ot);
			otKey = new PrimaryKey<OrderTrans>(ot);
		}
		else {
			ot = getDao(IOrderTransDao.class).load(otKey);
		}
		Assert.assertNotNull(ot);
		e.setOrderTrans(ot);
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
			otKey = null;
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
	protected void verifyLoadedEntityState(OrderItemTrans e) throws Exception {
		Assert.assertNotNull(e.getOrderItem());
		Assert.assertNotNull(e.getOrderTrans());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	protected void alterEntity(OrderItemTrans e) {
		Assert.assertFalse(OrderItemTransOp.C.equals(e.getOrderItemTransOp()));
		e.setOrderItemTransOp(OrderItemTransOp.C);
	}

	@Override
	protected void verifyEntityAlteration(OrderItemTrans e) throws Exception {
		Assert.assertTrue(OrderItemTransOp.C.equals(e.getOrderItemTransOp()));
	}

}
