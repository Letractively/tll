package com.tll.service.entity.impl.order;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IOrderDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.impl.Order;
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
	public OrderService(IOrderDao dao, IEntityAssembler entityAssembler) {
		super(IOrderDao.class, dao, entityAssembler);
	}

	@Override
	public Class<Order> getEntityClass() {
		return Order.class;
	}
}
