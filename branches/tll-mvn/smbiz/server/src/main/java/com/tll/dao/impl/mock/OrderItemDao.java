/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IOrderItemDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.OrderItem;

public class OrderItemDao extends EntityDao<OrderItem> implements IOrderItemDao, IMockDao<OrderItem> {

	@Inject
	public OrderItemDao(Set<OrderItem> set) {
		super(OrderItem.class, set);
	}
}