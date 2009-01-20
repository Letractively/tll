package com.tll.dao.impl;

import com.tll.dao.INamedEntityDao;
import com.tll.dao.ITimeStampEntityDao;
import com.tll.model.OrderItem;


/**
 * @author jpk
 */
public interface IOrderItemDao extends ITimeStampEntityDao<OrderItem>, INamedEntityDao<OrderItem> {
}