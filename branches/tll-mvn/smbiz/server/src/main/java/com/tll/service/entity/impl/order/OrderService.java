package com.tll.service.entity.impl.order;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.EntityAssembler;
import com.tll.model.Order;
import com.tll.service.entity.EntityService;

/**
 * OrderService - {@link IOrderService} impl
 * @author jpk
 */
@Transactional
public class OrderService extends EntityService<Order> implements IOrderService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public OrderService(IEntityDao dao, EntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<Order> getEntityClass() {
		return Order.class;
	}
}
