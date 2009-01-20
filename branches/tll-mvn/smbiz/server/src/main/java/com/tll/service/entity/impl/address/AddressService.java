package com.tll.service.entity.impl.address;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IAddressDao;
import com.tll.model.Address;
import com.tll.model.EntityAssembler;
import com.tll.service.entity.EntityService;

/**
 * AddressService - {@link IAddressService} impl
 * @author jpk
 */
@Transactional
public class AddressService extends EntityService<Address, IAddressDao> implements IAddressService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AddressService(IAddressDao dao, EntityAssembler entityAssembler) {
		super(IAddressDao.class, dao, entityAssembler);
	}

	@Override
	public Class<Address> getEntityClass() {
		return Address.class;
	}
}
