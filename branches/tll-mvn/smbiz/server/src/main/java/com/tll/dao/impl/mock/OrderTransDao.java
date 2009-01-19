/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IOrderTransDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.OrderTrans;
import com.tll.model.key.IBusinessKeyFactory;

/**
 * OrderTransDao
 * @author jpk
 */
public class OrderTransDao extends EntityDao<OrderTrans> implements IOrderTransDao, IMockDao<OrderTrans> {

	@Inject
	public OrderTransDao(Set<OrderTrans> set, IBusinessKeyFactory bkf) {
		super(OrderTrans.class, set, bkf);
	}

}
