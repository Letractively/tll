/**
 * 
 */
package com.tll.client.model;

import com.tll.model.EntityType;

/**
 * AbstractRelationalProperty
 * @author jpk
 */
public abstract class AbstractRelationalProperty extends AbstractPropertyBinding implements IRelationalProperty {

	protected EntityType relatedType;

	/**
	 * Indicates the encased model is a "reference" and the model shall NOT be
	 * cleared or cloned when recursing any given {@link Model}. The "reference"
	 * to this model is always honored.
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
	 * @param relatedType The related type
	 * @param propertyName The property name
	 * @param reference Is the relation a reference?
	 */
	public AbstractRelationalProperty(EntityType relatedType, String propertyName, boolean reference) {
		super(propertyName);
		this.relatedType = relatedType;
		this.reference = reference;
	}

	public final EntityType getRelatedType() {
		return relatedType;
	}

	protected void setRelatedType(EntityType relatedType) {
		this.relatedType = relatedType;
	}

	public final boolean isReference() {
		return reference;
	}
}
