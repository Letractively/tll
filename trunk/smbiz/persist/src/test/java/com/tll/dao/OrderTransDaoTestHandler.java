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
import com.tll.model.key.PrimaryKey;

/**
 * OrderTransDaoTestHandler
 * @author jpk
 */
public class OrderTransDaoTestHandler extends AbstractEntityDaoTestHandler<OrderTrans> {

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Account> pkA;
	private PrimaryKey<Order> pkO;

	@Override
	public Class<OrderTrans> entityClass() {
		return OrderTrans.class;
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
		order = persist(order);
		pkO = new PrimaryKey<Order>(order);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkO);
		purge(pkA);
		purge(pkC);
	}

	@Override
	public void assembleTestEntity(OrderTrans e) throws Exception {
		e.setOrder(load(pkO));
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
