/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Order;
import com.tll.model.impl.OrderTrans;
import com.tll.model.impl.OrderTransOp;
import com.tll.model.key.PrimaryKey;

/**
 * OrderTransDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class OrderTransDaoTest extends AbstractDaoTest<OrderTrans> {

	PrimaryKey aKey;
	PrimaryKey oKey;

	/**
	 * Constructor
	 */
	public OrderTransDaoTest() {
		super(OrderTrans.class, IOrderTransDao.class);
	}

	@Override
	protected void assembleTestEntity(OrderTrans e) throws Exception {
		e.setPymntTrans(null);

		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = account.getPrimaryKey();
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);

		Order order;
		if(oKey == null) {
			order = getMockEntityProvider().getEntityCopy(Order.class);
			order.setCurrency(account.getCurrency());
			order.setPaymentInfo(null);
			order.setAccount(account);
			order = getDao(IOrderDao.class).persist(order);
			oKey = order.getPrimaryKey();
		}
		else {
			order = getDao(IOrderDao.class).load(oKey);
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
				final Order order = getDao(IOrderDao.class).load(oKey);
				getDao(IOrderDao.class).purge(order);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			oKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = getDao(IAccountDao.class).load(aKey);
				getDao(IAccountDao.class).purge(account);
				getDao(ICurrencyDao.class).purge(account.getCurrency());
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
