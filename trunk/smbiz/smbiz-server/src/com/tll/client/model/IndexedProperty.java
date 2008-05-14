/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyType;

/**
 * IndexedProperty - Represents a single indexed property value referenced by a
 * parent related many property value.
 * @author jpk
 */
public class IndexedProperty extends ModelRefProperty {

	private int index;

	/**
	 * Constructor
	 */
	public IndexedProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param propName
	 * @param reference
	 * @param model
	 * @param index
	 */
	public IndexedProperty(String propName, boolean reference, Model model, int index) {
		super(PropertyPath.indexed(propName, index), reference, model);
		this.index = index;
	}

	public PropertyType getType() {
		return PropertyType.INDEXED;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

}
