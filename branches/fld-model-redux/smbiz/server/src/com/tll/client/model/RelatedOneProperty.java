/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.model;

import com.tll.model.EntityType;
import com.tll.model.schema.PropertyType;

/**
 * RelatedOneProperty - Represents a related one relationship w/in a
 * hierarchical model.
 * @author jpk
 */
public final class RelatedOneProperty extends ModelRefProperty {

	/**
	 * Constructor
	 */
	public RelatedOneProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param relatedType
	 * @param propName
	 * @param reference
	 * @param model
	 */
	public RelatedOneProperty(EntityType relatedType, String propName, boolean reference, Model model) {
		super(relatedType, propName, reference, model);
	}

	public PropertyType getType() {
		return PropertyType.RELATED_ONE;
	}

	@Override
	protected void fireModelChangeEvent(Model oldModel, Model newModel) {
		assert changeSupport != null;
		changeSupport.firePropertyChange(propertyName, oldModel, newModel);
	}
}
