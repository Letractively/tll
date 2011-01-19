/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.common.dto;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;
import com.tll.model.Account;

/**
 * @author jpk
 */
@Service(Account.class)
public interface AccountRequest extends RequestContext {

	Request<AccountProxy> findAccount(Long id);
	
	InstanceRequest<AccountProxy, Void> persist();

	InstanceRequest<AccountProxy, Void> remove();
}
