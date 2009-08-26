package com.tll.service.entity.ship;

import javax.validation.ValidatorFactory;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.ShipBoundCost;
import com.tll.service.entity.EntityService;

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
	 * @param vfactory
	 */
	@Inject
	public ShipBoundCostService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<ShipBoundCost> getEntityClass() {
		return ShipBoundCost.class;
	}
}
