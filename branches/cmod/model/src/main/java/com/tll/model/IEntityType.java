/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.model;

import com.tll.INameValueProvider;

/**
 * IEntityType - Generic way of identifying a particular entity type capable of
 * being resolved to a single {@link Class} that identifies the same entity
 * type.
 * @author jpk
 */
public interface IEntityType extends INameValueProvider<String> {
	
	/**
	 * This method serves as a way to resolve {@link IEntityType} instances to
	 * entity {@link Class} instances.
	 * @return The un-qualified entity class name (w/o the package name).
	 */
	String getEntityClassName();
}
