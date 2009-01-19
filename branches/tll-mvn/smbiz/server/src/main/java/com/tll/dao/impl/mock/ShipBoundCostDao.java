/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IShipBoundCostDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.ShipBoundCost;
import com.tll.model.key.IBusinessKeyFactory;

public class ShipBoundCostDao extends EntityDao<ShipBoundCost> implements IShipBoundCostDao, IMockDao<ShipBoundCost> {

	@Inject
	public ShipBoundCostDao(Set<ShipBoundCost> set, IBusinessKeyFactory bkf) {
		super(ShipBoundCost.class, set, bkf);
	}
}