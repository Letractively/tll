/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.common.model;

import java.util.List;

import com.tll.model.schema.PropertyType;

/**
 * IndexedProperty - Represents a single indexed property referenced by a parent
 * {@link RelatedManyProperty}.
 * <p>
 * <em><b>IMPT:</b> This property type is a purely client-side construct and is <b>not</em>
 * client/server marshaled.
 * @author jpk
 */
public final class IndexedProperty extends ModelRefProperty {

	/**
	 * The underlying related many collection property.
	 */
	private transient final List<Model> list;

	/**
	 * The index at which the target {@link Model} exists in the underlying list.
	 */
	private transient final int index;

	/**
	 * Constructor
	 * @param indexedType
	 * @param propName
	 * @param reference
	 * @param list The underlying related many list holding the model element
	 *        targeted by the given index
	 * @param index The index
	 */
	IndexedProperty(IEntityType indexedType, String propName, boolean reference, List<Model> list, int index) {
		super(indexedType, PropertyPath.index(propName, index), reference);
		assert list != null;
		this.list = list;
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

	@Override
	public Model getModel() {
		return list.get(index);
	}

	@Override
	protected void doSetModel(Model oldModel, Model newModel) {
		throw new UnsupportedOperationException();
	}
}