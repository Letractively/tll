/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.server.rpc.listing.test;

import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.test.TestSelectNamedQueries;
import com.tll.server.rpc.listing.INamedQueryResolver;


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
