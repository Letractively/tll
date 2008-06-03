/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.EntityType;

/**
 * ModelRefProperty - Thin wrapper around a {@link Model} in order to facilitate
 * the hierarchical representation of a related one association.
 * @author jpk
 */
public abstract class ModelRefProperty extends AbstractRelationalProperty implements IModelRefProperty {

	private Model model;

	/**
	 * Constructor
	 */
	public ModelRefProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param relatedType The related Model type. This <em>must</em> match that
	 *        of the given Model's type if the given Model is non-<code>null</code>.
	 * @param propName
	 * @param reference
	 * @param model
	 */
	public ModelRefProperty(EntityType relatedType, String propName, boolean reference, Model model) {
		super(relatedType, propName, reference);
		setModel(model);
	}

	public final Object getValue() {
		return model;
	}

	public final Model getModel() {
		return model;
	}

	final void setModel(Model model) {
		if(relatedType == null) {
			throw new IllegalStateException("A related type must be set");
		}
		if(model != null) {
			if(relatedType != model.getEntityType()) {
				throw new IllegalArgumentException("The model type must match the prescribed related type");
			}
		}
		this.model = model;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getPropertyName());
		sb.append("|ref:");
		sb.append(isReference());
		sb.append('|');
		if(model != null) {
			sb.append("(grphash:");
			sb.append(Integer.toString(model.hashCode()));
			sb.append(")");
		}
		else {
			sb.append(":null");
		}
		return sb.toString();
	}
}
