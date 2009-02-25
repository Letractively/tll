/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.SelectNamedQueries;


/**
 * NamedQueryResolver
 * @author jpk
 */
public class NamedQueryResolver implements INamedQueryResolver {
	
	private static final NamedQueryResolver instance = new NamedQueryResolver();

	public static final NamedQueryResolver instance() {
		return instance;
	}

	/**
	 * Constructor
	 */
	private NamedQueryResolver() {
		super();
	}

	@Override
	public ISelectNamedQueryDef resolveNamedQuery(String queryName) throws IllegalArgumentException {
		for(SelectNamedQueries snq : SelectNamedQueries.values()) {
			if(snq.getQueryName().equals(queryName)) {
				return snq;
			}
		}
		throw new IllegalArgumentException("Un-recognized query name: " + queryName);
	}

}
