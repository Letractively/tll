/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server;

import com.google.inject.Injector;


/**
 * IBootstrapHandler
 * @author jpk
 */
public interface IBootstrapHandler {

	/**
	 * Handles particular bootstrapping.
	 * @param injector The assembled app scoped dependency injector that provides
	 *        all app dependencies.
	 */
	void handleBootstrap(Injector injector);
}
