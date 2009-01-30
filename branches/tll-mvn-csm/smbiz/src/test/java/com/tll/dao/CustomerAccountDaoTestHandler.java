/*
 * The Logic Lab 
 */
package com.tll.dao;

import com.tll.model.Customer;

/**
 * CustomerAccountDaoTestHandler
 * @author jpk
 */
public class CustomerAccountDaoTestHandler extends AbstractAccountDaoTestHandler<Customer> {

	@Override
	public Class<Customer> entityClass() {
		return Customer.class;
	}
}
