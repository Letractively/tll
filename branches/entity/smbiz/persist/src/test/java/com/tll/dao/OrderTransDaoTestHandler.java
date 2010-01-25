/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Order;
import com.tll.model.OrderTrans;
import com.tll.model.OrderTransOp;

/**
 * OrderTransDaoTestHandler
 * @author jpk
 */
public class OrderTransDaoTestHandler extends AbstractEntityDaoTestHandler<OrderTrans> {

	private Object pkC, pkA, pkO;

	@Override
	public Class<OrderTrans> entityClass() {
		return OrderTrans.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = currency.getId();

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = account.getId();

		Order order = create(Order.class, false);
		order.setCurrency(currency);
		order.setAccount(account);
		order = persist(order);
		pkO = order.getId();
	}

	@Override
	public void purgeDependentEntities() {
		purge(Order.class, pkO);
		purge(Account.class, pkA);
		purge(Currency.class, pkC);
	}

	@Override
	public void assembleTestEntity(OrderTrans e) throws Exception {
		e.setOrder(load(Order.class, pkO));
	}

	@Override
	public void verifyLoadedEntityState(OrderTrans e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getOrder());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	public void alterTestEntity(OrderTrans e) {
		super.alterTestEntity(e);
		e.setOrderTransOp(OrderTransOp.AI);
	}

	@Override
	public void verifyEntityAlteration(OrderTrans e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getOrderTransOp(), OrderTransOp.AI);
	}

}
