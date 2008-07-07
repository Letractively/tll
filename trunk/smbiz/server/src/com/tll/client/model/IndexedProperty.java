/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.model;

import com.tll.model.EntityType;
import com.tll.model.schema.PropertyType;

/**
 * IndexedProperty - Represents a single indexed property referenced by a parent
 * {@link RelatedManyProperty}.
 * @author jpk
 */
public final class IndexedProperty extends ModelRefProperty {

	private int index;

	/**
	 * Constructor
	 */
	public IndexedProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param indexedType
	 * @param propName
	 * @param reference
	 * @param model
	 * @param index
	 */
	public IndexedProperty(EntityType indexedType, String propName, boolean reference, Model model, int index) {
		super(indexedType, PropertyPath.index(propName, index, false), reference, model);
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
