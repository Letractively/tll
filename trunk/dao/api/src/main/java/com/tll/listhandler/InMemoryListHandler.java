package com.tll.listhandler;

import java.util.Collections;
import java.util.List;

import com.tll.IPropertyValueProvider;
import com.tll.dao.SortColumnComparator;
import com.tll.dao.Sorting;

/**
 * InMemoryListHandler - {@link IListHandler} implementation for a
 * {@link java.util.List}.
 * @author jpk
 * @param <T> the row element type
 */
public class InMemoryListHandler<T> extends AbstractListHandler<T> {

	/**
	 * The managed list.
	 */
	private List<T> rows;

	/**
	 * Constructor
	 */
	public InMemoryListHandler() {
		super(null);
	}

	/**
	 * Constructor
	 * @param rows must not be <code>null</code> but may be empty.
	 * @param sorting sorting directive reflecting the state of the given list.
	 *        May be null.
	 * @throws IllegalArgumentException When <code>rows</code> is
	 *         <code>null</code>
	 */
	public InMemoryListHandler(List<T> rows, Sorting sorting) {
		super(sorting);
		setList(rows);
	}

	/**
	 * Sets or resets the managed row list.
	 * @param rows non-<code>null</code>
	 */
	public void setList(List<T> rows) {
		if(rows == null) {
			throw new IllegalArgumentException("Null row list");
		}
		this.rows = rows;
	}

	@Override
	public final int size() {
		return rows == null ? 0 : rows.size();
	}

	@SuppressWarnings({
		"unchecked", "rawtypes" })
	private void sort(Sorting sort) throws ListHandlerException {
		if(rows == null) throw new ListHandlerException("Rows not set.");
		if(sort == null || sort.size() < 1) {
			throw new ListHandlerException("No sorting specified.");
		}
		if(size() > 1) {
			Object elm = rows.get(0);
			try {
				if(elm instanceof Comparable) {
					Collections.sort((List<Comparable>) this.rows);
				}
				else if(elm instanceof IPropertyValueProvider) {
					Collections.sort(this.rows, new SortColumnComparator(sort.getPrimarySortColumn()));
				}
				else
					throw new RuntimeException("List elements are not comparable");
			}
			catch(final RuntimeException e) {
				throw new ListHandlerException("Unable to sort list: " + e.getMessage(), e);
			}
		}
		this.sorting = sort;
	}

	@Override
	public List<T> getElements(int offset, int pageSize, Sorting sort) throws IndexOutOfBoundsException,
			EmptyListException, ListHandlerException {
		final int siz = size();
		if(rows == null) throw new ListHandlerException("No rows set");
		if(siz < 1) throw new EmptyListException("No collection list elements exist");

		if(sort != null && !sort.equals(this.sorting) || sorting == null) {
			sort(sort);
		}

		// adjust boundaries if necessary
		// TODO determine if we should be rigid instead and throw an
		// IndexOutOfBoundsException..
		int start = offset, end = offset + pageSize;
		if(start >= siz) start = siz - 1;
		if(end > siz) end = siz;
		assert end >= start;

		return rows.subList(start, end);
	}
}
