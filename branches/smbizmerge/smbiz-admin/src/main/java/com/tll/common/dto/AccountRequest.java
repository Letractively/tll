/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.common.dto;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
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
