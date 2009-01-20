package com.tll.service.entity.impl.ship;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IShipModeDao;
import com.tll.model.EntityAssembler;
import com.tll.model.ShipMode;
import com.tll.service.entity.EntityService;

/**
 * ShioModeService - {@link IShipModeService} impl
 * @author jpk
 */
@Transactional
public class ShipModeService extends EntityService<ShipMode, IShipModeDao> implements IShipModeService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public ShipModeService(IShipModeDao dao, EntityAssembler entityAssembler) {
		super(IShipModeDao.class, dao, entityAssembler);
	}

	@Override
	public Class<ShipMode> getEntityClass() {
		return ShipMode.class;
	}
}
