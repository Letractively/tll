/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.data.rpc;

import com.google.gwt.event.shared.HasHandlers;

/**
 * IHasRpcHandlers
 * @author jpk
 */
public interface IHasRpcHandlers extends HasHandlers {

	/**
	 * Adds a handler.
	 * @param listener
	 */
	void addRpcHandler(IRpcHandler listener);

	/**
	 * Removes a handler.
	 * @param listener
	 */
	void removeRpcHandler(IRpcHandler listener);
}
