/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.data.rpc;

import java.util.ArrayList;

/**
 * ISourcesUserSessionEvents
 * @author jpk
 */
public interface ISourcesUserSessionEvents {

	/**
	 * Adds a listener.
	 * @param listener
	 */
	void addUserSessionListener(IUserSessionListener listener);

	/**
	 * Removes a listener.
	 * @param listener
	 */
	void removeUserSessionListener(IUserSessionListener listener);

	/**
	 * UserSessionListenerCollection
	 * @author jpk
	 */
	public static final class UserSessionListenerCollection extends ArrayList<IUserSessionListener> {

		public void fireLogin() {
			for(IUserSessionListener listener : this) {
				listener.onLogin();
			}
		}

		public void fireLogout() {
			for(IUserSessionListener listener : this) {
				listener.onLogout();
			}
		}
	}
}
