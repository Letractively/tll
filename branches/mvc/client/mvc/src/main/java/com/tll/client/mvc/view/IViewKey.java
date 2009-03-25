/**
 * The Logic Lab
 * @author jpk
 * @since Mar 24, 2009
 */
package com.tll.client.mvc.view;


/**
 * IViewKey - Uniquely identifies {@link IView}s at runtime.
 * @author jpk
 */
public interface IViewKey {

	/**
	 * @return The view class - the "compile time" component of the view key.
	 */
	ViewClass getViewClass();

	/**
	 * The "runtime" or dynamic component of the view key. Non-zero view ids imply
	 * the view is <em>dynamic</em> meaning its identifiability is only
	 * ascertainable at runtime.
	 * @return the unique view id. If <code>0</code>, the view to which this key
	 *         refers is considered "static". If non-zero, the view is said to be
	 *         dynamic.
	 */
	int getViewId();
}