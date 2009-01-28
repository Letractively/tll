/**
 * The Logic Lab
 * @author jpk Jan 3, 2008
 */
package com.tll.client.data.rpc;

import java.util.ArrayList;


/**
 * ISourcesRpcEvents
 * @author jpk
 */
public interface ISourcesRpcEvents {

	/**
	 * Adds a listener.
	 * @param listener
	 */
	void addRpcListener(IRpcListener listener);

	/**
	 * Removes a listener.
	 * @param listener
	 */
	void removeRpcListener(IRpcListener listener);

	/**
	 * RpcListenerCollection
	 * @author jpk
	 */
	public static class RpcListenerCollection extends ArrayList<IRpcListener> {

		public void fireRpcEvent(RpcEvent event) {
			for(IRpcListener listener : this) {
				listener.onRpcEvent(event);
			}
		}

	}
}
