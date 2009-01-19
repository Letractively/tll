/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.ICustomerAccountDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.CustomerAccount;
import com.tll.model.key.IBusinessKeyFactory;

public class CustomerAccountDao extends EntityDao<CustomerAccount> implements ICustomerAccountDao,
		IMockDao<CustomerAccount> {

	@Inject
	public CustomerAccountDao(Set<CustomerAccount> set, IBusinessKeyFactory bkf) {
		super(CustomerAccount.class, set, bkf);
	}
}