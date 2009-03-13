/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2009
 */
package com.tll.service.entity;

import com.tll.dao.IEntityDao;
import com.tll.model.Account;
import com.tll.model.IEntityAssembler;


/**
 * AccountService
 * @author jpk
 */
public class AccountService extends NamedEntityService<Account> implements IAccountService {

	@Override
	public Class<Account> getEntityClass() {
		return Account.class;
	}

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	private AccountService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}
}
