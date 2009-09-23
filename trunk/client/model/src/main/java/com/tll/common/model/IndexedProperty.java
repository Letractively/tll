/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.common.model;

import com.tll.model.schema.PropertyType;
import com.tll.util.PropertyPath;

/**
 * IndexedProperty - Represents a single indexed property referenced by a parent
 * {@link RelatedManyProperty}.
 * @author jpk
 */
public final class IndexedProperty extends ModelRefProperty {

	/**
	 * The index at which the target {@link Model} exists in the underlying list.
	 */
	private final int index;

	/**
	 * Constructor
	 * @param parent the required related many prop
	 * @param indexedType the required related type (type of the indexed models)
	 * @param model indexed model ref. May be <code>null</code>
	 * @param propName
	 * @param reference
	 * @param index The index
	 */
	IndexedProperty(RelatedManyProperty parent, IEntityType indexedType, Model model, String propName, boolean reference, int index) {
		super(indexedType, model, PropertyPath.index(propName, index), reference);
		assert parent != null;
		setParent(parent);
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
