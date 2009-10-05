/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.dao.db4o;

import java.util.List;

import com.db4o.query.Query;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.criteria.SelectNamedQueries;
import com.tll.model.CustomerAccount;
import com.tll.model.INamedEntity;
import com.tll.model.Interface;
import com.tll.model.Isp;
import com.tll.model.Merchant;


/**
 * SmbizNamedQueryTranslator
 * @author jpk
 */
public class SmbizNamedQueryTranslator implements IDb4oNamedQueryTranslator {

	@Override
	public void translateNamedQuery(ISelectNamedQueryDef queryDef, List<IQueryParam> params, Query q)
	throws InvalidCriteriaException {

		final String qname = queryDef.getQueryName();
		if(SelectNamedQueries.ISP_LISTING.getQueryName().equals(qname)) {
			q.constrain(Isp.class);
			q.descend(INamedEntity.NAME).orderAscending();
		}
		else if(SelectNamedQueries.MERCHANT_LISTING.getQueryName().equals(qname)) {
			q.constrain(Merchant.class);
			q.descend("parent").descend("id").constrain(params.get(0).getValue());
			q.descend(INamedEntity.NAME).orderAscending();
		}
		else if(SelectNamedQueries.CUSTOMER_LISTING.getQueryName().equals(qname)) {
			q.constrain(CustomerAccount.class);
			q.descend("account").descend("id").constrain(params.get(0).getValue());
			q.descend("customer").descend(INamedEntity.NAME).orderAscending();
		}
		else if(SelectNamedQueries.INTERFACE_SUMMARY_LISTING.getQueryName().equals(qname)) {
			q.constrain(Interface.class);
		}

		else throw new InvalidCriteriaException("Unhandled named query: " + qname);
	}

}
