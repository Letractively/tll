/**
 * The Logic Lab
 * @author jpk
 * Mar 7, 2008
 */
package com.tll.dao;

import com.tll.model.IEntity;
import com.tll.model.IScalar;

/**
 * SearchResult - Generic container for a single search result. Wraps either a
 * single entity or a single scalar element.
 * @author jpk
 */
public final class SearchResult {

	/**
	 * The raw search result element.
	 */
	private final Object element;

	/**
	 * Constructor
	 * @param element
	 */
	public SearchResult(final Object element) {
		super();
		if(element instanceof IEntity == false && element instanceof IScalar == false)
			throw new IllegalArgumentException("Invalid search result element type");
		this.element = element;
	}

	/**
	 * @return the raw search result element.
	 */
	public Object getElement() {
		return element;
	}

	/**
	 * Provides the property path that allows for value acquisition via
	 * reflection.
	 * @param propertyName the desired property <em>name</em> for which a full
	 *        reflectable property path is desired
	 * @return an OGNL compliant property path allowing for proper value
	 *         acquisition via reflection
	 */
	public String getPropertyPath(final String propertyName) {
		if(propertyName == null) return "element";
		if(element instanceof IEntity) {
			// entity
			return "element." + propertyName;
		}
		// scalar
		return "element." + ((IScalar) element).getPropertyPath(propertyName);
	}

	/**
	 * @return The type of this search result. May be <code>null</code>.
	 */
	Class<?> getRefType() {
		if(element instanceof IEntity) {
			// entity
			return ((IEntity) element).entityClass();
		}
		// scalar
		return ((IScalar) element).getRefType();
	}
}
