/**
 * The Logic Lab
 * @author jpk
 * Feb 15, 2008
 */
package com.tll.client.model;

import java.util.List;

import com.tll.model.EntityType;

/**
 * IndexedPropPathBinding - Represents a binding resulting from a resolved
 * property path ending with an index callout.
 * <p>
 * E.g.: <code>parent.relatedMany[4]</code>
 * @author jpk
 */
class IndexedPropPathBinding extends PropPathBinding {

	private final EntityType indexedType;
	private final List<Model> list;
	private final int index;
	private final boolean reference;

	/**
	 * This is a pseudo property value soley for the purposes of conforming to the
	 * semantics of returning a {@link IndexedProperty}.
	 */
	private IndexedProperty ipv;

	/**
	 * Constructor
	 * @param parent
	 * @param propPath
	 * @param reference
	 * @param indexedType
	 * @param list
	 * @param index
	 */
	IndexedPropPathBinding(Model parent, PropertyPath propPath, boolean reference, EntityType indexedType,
			List<Model> list, int index) {
		super(parent, propPath);
		this.reference = reference;
		assert indexedType != null;
		this.indexedType = indexedType;
		assert list != null;
		this.list = list;
		assert index >= 0;
		this.index = index;
	}

	@Override
	IPropertyBinding getPropertyBinding() throws UnsetPropertyException {
		if(ipv == null) {
			if(index >= list.size()) {
				throw new UnsetPropertyException(propPath.toString());
			}
			Model ng = list.get(index);
			ipv = new IndexedProperty(indexedType, propPath.nameAt(propPath.depth() - 1), reference, ng, index);
		}
		return ipv;
	}

	List<Model> getList() {
		return list;
	}

	boolean isReference() {
		return reference;
	}

}
