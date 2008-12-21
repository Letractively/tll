/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.EntityType;

/**
 * ModelRefProperty - Thin wrapper around a {@link Model} in order to realize
 * relationships in a model hierarchy.
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
	 * @param relatedType The related Model type. This <em>must</em> match that of
	 *        the given Model's type if the given Model is non-<code>null</code>.
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
		// NOTE: we don't *require* the relatedType to be set but if it is, we
		// enfore type compatability
		if(relatedType != null && model != null
				&& !(relatedType == model.getEntityType() || relatedType.isSubtype(model.getEntityType()))) {
			throw new IllegalArgumentException("The model must be a " + relatedType.getName());
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
			sb.append("(modelhash:");
			sb.append(Integer.toString(model.hashCode()));
			sb.append(")");
		}
		else {
			sb.append(":null");
		}
		return sb.toString();
	}
}
