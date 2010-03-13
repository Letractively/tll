/**
 * The Logic Lab
 * @author jpk
 * @since Jan 16, 2010
 */
package com.tll.dao.gae.test;

import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;
import com.tll.test.GaeTestEnvironment;

/**
 * GaeDatastoreTestEnvironment - Necessary startup/shutdown extensions for local
 * datastore interaction with Google's App Engine.
 * @author jpk
 */
public class GaeDatastoreTestEnvironment extends GaeTestEnvironment {

	@Override
	public void setupTestEnvironment() {
		super.setupTestEnvironment();
		ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
		proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
	}

	@Override
	public void teardownTestEnvironment() {
		ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
		LocalDatastoreService datastoreService = (LocalDatastoreService) proxy.getService(LocalDatastoreService.PACKAGE);
		datastoreService.clearProfiles();
		super.teardownTestEnvironment();
	}

}
