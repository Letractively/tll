/**
 * The Logic Lab
 * @author jpk
 * Apr 5, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * ShowViewRequest
 * @author jpk
 */
@SuppressWarnings("serial")
public abstract class ShowViewRequest extends ViewRequestEvent implements IViewRef {

	/**
	 * The view class.
	 */
	private final ViewClass viewClass;

	/**
	 * The ViewKey. May <em>not</em> be known at instantiation for certain impls.
	 */
	private ViewKey viewKey;

	private String shortViewName, longViewName;

	/**
	 * Constructor
	 * @param source
	 * @param viewClass
	 */
	public ShowViewRequest(Widget source, ViewClass viewClass) {
		super(source);
		this.viewClass = viewClass;
	}

	/**
	 * @return The view type specific unique id for the requested view. Used in lazily generating the
	 *         ViewKey.
	 */
	protected abstract int getViewId();

	/**
	 * @return The runtime dependent key that uniquely identifies a particular view. Should never
	 *         return <code>null</code>.
	 */
	@Override
	public final ViewKey getViewKey() {
		if(viewKey == null) {
			if(viewClass != null) {
				viewKey = new ViewKey(viewClass, getViewId());
			}
		}
		return viewKey;
	}

	public String getShortViewName() {
		return shortViewName;
	}

	public void setShortViewName(String shortViewName) {
		this.shortViewName = shortViewName;
	}

	public String getLongViewName() {
		return longViewName;
	}

	public void setLongViewName(String longViewName) {
		this.longViewName = longViewName;
	}

}
