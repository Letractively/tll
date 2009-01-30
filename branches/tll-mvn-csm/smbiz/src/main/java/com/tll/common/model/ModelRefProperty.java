/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.model.EntityType;

/**
 * ModelRefProperty - Thin wrapper around a {@link Model} in order to realize
 * relationships in a model hierarchy.
 * @author jpk
 */
public abstract class ModelRefProperty extends AbstractRelationalProperty implements IModelRefProperty {

	/**
	 * Constructor
	 */
	public ModelRefProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param relatedType The related Model type. This <em>must</em> match that of
	 *        the given Model's type if the given Model is non-<code>null</code>.
	 * @param propName
	 * @param reference
	 */
	public ModelRefProperty(EntityType relatedType, String propName, boolean reference) {
		super(relatedType, propName, reference);
	}

	public final Object getValue() {
		return getModel();
	}

	public final void setValue(Object value) throws IllegalArgumentException {
		if(value != null && value instanceof Model == false) {
			throw new IllegalArgumentException("The value is not a Model instance.");
		}
		setModel((Model) value);
	}

	/**
	 * @return The model.
	 */
	public abstract Model getModel();

	/**
	 * Responsible for setting the model and firing a property change events if
	 * necessary.
	 * @param model The model to set
	 * @throws IllegalArgumentException
	 */
	protected final void setModel(Model model) throws IllegalArgumentException {
		final Model oldModel = getModel();
		if(oldModel != model) {
			// NOTE: we don't *require* the relatedType to be set but if it is, we
			// enfore type compatability
			if(relatedType != null && model != null
					&& !(relatedType == model.getEntityType() || relatedType.isSubtype(model.getEntityType()))) {
				throw new IllegalArgumentException("The model must be a " + relatedType.getName());
			}
			doSetModel(oldModel, model);
		}
	}

	/**
	 * Called when the model is to be updated. Responsible for setting the model
	 * to the given new model and firing an appropriate property change event.
	 * @param oldModel
	 * @param newModel
	 * @throws IllegalArgumentException
	 */
	protected abstract void doSetModel(Model oldModel, Model newModel) throws IllegalArgumentException;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(propertyName);
		sb.append(isReference() ? " [REF] " : " ");
		Model m = getModel();
		sb.append(m == null ? "-empty-" : m.toString());
		return sb.toString();
	}
}
