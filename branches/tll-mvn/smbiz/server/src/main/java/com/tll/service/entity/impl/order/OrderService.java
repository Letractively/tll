package com.tll.service.entity.impl.order;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IOrderDao;
import com.tll.model.EntityAssembler;
import com.tll.model.Order;
import com.tll.service.entity.EntityService;

/**
 * OrderService - {@link IOrderService} impl
 * @author jpk
 */
@Transactional
public class OrderService extends EntityService<Order, IOrderDao> implements IOrderService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public OrderService(IOrderDao dao, EntityAssembler entityAssembler) {
		super(IOrderDao.class, dao, entityAssembler);
	}

	@Override
	public Class<Order> getEntityClass() {
		return Order.class;
	}
}
