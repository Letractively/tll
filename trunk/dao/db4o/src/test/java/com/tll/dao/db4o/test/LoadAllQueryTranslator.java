/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.dao.db4o.test;

import java.util.List;

import com.db4o.query.Query;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.db4o.IDb4oNamedQueryTranslator;
import com.tll.schema.IQueryParam;


/**
 * LoadAllQueryTranslator
 * @author jpk
 */
public class LoadAllQueryTranslator implements IDb4oNamedQueryTranslator {

	@Override
	public void translateNamedQuery(ISelectNamedQueryDef queryDef, List<IQueryParam> params, Query db4oQuery)
	throws InvalidCriteriaException {

		// simply return all for the given entity type
		db4oQuery.constrain(queryDef.getEntityType());
	}

}
