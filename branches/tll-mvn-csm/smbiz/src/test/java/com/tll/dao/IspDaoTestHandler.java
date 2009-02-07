/*
 * The Logic Lab 
 */
package com.tll.dao;

import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.SelectNamedQueries;
import com.tll.model.Isp;

/**
 * IspDaoTestHandler
 * @author jpk
 */
public class IspDaoTestHandler extends AbstractAccountDaoTestHandler<Isp> {

	@Override
	public Class<Isp> entityClass() {
		return Isp.class;
	}

	@Override
	public ISelectNamedQueryDef[] getQueriesToTest() {
		return new ISelectNamedQueryDef[] { SelectNamedQueries.ISP_LISTING };
	}

	@Override
	public Sorting getSortingForTestQuery(ISelectNamedQueryDef qdef) {
		return new Sorting("name");
	}
}
