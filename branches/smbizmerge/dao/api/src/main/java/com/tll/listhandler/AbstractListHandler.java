/**
 * The Logic Lab
 * @author jpk Nov 29, 2007
 */
package com.tll.listhandler;

import com.tll.dao.Sorting;

/**
 * AbstractListHandler - Common base class to all {@link IListHandler}
 * implementations.
 * @param <T> the list element type
 * @author jpk
 */
public abstract class AbstractListHandler<T> implements IListHandler<T> {

	/**
	 * The sorting directive.
	 */
	protected Sorting sorting;

	/**
	 * Constructor
	 * @param sorting
	 */
	protected AbstractListHandler(Sorting sorting) {
		super();
		this.sorting = sorting;
	}

	@Override
	public final Sorting getSorting() {
		return sorting;
	}
}
