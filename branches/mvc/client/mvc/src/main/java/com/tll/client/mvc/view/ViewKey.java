/**
 * The Logic Lab
 * @author jpk Jan 27, 2008
 */
package com.tll.client.mvc.view;

/**
 * ViewRef - Uniquely identifies {@link IView}s at runtime.
 * @author jpk
 */
public class ViewKey implements IViewKey {

	/**
	 * The ViewClass which may never be <code>null</code>.
	 */
	private final ViewClass viewClass;

	/**
	 * The runtime unique view id for the associated ViewClass.
	 */
	private int viewId;

	/**
	 * Constructor
	 * @param viewClass
	 */
	public ViewKey(ViewClass viewClass) {
		super();
		this.viewClass = viewClass;
		this.viewId = 0;
	}

	/**
	 * Constructor
	 * @param viewClass The ViewClass
	 * @param viewId The unique view id
	 */
	public ViewKey(ViewClass viewClass, int viewId) {
		super();
		if(viewClass == null) {
			throw new IllegalArgumentException("A view key must always specify a view class.");
		}
		this.viewClass = viewClass;
		this.viewId = viewId;
	}

	public ViewClass getViewClass() {
		return viewClass;
	}

	/**
	 * Uniquely identifies a view at runtime. AbstractView ids should be based on
	 * the unique view type related properties specific to the view
	 * implementation.
	 * @return int representing the unique view id. If <code>0</code>, the view id
	 *         is "un-defined" which is supported (E.g.: "static" views).
	 */
	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj.getClass() != getClass()) return false;
		final ViewKey that = (ViewKey) obj;
		return that.viewClass.equals(this.viewClass) && that.viewId == viewId;
	}

	@Override
	public int hashCode() {
		return viewId * 31 + viewClass.hashCode();
	}

	@Override
	public String toString() {
		return "view Class: " + viewClass + ", view id: " + getViewId();
	}
}
