/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IOrderDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.Order;

public class OrderDao extends EntityDao<Order> implements IOrderDao, IMockDao<Order> {

	@Inject
	public OrderDao(Set<Order> set) {
		super(Order.class, set);
	}
}