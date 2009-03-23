/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.mvc.view;


/**
 * AbstractViewRequest - Encapsulates <em>all</em> necessary properties to freshly instantiate a
 * <em>particular</em> view at runtime.
 * @author jpk
 */
public abstract class AbstractViewRequest implements IViewRequest {

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
		final ViewKey viewKey = getViewKey();
		if(viewKey != null) {
			s += viewKey.toString();
		}
		return s;
	}
}
