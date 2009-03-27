/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2009
 */
package com.tll.service.entity;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.Address;
import com.tll.model.IEntityAssembler;

/**
 * AddressService
 * @author jpk
 */
@Transactional
public class AddressService extends EntityService<Address> implements IAddressService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AddressService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<Address> getEntityClass() {
		return Address.class;
	}
}
