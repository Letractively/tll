/**
 * The Logic Lab
 * @author jpk
 * @since Apr 28, 2009
 */
package com.tll.server;


/**
 * Handles server-side exceptions.
 * @author jpk
 */
public interface IExceptionHandler {

	/**
	 * Handles a server-side exception.
	 * @param t The exception.
	 */
	void handleException(final Throwable t);

}