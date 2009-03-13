/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2009
 */
package com.tll.service.entity;

import com.tll.dao.IEntityDao;
import com.tll.model.Address;
import com.tll.model.IEntityAssembler;

/**
 * AddressService
 * @author jpk
 */
public class AddressService extends EntityService<Address> implements IAddressService {

	@Override
	public Class<Address> getEntityClass() {
		return Address.class;
	}

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	private AddressService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}
}
