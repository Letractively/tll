/**
 * 
 */
package com.tll.client.model;

/**
 * AbstractRelationalProperty
 * @author jpk
 */
public abstract class AbstractRelationalProperty extends AbstractPropertyBinding implements IRelationalProperty {

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
	 * @param propertyName
	 * @param reference
	 */
	public AbstractRelationalProperty(String propertyName, boolean reference) {
		super(propertyName);
		this.reference = reference;
	}

	public final boolean isReference() {
		return reference;
	}

	public final void setReference(boolean reference) {
		this.reference = reference;
	}

	public final void setValue(Object value) {
		throw new UnsupportedOperationException("Relational properties don't support generic value assigning");
	}
}
