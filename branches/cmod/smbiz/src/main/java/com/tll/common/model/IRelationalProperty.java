/**
 * The Logic Lab
 * @author jpk
 * Apr 16, 2008
 */
package com.tll.common.model;

import com.tll.model.EntityType;

/**
 * IRelationalProperty - Indicates a relationship within a model.
 * @author jpk
 */
public interface IRelationalProperty extends IModelProperty {

	/**
	 * @return The related entity/model type.
	 */
	EntityType getRelatedType();

	/**
	 * @return <code>true</code> when this property is a "reference" to another
	 *         model property. Return <code>false</code> when this property is
	 *         "owned" by the parent model. Corresponds to the server side cascade
	 *         model property attribute.
	 */
	boolean isReference();
}
