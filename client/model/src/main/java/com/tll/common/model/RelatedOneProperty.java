/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.common.model;

import com.tll.model.PropertyType;

/**
 * RelatedOneProperty - Represents a related one relationship w/in a
 * hierarchical model.
 * @author jpk
 */
public final class RelatedOneProperty extends AbstractModelRefProperty {

	/**
	 * Constructor
	 */
	public RelatedOneProperty() {
		super();
	}

	/**
	 * Constructor - Use when there is a non-null {@link Model} ref
	 * @param relatedType required related one type
	 * @param model the required related one model ref
	 * @param propName
	 * @param reference
	 */
	public RelatedOneProperty(final String relatedType, final Model model, final String propName,
			final boolean reference) {
		super(relatedType, model, propName, reference);
	}

	@Override
	public PropertyType getType() {
		return PropertyType.RELATED_ONE;
	}
}