/**
 * The Logic Lab
 * @author jpk
 * Aug 25, 2007
 */
package com.tll.client.admin.data.rpc;

import java.util.EventListener;

import com.tll.client.admin.AdminContext;

/**
 * IAdminContextListener
 * @author jpk
 */
public interface IAdminContextListener extends EventListener {

	/**
	 * ChangeType
	 * @author jpk
	 */
	public enum ChangeType {
		USER_CHANGE,
		ACCOUNT_CHANGE,
		INVALIDATE;
	}

	/**
	 * @param ac the admin context
	 */
	void onAdminContextChange(AdminContext ac, ChangeType changeType);
}
