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
import com.tll.model.key.PrimaryKey;

/**
 * OrderItemDaoTestHandler
 * @author jpk
 */
public class OrderItemDaoTestHandler extends AbstractEntityDaoTestHandler<OrderItem> {

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Account> pkA;
	private PrimaryKey<Order> pkO;

	@Override
	public Class<OrderItem> entityClass() {
		return OrderItem.class;
	}

	@Override
	public void persistDependentEntities() {
		final Currency c = createAndPersist(Currency.class, true);
		pkC = new PrimaryKey<Currency>(c);

		Asp asp = create(Asp.class, true);
		asp.setCurrency(c);
		asp = persist(asp);
		pkA = new PrimaryKey<Account>(asp);

		Order o = create(Order.class, false);
		o.setCurrency(c);
		o.setAccount(asp);
		o = persist(o);
		pkO = new PrimaryKey<Order>(o);
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
