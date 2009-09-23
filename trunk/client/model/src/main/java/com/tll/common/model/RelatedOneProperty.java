/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.common.model;

import com.tll.model.schema.PropertyType;

/**
 * RelatedOneProperty - Represents a related one relationship w/in a
 * hierarchical model.
 * @author jpk
 */
public final class RelatedOneProperty extends ModelRefProperty {

	/**
	 * The related one model.
	 */
	private Model model;

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
	public RelatedOneProperty(IEntityType relatedType, String propName, boolean reference, Model model) {
		super(relatedType, propName, reference);
		this.model = model;
	}

	public PropertyType getType() {
		return PropertyType.RELATED_ONE;
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	protected void doSetModel(Model oldModel, Model newModel) {
		model = newModel;
	}
}
