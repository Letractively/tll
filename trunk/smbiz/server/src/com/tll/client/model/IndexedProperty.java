/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.model;

import java.util.Iterator;
import java.util.List;

import com.tll.model.EntityType;
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
	 * The underlying list containing the indexed property.
	 */
	private transient final List<Model> list;

	/**
	 * The index at which the target {@link Model} exists in the underlying list.
	 */
	private transient int index;

	/**
	 * Constructor
	 * @param indexedType
	 * @param propName
	 * @param reference
	 * @param list The model list this property references and points to the
	 *        element at the given index.
	 * @param index The index
	 */
	IndexedProperty(EntityType indexedType, String propName, boolean reference, List<Model> list, int index) {
		super(indexedType, PropertyPath.index(propName, index), reference);
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
		int i = 0;
		for(Iterator<Model> it = list.iterator(); it.hasNext() && (i <= index); i++) {
			return it.next();
		}
		throw new IndexOutOfBoundsException("Indexed property index : " + index + " is out of bounds.");
	}

	@Override
	protected void doSetModel(Model oldModel, Model newModel) {
		list.set(index, newModel);
		if(changeSupport != null)
			changeSupport.fireIndexedPropertyChange(PropertyPath.deIndex(propertyName), index, oldModel, newModel);
	}
}
