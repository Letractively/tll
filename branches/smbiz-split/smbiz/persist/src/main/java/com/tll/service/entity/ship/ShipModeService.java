package com.tll.service.entity.ship;

import javax.validation.ValidatorFactory;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.ShipMode;
import com.tll.service.entity.NamedEntityService;

/**
 * ShioModeService - {@link IShipModeService} impl
 * @author jpk
 */
public class ShipModeService extends NamedEntityService<ShipMode> implements IShipModeService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public ShipModeService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<ShipMode> getEntityClass() {
		return ShipMode.class;
	}
}
