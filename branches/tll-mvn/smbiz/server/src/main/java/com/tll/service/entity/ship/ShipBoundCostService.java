package com.tll.service.entity.ship;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.ShipBoundCost;
import com.tll.service.entity.EntityService;
import com.tll.service.entity.IEntityAssembler;

/**
 * ShipBoundCostService - {@link IShipBoundCostService} impl
 * @author jpk
 */
@Transactional
public class ShipBoundCostService extends EntityService<ShipBoundCost> implements IShipBoundCostService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public ShipBoundCostService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<ShipBoundCost> getEntityClass() {
		return ShipBoundCost.class;
	}
}
