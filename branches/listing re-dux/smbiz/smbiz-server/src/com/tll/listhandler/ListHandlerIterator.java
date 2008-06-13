package com.tll.listhandler;

import java.util.Iterator;

/**
 * The list handler iterator.
 * @author jpk
 */
abstract class ListHandlerIterator<T> implements Iterator<T> {

	protected int index;
	protected int size;

	public ListHandlerIterator(int size) {
		super();
		this.size = size;
		this.index = size > 0 ? 0 : -1;
	}

	public boolean hasNext() {
		return index < size - 1;
	}

	public void remove() {
		throw new UnsupportedOperationException("List handler iterators don't support element removal.");
	}
}
