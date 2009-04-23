package com.tll.service.entity.order;

import javax.validation.ValidatorFactory;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
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
	 * @param vfactory
	 */
	@Inject
	public OrderService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<Order> getEntityClass() {
		return Order.class;
	}
}
