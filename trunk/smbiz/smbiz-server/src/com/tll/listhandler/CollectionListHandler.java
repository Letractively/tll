package com.tll.listhandler;

import java.util.Collections;
import java.util.List;

import com.tll.SystemError;

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
	public CollectionListHandler() {
		super();
	}

	/**
	 * Constructor
	 * @param rows
	 * @throws EmptyListException
	 */
	public CollectionListHandler(List<T> rows) throws EmptyListException {
		this();
		setRows(rows);
	}

	/**
	 * Constructor
	 * @param rows
	 * @param sorting
	 * @throws EmptyListException
	 * @throws ListHandlerException
	 */
	public CollectionListHandler(List<T> rows, Sorting sorting) throws EmptyListException, ListHandlerException {
		this();
		setRows(rows, sorting);
	}

	public ListHandlerType getListHandlerType() {
		return ListHandlerType.COLLECTION;
	}

	public boolean isSortable() {
		return true;
	}

	public void setRows(List<T> rows) throws EmptyListException {
		try {
			setRows(rows, null);
		}
		catch(final EmptyListException ele) {
			throw ele;
		}
		catch(final ListHandlerException lhe) {
			throw new SystemError("An unexpected list handling exception occurred attempting to set rows: "
					+ lhe.getMessage(), lhe);
		}
	}

	public void setRows(List<T> rows, Sorting sorting) throws EmptyListException, ListHandlerException {
		if(rows == null || rows.size() < 1) {
			throw new EmptyListException("Unable to instantiate collection list handler: No rows specified");
		}
		this.rows = rows;
		sort(sorting);
	}

	public int size() {
		return rows == null ? 0 : rows.size();
	}

	public boolean hasElements() {
		return size() > 0;
	}

	public void sort(Sorting sorting) throws ListHandlerException {
		if(sorting != null && sorting.size() > 0 && size() > 1) {
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
