/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.common.model;

import com.tll.IMarshalable;


/**
 * IEntityType - Generic way of identifying a particular entity type capable of
 * being resolved to a single {@link Class} that identifies the same entity
 * type.
 * @author jpk
 */
public interface IEntityType extends IMarshalable {

	/**
	 * This method serves as a way to resolve {@link IEntityType} instances to
	 * entity {@link Class} instances.
	 * @return Fully qualified class name of the referenced entity.
	 */
	String getEntityClassName();
	
	/**
	 * @return A presentation worthy name.
	 */
	String getPresentationName();
}
