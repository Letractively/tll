/*
 * The Logic Lab 
 */
package com.tll.service.entity.impl.account;

import javax.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IAccountAddressDao;
import com.tll.model.EntityAssembler;
import com.tll.model.impl.AccountAddress;
import com.tll.model.key.INameKey;
import com.tll.service.entity.EntityService;

/**
 * AccountAddressService - {@link IAccountAddressService} impl
 * @author jpk
 */
@Transactional
public class AccountAddressService extends EntityService<AccountAddress, IAccountAddressDao> implements IAccountAddressService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AccountAddressService(IAccountAddressDao dao, EntityAssembler entityAssembler) {
		super(IAccountAddressDao.class, dao, entityAssembler);
	}

	@Override
	public Class<AccountAddress> getEntityClass() {
		return AccountAddress.class;
	}

	public AccountAddress load(INameKey<? extends AccountAddress> key) throws EntityNotFoundException {
		return dao.load(key);
	}

}
