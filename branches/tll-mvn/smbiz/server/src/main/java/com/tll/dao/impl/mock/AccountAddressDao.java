/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IAccountAddressDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.AccountAddress;
import com.tll.model.key.IBusinessKeyFactory;

public class AccountAddressDao extends EntityDao<AccountAddress> implements IAccountAddressDao,
		IMockDao<AccountAddress> {

	@Inject
	public AccountAddressDao(Set<AccountAddress> set, IBusinessKeyFactory bkf) {
		super(AccountAddress.class, set, bkf);
	}
}