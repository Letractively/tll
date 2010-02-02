package com.tll.listhandler;

import java.util.Collections;
import java.util.List;

import com.tll.dao.SortColumnBeanComparator;
import com.tll.dao.Sorting;

/**
 * InMemoryListHandler - {@link IListHandler} implementation for a {@link java.util.List}.
 * @author jpk
 * @param <T> the row element type
 */
public class InMemoryListHandler<T> extends AbstractListHandler<T> {

	/**
	 * The managed non-<code>null</code> list.
	 */
	private final List<T> rows;

	/**
	 * Constructor
	 * @param rows must not be <code>null</code> but may be empty.
	 * @throws IllegalArgumentException When <code>rows</code> is <code>null</code>
	 */
	InMemoryListHandler(List<T> rows) {
		super();
		if(rows == null) {
			throw new IllegalArgumentException("Null row list");
		}
		this.rows = rows;
	}

	public final ListHandlerType getListHandlerType() {
		return ListHandlerType.IN_MEMORY;
	}

	public final int size() {
		return rows == null ? 0 : rows.size();
	}

	void sort(Sorting sort) throws ListHandlerException {
		if(sort == null || sort.size() < 1) {
			throw new ListHandlerException("No sorting specified.");
		}
		if(size() > 1) {
			try {
				Collections.sort(this.rows, new SortColumnBeanComparator<T>(sort.getPrimarySortColumn()));
			}
			catch(final RuntimeException e) {
				throw new ListHandlerException("Unable to sort list: " + e.getMessage(), e);
			}
		}
		this.sorting = sort;
	}

	public List<T> getElements(int offset, int pageSize, Sorting sort) throws IndexOutOfBoundsException,
	EmptyListException, ListHandlerException {
		if(size() < 1) throw new EmptyListException("No collection list elements exist");
		if(sort != null && !sort.equals(this.sorting)) {
			sort(sort);
		}
		return rows.subList(offset, offset + pageSize);
	}
}
