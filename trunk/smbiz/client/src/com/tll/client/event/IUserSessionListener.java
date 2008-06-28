/**
 * The Logic Lab
 * @author jpk Aug 24, 2007
 */
package com.tll.client.event;

import java.util.EventListener;

/**
 * IUserSessionListener - Listens to user session related events namely
 * login/logout.
 * @author jpk
 */
public interface IUserSessionListener extends EventListener {

	/**
	 * Successful user login - user session begins.
	 */
	void onLogin();

	/**
	 * User has logged out - user session ends.
	 */
	void onLogout();
}
