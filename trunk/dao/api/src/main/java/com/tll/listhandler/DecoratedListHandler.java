package com.tll.listhandler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tll.dao.Sorting;

/**
 * The decorated list handler allowing list elements to be transformed to
 * another type.
 * @param <T> The pre-transformation type.
 * @param <V> The transormed type.
 * @author jpk
 */
public abstract class DecoratedListHandler<T, V> implements IListHandler<V> {

	static final Logger LOG = LoggerFactory.getLogger(DecoratedListHandler.class);

	/**
	 * The wrapped list handler. This class supports the case when it is
	 * <code>null</code>.
	 */
	private IListHandler<T> listHandler;

	/**
	 * Constructor
	 */
	public DecoratedListHandler() {
		super();
	}

	/**
	 * Constructor
	 * @param listHandler
	 */
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

	@Override
	public int size() {
		return (listHandler == null) ? 0 : listHandler.size();
	}

	@Override
	public Sorting getSorting() {
		return (listHandler == null) ? null : listHandler.getSorting();
	}

	@Override
	public List<V> getElements(int offset, int pageSize, Sorting sorting) throws IndexOutOfBoundsException,
			EmptyListException, ListHandlerException {
		if(listHandler == null) return null;

		final List<T> rows = listHandler.getElements(offset, pageSize, sorting);

		final List<V> decoratedRows = new ArrayList<V>(rows.size());

		for(final T t : rows) {
			decoratedRows.add(getDecoratedElement(t));
		}

		return decoratedRows;
	}
	
	protected abstract V getDecoratedElement(T elm);
}
