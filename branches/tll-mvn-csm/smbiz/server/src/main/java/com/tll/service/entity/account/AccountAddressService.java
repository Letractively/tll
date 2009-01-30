/*
 * The Logic Lab 
 */
package com.tll.service.entity.account;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.AccountAddress;
import com.tll.service.entity.IEntityAssembler;
import com.tll.service.entity.NamedEntityService;

/**
 * AccountAddressService - {@link IAccountAddressService} impl
 * @author jpk
 */
@Transactional
public class AccountAddressService extends NamedEntityService<AccountAddress> implements IAccountAddressService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AccountAddressService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<AccountAddress> getEntityClass() {
		return AccountAddress.class;
	}
}
