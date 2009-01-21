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
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.dao.impl.IAccountDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.Account;
import com.tll.model.Customer;
import com.tll.model.Isp;
import com.tll.model.Merchant;

public class AccountDao extends EntityDao<Account> implements IAccountDao, IMockDao<Account> {

	@Inject
	public AccountDao(Set<Account> set) {
		super(Account.class, set);
	}

	@Override
	protected List<Account> processQuery(ICriteria<? extends Account> criteria) {
		List<Account> list = new ArrayList<Account>();
		ISelectNamedQueryDef nq = criteria.getNamedQueryDefinition();
		if(nq != null) {
			if("account.ispList".equals(nq.getQueryName())) {
				for(Account a : set) {
					if(Isp.class.equals(a.entityClass())) {
						list.add(a);
					}
				}
			}
			else if("account.merchantList".equals(nq.getQueryName())) {
				for(Account a : set) {
					if(Merchant.class.equals(a.entityClass())) {
						list.add(a);
					}
				}
			}
			else if("account.customerList".equals(nq.getQueryName())) {
				for(Account a : set) {
					if(Customer.class.equals(a.entityClass())) {
						list.add(a);
					}
				}
			}
		}
		return list;
	}
}