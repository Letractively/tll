/**
 * The Logic Lab
 * @author jpk
 * Mar 7, 2008
 */
package com.tll.dao;

import com.tll.IPropertyValueProvider;
import com.tll.model.IEntity;
import com.tll.model.IScalar;

/**
 * SearchResult - Generic container for a single search result. Wraps either a
 * single entity or a single scalar element.
 * @author jpk
 */
public final class SearchResult implements IPropertyValueProvider {

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
	 * @return The type of this search result. May be <code>null</code>.
	 */
	public Class<?> getRefType() {
		if(element instanceof IEntity) {
			// entity
			return ((IEntity) element).entityClass();
		}
		// scalar
		return ((IScalar) element).getRefType();
	}

	@Override
	public Object getPropertyValue(String propertyPath) {
		if(element == null) return null;
		//final BeanWrapper bw = new BeanWrapperImpl(element);
		if(element instanceof IEntity) {
			throw new UnsupportedOperationException("Need to re-impl this w/o spring deps!");
			//return bw.getPropertyValue(propertyPath);
		}
		else if(element instanceof IScalar) {
			return ((IScalar)element).getTupleMap().get(propertyPath);
		}
		throw new IllegalStateException("Unhandled element type:" + element.getClass());
	}
}
