/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyType;

/**
 * RelatedOneProperty - Represents a related one relationship w/in a
 * hierarchical model.
 * @author jpk
 */
public class RelatedOneProperty extends ModelRefProperty {

	/**
	 * Constructor
	 */
	public RelatedOneProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param propName
	 * @param reference
	 * @param model
	 */
	public RelatedOneProperty(String propName, boolean reference, Model model) {
		super(propName, reference, model);
	}

	public String descriptor() {
		return "Related one model ref property";
	}

	public PropertyType getType() {
		return PropertyType.RELATED_ONE;
	}

}
