/*
 * The Logic Lab
 */
package com.tll.dao;

import com.tll.model.Customer;

/**
 * CustomerDaoTestHandler
 * @author jpk
 */
public class CustomerDaoTestHandler extends AbstractAccountDaoTestHandler<Customer> {

	@Override
	public Class<Customer> entityClass() {
		return Customer.class;
	}

	/*
	@Override
	public ISelectNamedQueryDef[] getQueriesToTest() {
		return new ISelectNamedQueryDef[] { SelectNamedQueries.CUSTOMER_LISTING };
	}

	@Override
	public IQueryParam[] getParamsForTestQuery(ISelectNamedQueryDef qdef) {
		return new IQueryParam[] { new QueryParam("merchantId", PropertyType.INT, Integer.valueOf(1)) };
	}

	@Override
	public Sorting getSortingForTestQuery(ISelectNamedQueryDef qdef) {
		return new Sorting("name");
	}
	 */
}
