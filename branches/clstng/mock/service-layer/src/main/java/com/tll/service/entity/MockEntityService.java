package com.tll.service.entity;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.Address;

/**
 * ListHandlerDataProvider - The test list handler data provider.
 * @author jpk
 */
public final class MockEntityService extends EntityService<Address> {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public MockEntityService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<Address> getEntityClass() {
		return Address.class;
	}
}