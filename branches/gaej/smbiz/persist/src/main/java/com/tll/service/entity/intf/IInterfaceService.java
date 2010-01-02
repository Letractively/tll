/**
 * The Logic Lab
 */
package com.tll.service.entity.intf;

import java.util.Collection;

import com.tll.model.AccountInterface;
import com.tll.model.Interface;
import com.tll.service.entity.INamedEntityService;

/**
 * IInterfaceService
 * @author jpk
 */
public interface IInterfaceService extends INamedEntityService<Interface> {

	/**
	 * Loads the account interface for a given account and interface.
	 * @param accountId
	 * @param interfaceId
	 * @return the never-<code>null</code> account interface.
	 */
	AccountInterface loadAccountInterface(long accountId, long interfaceId);

	/**
	 * Adds or replaces the given account interface.
	 * @param accountInterface The account interface
	 */
	void setAccountInterface(AccountInterface accountInterface);

	/**
	 * Replaces <em>all</em> subscribed intf options for all existing master
	 * interfaces for a given account with the ones given.
	 * @param accountInterfaces The new subscribed options for all defined
	 *        interfaces
	 */
	void setAccountInterfaces(Collection<AccountInterface> accountInterfaces);

	/**
	 * Purges the subscribed options for a given account and interface.
	 * @param accountId
	 * @param interfaceId
	 */
	void purgeAccountInterface(long accountId, long interfaceId);

	/**
	 * Purges all subscribed interface options for a given account.
	 * @param accountId
	 */
	void purgeAccountInterfacess(long accountId);
}
