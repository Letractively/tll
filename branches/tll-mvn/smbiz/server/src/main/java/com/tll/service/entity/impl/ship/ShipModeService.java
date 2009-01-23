package com.tll.service.entity.impl.ship;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.ShipMode;
import com.tll.service.entity.NamedEntityService;

/**
 * ShioModeService - {@link IShipModeService} impl
 * @author jpk
 */
@Transactional
public class ShipModeService extends NamedEntityService<ShipMode> implements IShipModeService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public ShipModeService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<ShipMode> getEntityClass() {
		return ShipMode.class;
	}
}
