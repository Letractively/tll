/**
 * The Logic Lab
 * @author jpk
 * Mar 7, 2008
 */
package com.tll.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tll.model.IEntity;
import com.tll.model.IScalar;

/**
 * SearchResult - Generic container for a single search result. Wraps either a
 * single entity or a single scalar element.
 * @author jpk
 * @param <T> the search result element type
 */
public final class SearchResult<T> {

	/**
	 * Factory method to "wrap" a collection of like types.
	 * @param <T>
	 * @param clc The collection to transform
	 * @return Newly created list of {@link SearchResult}s whose contained
	 *         elements are the elements in the given collection.
	 */
	public static <T> List<SearchResult<T>> create(Collection<T> clc) {
		if(clc == null) return null;
		final ArrayList<SearchResult<T>> rval = new ArrayList<SearchResult<T>>(clc.size());
		for(final T t : clc) {
			rval.add(new SearchResult<T>(t));
		}
		return rval;
	}

	/**
	 * The raw search result element.
	 */
	private final T element;

	/**
	 * Constructor
	 * @param element
	 */
	public SearchResult(final T element) {
		super();
		if(element instanceof IEntity == false && element instanceof IScalar == false)
			throw new IllegalArgumentException("Invalid search result element type");
		this.element = element;
	}

	/**
	 * @return the raw search result element.
	 */
	public T getElement() {
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
