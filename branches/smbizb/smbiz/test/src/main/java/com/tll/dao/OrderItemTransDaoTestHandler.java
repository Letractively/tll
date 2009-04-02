/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Order;
import com.tll.model.OrderItem;
import com.tll.model.OrderItemTrans;
import com.tll.model.OrderItemTransOp;
import com.tll.model.OrderTrans;

/**
 * OrderItemTransDaoTestHandler
 * @author jpk
 */
public class OrderItemTransDaoTestHandler extends AbstractEntityDaoTestHandler<OrderItemTrans> {

	Currency currency;
	Account account;
	Order order;
	OrderTrans orderTrans;

	@Override
	public Class<OrderItemTrans> entityClass() {
		return OrderItemTrans.class;
	}

	@Override
	public boolean supportsPaging() {
		// since we can't (currently) create unique multiple instances since the
		// only bk is the binding between order item/order trans only
		return false;
	}

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);

		account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);

		order = create(Order.class, false);
		order.setCurrency(currency);
		order.setAccount(account);
		order.addOrderItem(create(OrderItem.class, true));
		order = persist(order);

		orderTrans = create(OrderTrans.class, true);
		orderTrans.setOrder(order);
		orderTrans.setPymntTrans(null);
		orderTrans = persist(orderTrans);
	}

	@Override
	public void purgeDependentEntities() {
		purge(orderTrans);
		purge(order);
		purge(account);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(OrderItemTrans e) throws Exception {
		e.setOrderItem(order.getOrderItems().iterator().next());
		e.setOrderTrans(orderTrans);
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
