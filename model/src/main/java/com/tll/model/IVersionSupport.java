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
	 * The name of the version field.
	 */
	public static final String VERSION_FIELDNAME = "version";

	/**
	 * @return the version
	 */
	Integer getVersion();

	/**
	 * @param version the version to set
	 */
	void setVersion(Integer version);
}
