/**
 * 
 */
package com.tll.common.model;

/**
 * AbstractRelationalProperty - Base class for model properties target a single
 * {@link Model} instance.
 * @author jpk
 */
public abstract class AbstractRelationalProperty extends AbstractModelProperty implements IRelationalProperty {

	/**
	 * The related type (needed when the model ref is null).
	 */
	protected String relatedType;

	/**
	 * Indicates the encased model is a "reference" and the model shall NOT, by
	 * default, be cleared or cloned when traversing a Model hierarchy. The
	 * "reference" to this model is always honored.
	 */
	protected boolean reference;

	/**
	 * Constructor
	 */
	public AbstractRelationalProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param relatedType The required related type
	 * @param propertyName The property name
	 * @param reference Is the relation a reference?
	 */
	public AbstractRelationalProperty(final String relatedType, final String propertyName, final boolean reference) {
		super(propertyName);
		if(relatedType == null) throw new IllegalArgumentException("Null related type");
		this.relatedType = relatedType;
		this.reference = reference;
	}

	@Override
	public final String getRelatedType() {
		return relatedType;
	}

	protected final void setRelatedType(final String relatedType) {
		this.relatedType = relatedType;
	}

	@Override
	public final boolean isReference() {
		return reference;
	}
}
