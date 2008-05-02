/**
 * 
 */
package com.tll.listhandler;

import java.io.Serializable;
import java.util.Comparator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.tll.client.model.PropertyPathHelper;

/**
 * {@link Comparator} that compares properties of beans via reflection employing
 * a {@link SortColumn} that dictates the order and Spring's {@link BeanWrapper}
 * for performing reflection operations.
 * @param <T> The {@link Comparator} type.
 * @author jpk
 */
@SuppressWarnings("unchecked")
public class SortColumnBeanComparator<T> implements Comparator<T>, Serializable {

	static final long serialVersionUID = 5681155061446144523L;

	/**
	 * The {@link SortColumn} dictating the ordinality when comparing.
	 */
	protected SortColumn sortColumn;

	/**
	 * Constructor
	 * @param sortColumn
	 */
	public SortColumnBeanComparator(final SortColumn sortColumn) {
		super();
		this.sortColumn = sortColumn;
	}

	public int compare(final T o1, final T o2) {

		int rval = 0;

		String nestedpath = "";

		// HACK
		// TODO eliminate this
		if(o1 instanceof SearchResult) {
			assert o2 instanceof SearchResult;
			nestedpath = "element";
		}
		// END HACK

		final BeanWrapper bw1 = new BeanWrapperImpl(o1);
		final BeanWrapper bw2 = new BeanWrapperImpl(o2);

		final String propPath = PropertyPathHelper.getPropertyPath(nestedpath, sortColumn.getPropertyName());
		final Object v1 = bw1.getPropertyValue(propPath);
		final Object v2 = bw2.getPropertyValue(propPath);

		if(v1 == null && v2 == null) {
			return 0;
		}
		if(v1 == null) {
			return sortColumn.isAscending() ? -1 : 1;
		}
		if(v2 == null) {
			return sortColumn.isAscending() ? 1 : -1;
		}

		if(v1 instanceof String && v2 instanceof String) {
			if(Boolean.TRUE.equals(this.sortColumn.getIgnoreCase())) {
				rval = ((String) v1).compareToIgnoreCase((String) v2);
			}
			else {
				rval = ((String) v1).compareTo((String) v2);
			}
		}
		else if(v1 instanceof Comparable) {
			rval = ((Comparable) v1).compareTo(v2);
		}
		else if(v1 instanceof Number) {
			final Double d1 = new Double(((Number) v1).doubleValue());
			final Double d2 = new Double(((Number) v2).doubleValue());
			rval = d1.compareTo(d2);
		}
		else if(v1 instanceof Boolean) {
			final Boolean b1 = (Boolean) v1;
			final Boolean b2 = (Boolean) v2;

			if(b1.booleanValue() == b2.booleanValue())
				rval = 0;
			else {
				rval = (b1.booleanValue() == true) ? 1 : -1;
			}
		}

		else
			throw new IllegalStateException("Unhandled bean property type: " + v1.getClass());

		return sortColumn.isAscending() ? rval : (rval == 0) ? 0 : (rval > 0) ? -1 : +1;
	}
}