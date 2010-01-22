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
import com.tll.model.GlobalLongPrimaryKey;

/**
 * OrderItemTransDaoTestHandler
 * @author jpk
 */
public class OrderItemTransDaoTestHandler extends AbstractEntityDaoTestHandler<OrderItemTrans> {

	private GlobalLongPrimaryKey<Currency> pkC;
	private GlobalLongPrimaryKey<Account> pkA;
	private GlobalLongPrimaryKey<Order> pkO;
	private GlobalLongPrimaryKey<OrderTrans> pkOt;

	@Override
	public Class<OrderItemTrans> entityClass() {
		return OrderItemTrans.class;
	}

	@Override
	public boolean supportsPaging() {
		// since we can't (currently) create unique multiple instances since the
		// only bk is the binding between pkO item/pkO trans only
		return false;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = new GlobalLongPrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new GlobalLongPrimaryKey<Account>(account);

		Order order = create(Order.class, false);
		order.setCurrency(currency);
		order.setAccount(account);
		order.addOrderItem(create(OrderItem.class, true));
		order = persist(order);
		pkO = new GlobalLongPrimaryKey<Order>(order);

		OrderTrans orderTrans = create(OrderTrans.class, true);
		orderTrans.setOrder(order);
		orderTrans.setPymntTrans(null);
		orderTrans = persist(orderTrans);
		pkOt = new GlobalLongPrimaryKey<OrderTrans>(orderTrans);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkOt);
		purge(pkO);
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(OrderItemTrans e) throws Exception {
		e.setOrderItem(load(pkO).getOrderItems().iterator().next());
		e.setOrderTrans(load(pkOt));
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
