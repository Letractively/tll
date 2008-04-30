/**
 * The Logic Lab
 */
package com.tll.client.model;

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
	 * @param propName
	 * @param reference
	 * @param model
	 */
	public ModelRefProperty(String propName, boolean reference, Model model) {
		super(propName, reference);
		this.model = model;
	}

	public final Object getValue() {
		return model;
	}

	public final Model getModel() {
		return model;
	}

	final void setModel(Model model) {
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
