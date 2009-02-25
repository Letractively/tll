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

	Currency currency;
	Account account;
	Order order;

	@Override
	public Class<OrderItem> entityClass() {
		return OrderItem.class;
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
		order = persist(order);
	}

	@Override
	public void purgeDependentEntities() {
		purge(order);
		purge(account);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(OrderItem e) throws Exception {
		e.setOrder(order);
	}

	@Override
	public void verifyLoadedEntityState(OrderItem e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getOrder());
		Assert.assertNotNull(e.getParent());
	}

}
