package com.tll.service.entity.address;

import javax.validation.ValidatorFactory;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.Address;
import com.tll.model.IEntityAssembler;
import com.tll.service.entity.EntityService;

/**
 * AddressService - {@link IAddressService} impl
 * @author jpk
 */
public class AddressService extends EntityService<Address> implements IAddressService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public AddressService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<Address> getEntityClass() {
		return Address.class;
	}
}
