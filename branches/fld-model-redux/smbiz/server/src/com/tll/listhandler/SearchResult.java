/**
 * The Logic Lab
 * @author jpk
 * Mar 7, 2008
 */
package com.tll.listhandler;

import com.tll.client.model.PropertyPath;
import com.tll.model.IEntity;
import com.tll.model.IScalar;

/**
 * SearchResult - Generic container for a single search result. Wraps either a
 * single entity or a single scalar element.
 * @author jpk
 */
public final class SearchResult<E extends IEntity> {

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
		this.element = element;
	}

	/**
	 * @return the uncast search result element.
	 */
	public Object getElement() {
		return element;
	}

	@SuppressWarnings("unchecked")
	public E getEntity() {
		return (element instanceof IScalar) ? null : (E) element;
	}

	public IScalar getScalar() {
		return (element instanceof IScalar) ? (IScalar) element : null;
	}

	public String getPropertyPath(final String propertyName) {
		final E e = getEntity();
		if(e != null) {
			// entity
			return PropertyPath.getPropertyPath("element", propertyName);
		}
		// scalar
		final IScalar scalar = getScalar();
		assert scalar != null;
		return PropertyPath.getPropertyPath("element", scalar.getPropertyPath(propertyName));
	}

	/**
	 * @return The type of this search result. May be <code>null</code>.
	 */
	Class<? extends IEntity> getRefType() {
		final E e = getEntity();
		if(e != null) {
			// entity
			return e.entityClass();
		}
		// scalar
		return getScalar().getRefType();
	}
}
