/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.server.rpc.listing;

import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.SelectNamedQueries;
import com.tll.server.rpc.listing.INamedQueryResolver;


/**
 * SmbizNamedQueryResolver
 * @author jpk
 */
public class SmbizNamedQueryResolver implements INamedQueryResolver {
	
	@Override
	public ISelectNamedQueryDef resolveNamedQuery(String queryName) throws IllegalArgumentException {
		for(final SelectNamedQueries snq : SelectNamedQueries.values()) {
			if(snq.getQueryName().equals(queryName)) {
				return snq;
			}
		}
		throw new IllegalArgumentException("Un-recognized query name: " + queryName);
	}

}
