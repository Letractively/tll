/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2009
 */
package com.tll.service.entity;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.Account;
import com.tll.model.IEntityAssembler;


/**
 * AccountService
 * @author jpk
 */
@Transactional
public class AccountService extends NamedEntityService<Account> implements IAccountService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AccountService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<Account> getEntityClass() {
		return Account.class;
	}

}
