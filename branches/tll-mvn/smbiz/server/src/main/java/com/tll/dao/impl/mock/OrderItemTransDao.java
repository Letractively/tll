/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IOrderItemTransDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.OrderItemTrans;
import com.tll.model.key.IBusinessKeyFactory;

public class OrderItemTransDao extends EntityDao<OrderItemTrans> implements IOrderItemTransDao,
		IMockDao<OrderItemTrans> {

	@Inject
	public OrderItemTransDao(Set<OrderItemTrans> set, IBusinessKeyFactory bkf) {
		super(OrderItemTrans.class, set, bkf);
	}
}