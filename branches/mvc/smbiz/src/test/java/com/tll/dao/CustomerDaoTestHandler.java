/*
 * The Logic Lab 
 */
package com.tll.dao;

import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.QueryParam;
import com.tll.criteria.SelectNamedQueries;
import com.tll.model.Customer;
import com.tll.model.schema.PropertyType;

/**
 * CustomerDaoTestHandler
 * @author jpk
 */
public class CustomerDaoTestHandler extends AbstractAccountDaoTestHandler<Customer> {

	@Override
	public Class<Customer> entityClass() {
		return Customer.class;
	}

	@Override
	public ISelectNamedQueryDef[] getQueriesToTest() {
		return new ISelectNamedQueryDef[] { SelectNamedQueries.CUSTOMER_LISTING };
	}

	@Override
	public IQueryParam[] getParamsForTestQuery(ISelectNamedQueryDef qdef) {
		return new IQueryParam[] { new QueryParam("merchantId", PropertyType.INT, new Integer(1)) };
	}
	
	@Override
	public Sorting getSortingForTestQuery(ISelectNamedQueryDef qdef) {
		return new Sorting("name");
	}
}
