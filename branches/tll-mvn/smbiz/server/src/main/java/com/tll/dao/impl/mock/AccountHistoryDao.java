/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IAccountHistoryDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.AccountHistory;

public class AccountHistoryDao extends EntityDao<AccountHistory> implements IAccountHistoryDao,
		IMockDao<AccountHistory> {

	@Inject
	public AccountHistoryDao(Set<AccountHistory> set) {
		super(AccountHistory.class, set);
	}
}