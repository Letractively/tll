/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IShipModeDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.ShipMode;
import com.tll.model.key.IBusinessKeyFactory;

public class ShipModeDao extends EntityDao<ShipMode> implements IShipModeDao, IMockDao<ShipMode> {

	@Inject
	public ShipModeDao(Set<ShipMode> set, IBusinessKeyFactory bkf) {
		super(ShipMode.class, set, bkf);
	}
}