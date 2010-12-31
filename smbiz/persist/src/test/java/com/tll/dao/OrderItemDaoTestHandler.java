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

/**
 * OrderItemDaoTestHandler
 * @author jpk
 */
public class OrderItemDaoTestHandler extends AbstractEntityDaoTestHandler<OrderItem> {

	private Long pkC, pkA, pkO;

	@Override
	public Class<OrderItem> entityClass() {
		return OrderItem.class;
	}

	@Override
	public void doPersistDependentEntities() {
		final Currency c = createAndPersist(Currency.class, true);
		pkC = c.getId();

		Asp asp = create(Asp.class, true);
		asp.setCurrency(c);
		asp = persist(asp);
		pkA = asp.getId();

		Order o = create(Order.class, false);
		o.setCurrency(c);
		o.setAccount(asp);
		o = persist(o);
		pkO = o.getId();
	}

	@Override
	public void doPurgeDependentEntities() {
		purge(Order.class, pkO);
		purge(Account.class, pkA);
		purge(Currency.class, pkC);
	}

	@Override
	public void assembleTestEntity(OrderItem e) throws Exception {
		e.setOrder(load(Order.class, pkO));
	}

	@Override
	public void verifyLoadedEntityState(OrderItem e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getOrder());
		Assert.assertNotNull(e.getParent());
	}

}
