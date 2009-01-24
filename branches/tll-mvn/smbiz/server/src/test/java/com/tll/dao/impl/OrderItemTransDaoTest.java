/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Order;
import com.tll.model.OrderItem;
import com.tll.model.OrderItemTrans;
import com.tll.model.OrderItemTransOp;
import com.tll.model.OrderTrans;
import com.tll.model.key.PrimaryKey;

/**
 * OrderItemTransDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class OrderItemTransDaoTest extends AbstractEntityDaoTest<OrderItemTrans> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Order> oKey;
	PrimaryKey<OrderTrans> otKey;

	/**
	 * Constructor
	 */
	public OrderItemTransDaoTest() {
		super(OrderItemTrans.class, false);
	}

	@Override
	protected void assembleTestEntity(OrderItemTrans e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class, true);
			account.setCurrency(getEntityDao().persist(getMockEntityProvider().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);

		Order order;
		OrderItem oi;
		if(oKey == null) {
			order = getMockEntityProvider().getEntityCopy(Order.class, true);
			order.setCurrency(account.getCurrency());
			order.setPaymentInfo(null);
			order.setAccount(account);

			// order item
			oi = getMockEntityProvider().getEntityCopy(OrderItem.class, true);
			oi.setOrder(order);
			order.addOrderItem(oi);
			order = getEntityDao().persist(order);
			oKey = new PrimaryKey<Order>(order);
			oi = order.getOrderItem(oi.getId()); // get non-transient version
		}
		else {
			order = getEntityDao().load(oKey);
			oi = order.getOrderItems().iterator().next();
		}
		Assert.assertNotNull(order);
		Assert.assertNotNull(oi);
		e.setOrderItem(oi);

		OrderTrans ot;
		if(otKey == null) {
			ot = getMockEntityProvider().getEntityCopy(OrderTrans.class, true);
			ot.setOrder(order);
			ot.setPymntTrans(null);
			ot = getEntityDao().persist(ot);
			otKey = new PrimaryKey<OrderTrans>(ot);
		}
		else {
			ot = getEntityDao().load(otKey);
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
				final Order order = getEntityDao().load(oKey);
				getEntityDao().purge(order);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			oKey = null;
			otKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = getEntityDao().load(aKey);
				getEntityDao().purge(account);
				getEntityDao().purge(account.getCurrency());
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
