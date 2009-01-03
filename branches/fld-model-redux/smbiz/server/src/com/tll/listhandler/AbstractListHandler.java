/**
 * The Logic Lab
 * @author jpk Nov 29, 2007
 */
package com.tll.listhandler;

import com.tll.dao.Sorting;

/**
 * AbstractListHandler - Common base class to all {@link IListHandler}
 * implementations.
 * @author jpk
 */
public abstract class AbstractListHandler<T> implements IListHandler<T> {

	/**
	 * The sorting directive.
	 */
	protected Sorting sorting;
}
