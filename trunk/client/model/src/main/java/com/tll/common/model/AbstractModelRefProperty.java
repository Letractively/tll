/**
 * The Logic Lab
 */
package com.tll.common.model;

/**
 * AbstractModelRefProperty - Thin wrapper around a {@link Model} in order to
 * realize relationships in a model hierarchy.
 * @author jpk
 */
abstract class AbstractModelRefProperty extends AbstractRelationalProperty implements IModelRefProperty {

	/**
	 * The related one model.
	 */
	protected Model model;

	/**
	 * Constructor
	 */
	public AbstractModelRefProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param relatedType the required related type which should be base or equal
	 *        to the non-null model instance
	 * @param model may be <code>null</code>
	 * @param propName
	 * @param reference
	 */
	public AbstractModelRefProperty(final String relatedType, final Model model, final String propName,
			final boolean reference) {
		super(relatedType, propName, reference);
		setModel(model, false);
	}

	@Override
	public final Object getValue() {
		return getModel();
	}

	@Override
	public final void setValue(final Object value) throws IllegalArgumentException {
		if(value != null && value instanceof Model == false) {
			throw new IllegalArgumentException("The value is not a Model instance.");
		}
		setModel((Model) value, true);
	}

	@Override
	public final Model getModel() {
		return model;
	}

	@Override
	public final void setModel(final Model model) {
		setModel(model, false);
	}

	/**
	 * Responsible for setting the model and firing a property change events if
	 * necessary.
	 * @param model The model to set
	 * @param fireChangeEvent when <code>true</code>, a property change event will
	 *        be fired
	 * @throws IllegalArgumentException
	 */
	protected final void setModel(final Model model, final boolean fireChangeEvent) throws IllegalArgumentException {
		final Model oldModel = this.model;
		if(oldModel != model) {
			if(model != null) {
				// TODO re-impl (?)
				// enfore type compatability
				/*
				if(relatedType != null && model != null
						&& !(relatedType == model.getEntityType() || relatedType.isSubtype(model.getEntityType()))) {
					throw new IllegalArgumentException("The model must be a " + relatedType.getPresentationName());
				}
				 */
			}
			this.model = model;
			if(fireChangeEvent) getChangeSupport().firePropertyChange(propertyName, oldModel, model);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(propertyName);
		sb.append(isReference() ? "[REF] " : " ");
		final Model m = getModel();
		sb.append(m == null ? "null" : m.toString());
		return sb.toString();
	}
}