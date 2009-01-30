/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

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

	PrimaryKey<Account> aKey;
	PrimaryKey<Order> oKey;

	@Override
	public Class<OrderTrans> entityClass() {
		return OrderTrans.class;
	}

	@Override
	public void assembleTestEntity(OrderTrans e) throws Exception {
		e.setPymntTrans(null);

		Account account;
		if(aKey == null) {
			account = mockEntityFactory.getEntityCopy(Asp.class, true);
			account.setCurrency(entityDao.persist(mockEntityFactory.getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = entityDao.persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = entityDao.load(aKey);
		}
		Assert.assertNotNull(account);

		Order order;
		if(oKey == null) {
			order = mockEntityFactory.getEntityCopy(Order.class, true);
			order.setCurrency(account.getCurrency());
			order.setPaymentInfo(null);
			order.setAccount(account);
			order = entityDao.persist(order);
			oKey = new PrimaryKey<Order>(order);
		}
		else {
			order = entityDao.load(oKey);
		}
		Assert.assertNotNull(order);
		e.setOrder(order);
	}

	@Override
	public void teardownTestEntity(OrderTrans e) {
		if(oKey != null) {
			try {
				final Order order = entityDao.load(oKey);
				entityDao.purge(order);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			oKey = null;
		}

		if(aKey != null) {
			try {
				final Account account = entityDao.load(aKey);
				entityDao.purge(account);
				entityDao.purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}
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
