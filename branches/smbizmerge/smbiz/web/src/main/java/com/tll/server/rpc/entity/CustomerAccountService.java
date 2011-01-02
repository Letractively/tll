/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity;

import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.model.IEntity;

/**
 * CustomerAccountService
 * @author jpk
 */
public class CustomerAccountService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public CustomerAccountService(PersistContext context) {
		super(context);
	}

	@Override
	protected Model entityToModel(IEntityType entityType, IEntity e) throws Exception {
		final Model m = super.entityToModel(entityType, e);
		return m;
	}
}
