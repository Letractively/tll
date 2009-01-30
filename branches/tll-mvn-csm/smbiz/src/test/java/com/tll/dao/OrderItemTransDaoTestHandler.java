/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

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
 * OrderItemTransDaoTestHandler
 * @author jpk
 */
public class OrderItemTransDaoTestHandler extends AbstractEntityDaoTestHandler<OrderItemTrans> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Order> oKey;
	PrimaryKey<OrderTrans> otKey;

	@Override
	public Class<OrderItemTrans> entityClass() {
		return OrderItemTrans.class;
	}

	@Override
	public void assembleTestEntity(OrderItemTrans e) throws Exception {
		Account account;
		if(aKey == null) {
			account = mockEntityFactory.getEntityCopy(Asp.class, true);
			account.setCurrency(entityDao.persist(mockEntityFactory.getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = entityDao.persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = entityDao.load(aKey);
		}
		Assert.assertNotNull(account);

		Order order;
		OrderItem oi;
		if(oKey == null) {
			order = mockEntityFactory.getEntityCopy(Order.class, true);
			order.setCurrency(account.getCurrency());
			order.setPaymentInfo(null);
			order.setAccount(account);

			// order item
			oi = mockEntityFactory.getEntityCopy(OrderItem.class, true);
			oi.setOrder(order);
			order.addOrderItem(oi);
			order = entityDao.persist(order);
			oKey = new PrimaryKey<Order>(order);
			oi = order.getOrderItem(oi.getId()); // get non-transient version
		}
		else {
			order = entityDao.load(oKey);
			oi = order.getOrderItems().iterator().next();
		}
		Assert.assertNotNull(order);
		Assert.assertNotNull(oi);
		e.setOrderItem(oi);

		OrderTrans ot;
		if(otKey == null) {
			ot = mockEntityFactory.getEntityCopy(OrderTrans.class, true);
			ot.setOrder(order);
			ot.setPymntTrans(null);
			ot = entityDao.persist(ot);
			otKey = new PrimaryKey<OrderTrans>(ot);
		}
		else {
			ot = entityDao.load(otKey);
		}
		Assert.assertNotNull(ot);
		e.setOrderTrans(ot);
	}

	@Override
	public void teardownTestEntity(OrderItemTrans e) {
		if(oKey != null) {
			try {
				final Order order = entityDao.load(oKey);
				entityDao.purge(order);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			oKey = null;
			otKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = entityDao.load(aKey);
				entityDao.purge(account);
				entityDao.purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}
	}

	@Override
	public void verifyLoadedEntityState(OrderItemTrans e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getOrderItem());
		Assert.assertNotNull(e.getOrderTrans());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	public void alterTestEntity(OrderItemTrans e) {
		super.alterTestEntity(e);
		Assert.assertFalse(OrderItemTransOp.C.equals(e.getOrderItemTransOp()));
		e.setOrderItemTransOp(OrderItemTransOp.C);
	}

	@Override
	public void verifyEntityAlteration(OrderItemTrans e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertTrue(OrderItemTransOp.C.equals(e.getOrderItemTransOp()));
	}

}
