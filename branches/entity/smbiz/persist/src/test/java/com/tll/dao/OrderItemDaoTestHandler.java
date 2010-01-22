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
import com.tll.model.GlobalLongPrimaryKey;

/**
 * OrderItemDaoTestHandler
 * @author jpk
 */
public class OrderItemDaoTestHandler extends AbstractEntityDaoTestHandler<OrderItem> {

	private GlobalLongPrimaryKey<Currency> pkC;
	private GlobalLongPrimaryKey<Account> pkA;
	private GlobalLongPrimaryKey<Order> pkO;

	@Override
	public Class<OrderItem> entityClass() {
		return OrderItem.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency c = createAndPersist(Currency.class, true);
		pkC = new GlobalLongPrimaryKey<Currency>(c);

		Asp asp = create(Asp.class, true);
		asp.setCurrency(c);
		asp = persist(asp);
		pkA = new GlobalLongPrimaryKey<Account>(asp);

		Order o = create(Order.class, false);
		o.setCurrency(c);
		o.setAccount(asp);
		o = persist(o);
		pkO = new GlobalLongPrimaryKey<Order>(o);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkO);
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(OrderItem e) throws Exception {
		e.setOrder(load(pkO));
	}

	@Override
	public void verifyLoadedEntityState(OrderItem e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getOrder());
		Assert.assertNotNull(e.getParent());
	}

}
