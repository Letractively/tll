/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.tll.criteria.ICriteria;
import com.tll.dao.impl.IAccountDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.Account;
import com.tll.model.impl.Isp;
import com.tll.model.impl.Merchant;
import com.tll.model.key.INameKey;

public class AccountDao extends EntityDao<Account> implements IAccountDao, IMockDao<Account> {

	@Inject
	public AccountDao(Set<Account> set) {
		super(Account.class, set);
	}

	@Override
	protected List<Account> processQuery(ICriteria<? extends Account> criteria) {
		List<Account> list = new ArrayList<Account>();
		if("account.ispList".equals(criteria.getQueryName())) {
			for(Account a : set) {
				if(Isp.class.equals(a.entityClass())) {
					list.add(a);
				}
			}
		}
		else if("account.merchantList".equals(criteria.getQueryName())) {
			for(Account a : set) {
				if(Merchant.class.equals(a.entityClass())) {
					list.add(a);
				}
			}
		}
		return list;
	}

	public Account load(INameKey<? extends Account> nameKey) {
		return loadByName(nameKey);
	}
}