/**
 * The Logic Lab
 * @author jpk
 * Aug 25, 2007
 */
package com.tll.client.admin.event;

import java.util.EventListener;

import com.tll.client.admin.AdminContext;

/**
 * IAdminContextListener
 * @author jpk
 */
public interface IAdminContextListener extends EventListener {

	public final class ChangeType {}
	
	public static final ChangeType USER_CHANGE = new ChangeType();
	public static final ChangeType ACCOUNT_CHANGE = new ChangeType();
	public static final ChangeType INVALIDATE = new ChangeType();
	
	/**
	 * @param ac the admin context
	 */
	void onAdminContextChange(AdminContext ac, ChangeType changeType);
}
