/**
 * The Logic Lab
 */
package com.tll.service.entity.intf;

import com.tll.model.AccountInterface;
import com.tll.model.Interface;
import com.tll.service.entity.INamedEntityService;

/**
 * IInterfaceService
 * @author jpk
 */
public interface IInterfaceService extends INamedEntityService<Interface> {

	/**
	 * Loads interface options for an account.
	 * @param accountId
	 * @param interfaceId
	 * @return list of subscribed interface options for a given interface and account
	 */
	AccountInterface loadAccountOptions(String accountId, String interfaceId);

	/**
	 * Replaces the subscribed interface options for a given account.
	 * @param accountInterface The account interface
	 */
	void persistAccountOptions(AccountInterface accountInterface);
}
