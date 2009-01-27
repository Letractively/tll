/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Order;
import com.tll.model.OrderTrans;
import com.tll.model.OrderTransOp;
import com.tll.model.key.PrimaryKey;

/**
 * OrderTransDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class OrderTransDaoTest extends AbstractEntityDaoTest<OrderTrans> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Order> oKey;

	/**
	 * Constructor
	 */
	public OrderTransDaoTest() {
		super(OrderTrans.class, true);
	}

	@Override
	protected void assembleTestEntity(OrderTrans e) throws Exception {
		e.setPymntTrans(null);

		Account account;
		if(aKey == null) {
			account = getMockEntityFactory().getEntityCopy(Asp.class, true);
			account.setCurrency(getEntityDao().persist(getMockEntityFactory().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);

		Order order;
		if(oKey == null) {
			order = getMockEntityFactory().getEntityCopy(Order.class, true);
			order.setCurrency(account.getCurrency());
			order.setPaymentInfo(null);
			order.setAccount(account);
			order = getEntityDao().persist(order);
			oKey = new PrimaryKey<Order>(order);
		}
		else {
			order = getEntityDao().load(oKey);
		}
		Assert.assertNotNull(order);
		e.setOrder(order);
	}

	@Override
	protected void afterMethodHook() {
		startNewTransaction();
		setComplete();

		if(oKey != null) {
			try {
				final Order order = getEntityDao().load(oKey);
				getEntityDao().purge(order);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			oKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = getEntityDao().load(aKey);
				getEntityDao().purge(account);
				getEntityDao().purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		endTransaction();
	}

	@Override
	protected void verifyLoadedEntityState(OrderTrans e) throws Exception {
		Assert.assertNotNull(e.getOrder());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	protected void alterEntity(OrderTrans e) {
		e.setOrderTransOp(OrderTransOp.AI);
	}

	@Override
	protected void verifyEntityAlteration(OrderTrans e) throws Exception {
		Assert.assertEquals(e.getOrderTransOp(), OrderTransOp.AI);
	}

}
