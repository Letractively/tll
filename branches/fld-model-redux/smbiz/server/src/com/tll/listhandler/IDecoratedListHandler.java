/**
 * The Logic Lab
 * @author jpk Nov 29, 2007
 */
package com.tll.listhandler;

/**
 * IDecoratedListHandler
 * @author jpk
 */
public interface IDecoratedListHandler<T, V> extends IListHandler<V> {

	/**
	 * Transforms an element from type <T> to type <V>
	 * @param element the element
	 * @return the transformed element
	 */
	V getDecoratedElement(T element);

	/**
	 * @return The wrapped list handler.
	 */
	IListHandler<T> getWrappedHandler();

	/**
	 * Sets the wrapped list handler.
	 * @param listHandler
	 */
	void setWrappedHandler(IListHandler<T> listHandler);
}
