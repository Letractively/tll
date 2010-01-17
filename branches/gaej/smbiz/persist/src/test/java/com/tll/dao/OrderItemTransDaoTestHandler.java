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
import com.tll.model.key.PrimaryKey;

/**
 * OrderItemTransDaoTestHandler
 * @author jpk
 */
public class OrderItemTransDaoTestHandler extends AbstractEntityDaoTestHandler<OrderItemTrans> {

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Account> pkA;
	private PrimaryKey<Order> pkO;
	private PrimaryKey<OrderTrans> pkOt;

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
		pkC = new PrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new PrimaryKey<Account>(account);

		Order order = create(Order.class, false);
		order.setCurrency(currency);
		order.setAccount(account);
		order.addOrderItem(create(OrderItem.class, true));
		order = persist(order);
		pkO = new PrimaryKey<Order>(order);

		OrderTrans orderTrans = create(OrderTrans.class, true);
		orderTrans.setOrder(order);
		orderTrans.setPymntTrans(null);
		orderTrans = persist(orderTrans);
		pkOt = new PrimaryKey<OrderTrans>(orderTrans);
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