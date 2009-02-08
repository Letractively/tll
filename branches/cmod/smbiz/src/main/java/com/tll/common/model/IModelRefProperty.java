/**
 * 
 */
package com.tll.common.model;

/**
 * IModelRefProperty - Indicates a model property that references another
 * {@link Model}.
 * @author jpk
 */
public interface IModelRefProperty extends IRelationalProperty {

	/**
	 * @return The model ref
	 */
	Model getModel();

}
