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
	
	/**
	 * Sets the model ref.
	 * <p>NOTE: No property change event is issued.
	 * @param model the model to set
	 */
	void setModel(Model model);

}
