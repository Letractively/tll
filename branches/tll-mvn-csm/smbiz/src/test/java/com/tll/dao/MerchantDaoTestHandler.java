/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.QueryParam;
import com.tll.criteria.SelectNamedQueries;
import com.tll.model.Merchant;
import com.tll.model.schema.PropertyType;

/**
 * MerchantDaoTestHandler
 * @author jpk
 */
public class MerchantDaoTestHandler extends AbstractAccountDaoTestHandler<Merchant> {

	@Override
	public Class<Merchant> entityClass() {
		return Merchant.class;
	}

	@Override
	public void verifyLoadedEntityState(Merchant e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getStoreName(), "Merchant store name is null");
	}

	@Override
	public ISelectNamedQueryDef[] getQueriesToTest() {
		return new ISelectNamedQueryDef[] { SelectNamedQueries.MERCHANT_LISTING };
	}

	@Override
	public IQueryParam[] getParamsForTestQuery(ISelectNamedQueryDef qdef) {
		return new IQueryParam[] { new QueryParam("ispId", PropertyType.INT, new Integer(1)) };
	}

	@Override
	public Sorting getSortingForTestQuery(ISelectNamedQueryDef qdef) {
		return new Sorting("name");
	}
}
