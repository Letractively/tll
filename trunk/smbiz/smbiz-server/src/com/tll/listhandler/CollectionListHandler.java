package com.tll.listhandler;

import java.util.Collections;
import java.util.List;

/**
 * List handler implementation for a {@link java.util.Collection}.
 * @author jpk
 */
public class CollectionListHandler<T> extends AbstractListHandler<T> {

	private List<T> rows;

	private Sorting sorting;

	/**
	 * Constructor
	 */
	CollectionListHandler() {
		super();
	}

	/**
	 * Constructor
	 * @param rows
	 * @param sorting The sorting the given <code>rows</code> are in. May be
	 *        <code>null</code>. <strong>NOTE: </strong>NO actual sorting is
	 *        performed.
	 * @throws EmptyListException
	 */
	CollectionListHandler(List<T> rows, Sorting sorting) throws EmptyListException {
		this();
		setRows(rows);
		this.sorting = sorting;
	}

	public ListHandlerType getListHandlerType() {
		return ListHandlerType.COLLECTION;
	}

	private void setRows(List<T> rows) throws EmptyListException {
		if(rows == null || rows.size() < 1) {
			throw new EmptyListException("Unable to instantiate collection list handler: No rows specified");
		}
		this.rows = rows;
	}

	public int size() {
		return rows == null ? 0 : rows.size();
	}

	public boolean hasElements() {
		return size() > 0;
	}

	public void sort(Sorting sorting) throws ListHandlerException {
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

	public Sorting getSorting() {
		return sorting;
	}

	public T getElement(int index) throws EmptyListException, ListHandlerException {
		if(!hasElements()) throw new EmptyListException("Unable to retrieve collection list element: none exist");
		return rows.get(index);
	}

	public List<T> getElements(int start, int end) throws EmptyListException, ListHandlerException {
		if(!hasElements()) throw new EmptyListException("Unable to retrieve collection list elements: none exist");
		try {
			return rows.subList(start, end);
		}
		catch(final IndexOutOfBoundsException ioobe) {
			throw new ListHandlerException("invalid index range: start(" + start + "), end(" + end + ")");
		}
	}
}
