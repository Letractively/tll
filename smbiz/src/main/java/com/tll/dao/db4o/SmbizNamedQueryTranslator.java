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
import com.tll.model.INamedEntity;
import com.tll.model.Interface;
import com.tll.model.Isp;
import com.tll.model.Merchant;


/**
 * SmbizNamedQueryTranslator
 * @author jpk
 */
public class SmbizNamedQueryTranslator implements IDb4oNamedQueryTranslator {

	static final String QNAME_ISP_LISTING = "account.ispList";
	static final String QNAME_MERCHANT_LISTING = "account.merchantList";
	static final String QNAME_CUSTOMER_LISTING = "account.customerList";

	static final String QNAME_INTERFACE_LISTING = "interface.select";

	@Override
	public void translateNamedQuery(ISelectNamedQueryDef queryDef, List<IQueryParam> params, Query q)
	throws InvalidCriteriaException {

		final String qname = queryDef.getQueryName();
		if(QNAME_ISP_LISTING.equals(qname)) {
			q.constrain(Isp.class);
			q.descend(INamedEntity.NAME).orderAscending();
		}
		else if(QNAME_MERCHANT_LISTING.equals(qname)) {
			q.constrain(Merchant.class);
			q.descend("parent").descend("id").equals(params.get(0).getValue());
			q.descend(INamedEntity.NAME).orderAscending();
		}
		else if(QNAME_CUSTOMER_LISTING.equals(qname)) {
			q.constrain(Merchant.class);
			q.descend("parent").descend("id").equals(params.get(0).getValue());
			q.descend(INamedEntity.NAME).orderAscending();
		}
		else if(QNAME_INTERFACE_LISTING.equals(qname)) {
			q.constrain(Interface.class);
		}

		else throw new InvalidCriteriaException("Unhandled named query: " + qname);
	}

}
