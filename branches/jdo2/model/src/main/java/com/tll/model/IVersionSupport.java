/*
 * Created on - Mar 11, 2006
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2006 - All rights reserved.
 *
 */

package com.tll.model;

/**
 * IVersionSupport - Versioning support to realize datastore level optimistic
 * concurrency control.
 * @author jpk
 */
public interface IVersionSupport {

	/**
	 * @return the version
	 */
	long getVersion();

	/**
	 * @param version the version to set
	 */
	void setVersion(long version);
}
