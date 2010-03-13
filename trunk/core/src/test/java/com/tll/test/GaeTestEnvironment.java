/**
 * The Logic Lab
 * @author jpk
 * @since Jan 16, 2010
 */
package com.tll.test;

import java.io.File;

import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

/**
 * GaeTestEnvironment - Necessary startup/shutdown routines for interacting with
 * a localized Google App Engine environment.
 * @author jpk
 */
public class GaeTestEnvironment implements ITestEnvironment {

	@Override
	public void setupTestEnvironment() {
		ApiProxy.setEnvironmentForCurrentThread(new GaeLocalEnvironment());
		ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(".")) {});
	}

	@Override
	public void teardownTestEnvironment() {
		// not strictly necessary to null these out but there's no harm either
		ApiProxy.setDelegate(null);
		ApiProxy.setEnvironmentForCurrentThread(null);
	}

}
