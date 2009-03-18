/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.TestSelectNamedQueries;


/**
 * TestNamedQueryResolver
 * @author jpk
 */
public class TestNamedQueryResolver implements INamedQueryResolver {
	
	@Override
	public ISelectNamedQueryDef resolveNamedQuery(String queryName) throws IllegalArgumentException {
		for(final TestSelectNamedQueries snq : TestSelectNamedQueries.values()) {
			if(snq.getQueryName().equals(queryName)) {
				return snq;
			}
		}
		throw new IllegalArgumentException("Un-recognized query name: " + queryName);
	}

}
