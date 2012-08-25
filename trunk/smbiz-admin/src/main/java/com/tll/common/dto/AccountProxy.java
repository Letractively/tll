/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.common.dto;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.tll.model.Account;

/**
 * @author jpk
 */
@ProxyFor(Account.class)
public interface AccountProxy extends EntityProxy {

	Long getId();
	
	AccountProxy getParent();
	
	String getEntityType();
	
	CurrencyProxy getCurrency();
	
	
}
