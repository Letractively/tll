/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * ViewRequestEvent - Encapsulates <em>all</em> necessary properties to freshly instantiate a
 * <em>particular</em> view at runtime.
 * @author jpk
 */
@SuppressWarnings("serial")
public abstract class ViewRequestEvent extends ViewEvent {

	/**
	 * Constructor - Use when the view key is not yet known.
	 * @param source
	 */
	protected ViewRequestEvent(Widget source) {
		super(source);
	}

	/**
	 * @return The view key
	 */
	public abstract ViewKey getViewKey();

	/**
	 * @return <code>true</code> if history should be updated with a view token, <code>false</code>
	 *         if history is NOT to be updated.
	 *         <p>
	 *         Default returns <code>true</code>. There are few cases when this needs to be
	 *         <code>false</code>.
	 */
	public boolean addHistory() {
		return true;
	}

	@Override
	public final String toString() {
		String s = super.toString();
		ViewKey viewKey = getViewKey();
		if(viewKey != null) {
			s += viewKey.toString();
		}
		return s;
	}
}
