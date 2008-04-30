package com.tll.listhandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.SystemError;

/**
 * The decorated list handler allowing list elements to be transformed to
 * another type.
 * @param <T> The pre-transformation type.
 * @param <V> The transormed type.
 * @author jpk
 */
public abstract class DecoratedListHandler<T, V> implements IDecoratedListHandler<T, V> {

	static final Log LOG = LogFactory.getLog(DecoratedListHandler.class);

	/**
	 * The wrapped list handler. This class supports the case when it is
	 * <code>null</code>.
	 */
	private IListHandler<T> listHandler;

	public DecoratedListHandler() {
		super();
	}

	public DecoratedListHandler(IListHandler<T> listHandler) {
		this();
		setWrappedHandler(listHandler);
	}

	public IListHandler<T> getWrappedHandler() {
		return listHandler;
	}

	public void setWrappedHandler(IListHandler<T> listHandler) {
		this.listHandler = listHandler;
	}

	public boolean isSortable() {
		return listHandler == null ? false : listHandler.isSortable();
	}

	public int size() {
		return (listHandler == null) ? 0 : listHandler.size();
	}

	public boolean hasElements() {
		return (listHandler == null) ? false : listHandler.hasElements();
	}

	/*
	public void refresh() throws EmptyListException {
		if(listHandler != null) {
			listHandler.refresh();
		}
	}
	*/

	public void sort(Sorting sorting) throws ListHandlerException {
		if(listHandler != null) listHandler.sort(sorting);
	}

	public V getElement(int index) throws EmptyListException, ListHandlerException {
		if(listHandler == null) return null;
		return getDecoratedElement(listHandler.getElement(index));
	}

	public List<V> getElements(int start, int end) throws ListHandlerException {
		if(listHandler == null) return null;

		final List<T> rows = listHandler.getElements(start, end);

		final List<V> decoratedRows = new ArrayList<V>(rows.size());

		for(final T t : rows) {
			decoratedRows.add(getDecoratedElement(t));
		}

		return decoratedRows;
	}

	public List<V> getAllElements() throws EmptyListException, ListHandlerException {
		if(listHandler == null) return null;
		return getElements(0, size());
	}

	public ListHandlerType getListHandlerType() {
		return (listHandler == null) ? null : listHandler.getListHandlerType();
	}

	public Sorting getSorting() {
		return (listHandler == null) ? null : listHandler.getSorting();
	}

	/**
	 * DecoratedListHandlerIterator
	 * @author jpk
	 */
	public class DecoratedListHandlerIterator extends ListHandlerIterator<V> {

		public DecoratedListHandlerIterator() {
			super(size());
		}

		public V next() {
			try {
				return getElement(index++);
			}
			catch(final ListHandlerException lhe) {
				throw new SystemError("Unexpected list handler exception occurred while iterating: " + lhe.getMessage(), lhe);
			}
		}

	}

	public Iterator<V> iterator() {
		return new DecoratedListHandlerIterator();
	}
}
