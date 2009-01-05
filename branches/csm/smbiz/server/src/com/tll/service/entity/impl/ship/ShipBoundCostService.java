package com.tll.service.entity.impl.ship;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IShipBoundCostDao;
import com.tll.model.EntityAssembler;
import com.tll.model.impl.ShipBoundCost;
import com.tll.service.entity.EntityService;

/**
 * ShipBoundCostService - {@link IShipBoundCostService} impl
 * @author jpk
 */
@Transactional
public class ShipBoundCostService extends EntityService<ShipBoundCost, IShipBoundCostDao> implements IShipBoundCostService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public ShipBoundCostService(IShipBoundCostDao dao, EntityAssembler entityAssembler) {
		super(IShipBoundCostDao.class, dao, entityAssembler);
	}

	@Override
	public Class<ShipBoundCost> getEntityClass() {
		return ShipBoundCost.class;
	}
}
