/**
 * The Logic Lab
 * @author jpk Nov 29, 2007
 */
package com.tll.listhandler;

import java.util.Iterator;

import com.tll.SystemError;

/**
 * AbstractListHandler
 * @author jpk
 */
public abstract class AbstractListHandler<T> implements IListHandler<T> {

	public final Iterator<T> iterator() {
		return new ListHandlerIteratorImpl(size());
	}

	/**
	 * ListHandlerIterator - Common list handling iteration.
	 * @author jpk
	 */
	protected class ListHandlerIteratorImpl extends ListHandlerIterator<T> {

		public ListHandlerIteratorImpl(int size) {
			super(size);
		}

		public T next() {
			try {
				return getElement(index++);
			}
			catch(final ListHandlerException lhe) {
				throw new SystemError("A list handler exception occurred while iterating: " + lhe.getMessage());
			}
		}
	}
}
