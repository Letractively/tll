package com.tll.listhandler;

import java.util.Collections;
import java.util.List;

import com.tll.dao.SortColumnBeanComparator;
import com.tll.dao.Sorting;

/**
 * List handler implementation for a {@link java.util.Collection}.
 * @author jpk
 * @param <T>
 */
public class CollectionListHandler<T> extends AbstractListHandler<T> {

	/**
	 * The managed list.
	 */
	private List<T> rows;

	/**
	 * Constructor
	 */
	CollectionListHandler() {
		super();
	}

	/**
	 * Constructor
	 * @param rows
	 * @throws EmptyListException
	 */
	CollectionListHandler(List<T> rows) throws EmptyListException {
		this();
		if(rows == null || rows.size() < 1) {
			throw new EmptyListException("Unable to instantiate collection list handler: No rows specified");
		}
		this.rows = rows;
	}

	public final ListHandlerType getListHandlerType() {
		return ListHandlerType.COLLECTION;
	}

	public final int size() {
		return rows == null ? 0 : rows.size();
	}

	void sort(Sorting sorting) throws ListHandlerException {
		if(sorting == null || sorting.size() < 1) {
			throw new ListHandlerException("No sorting specified.");
		}
		if(size() > 1) {
			try {
				Collections.sort(this.rows, new SortColumnBeanComparator<T>(sorting.getPrimarySortColumn()));
			}
			catch(final RuntimeException e) {
				throw new ListHandlerException("Unable to sort list: " + e.getMessage(), e);
			}
		}
		this.sorting = sorting;
	}

	public List<T> getElements(int offset, int pageSize, Sorting sorting) throws IndexOutOfBoundsException,
			EmptyListException, ListHandlerException {
		if(size() < 1) throw new EmptyListException("No collection list elements exist");
		if(sorting != null && !sorting.equals(this.sorting)) {
			sort(sorting);
		}
		return rows.subList(offset, offset + pageSize);
	}
}
