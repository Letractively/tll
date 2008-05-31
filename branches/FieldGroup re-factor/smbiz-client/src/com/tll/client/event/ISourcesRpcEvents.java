/**
 * The Logic Lab
 * @author jpk Jan 3, 2008
 */
package com.tll.client.event;

import java.util.ArrayList;

import com.tll.client.event.type.RpcEvent;

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
