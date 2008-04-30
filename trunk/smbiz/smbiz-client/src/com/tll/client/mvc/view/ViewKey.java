/**
 * The Logic Lab
 * @author jpk Jan 27, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.History;

/**
 * ViewRef - Construct that is able to uniquely identify a view at runtime.
 * @author jpk
 */
public final class ViewKey {

	/**
	 * Used to dis-ambiguate {@link History} tokens. I.e. whether the history token passed to the
	 * {@link History#onHistoryChanged(String)} method is a call for a view.
	 */
	private static final String VIEW_TOKEN_PREFIX = "v";
	private static final int VIEW_TOKEN_PREFIX_LENGTH = 1;

	/**
	 * Extracts the view key hash from the given history token. <code>-1</code> is returned if the
	 * historyToken is <em>not</em> view related.
	 * @param historyToken The possibly view related history token
	 * @return Extracted hash of the associated {@link ViewKey} or <code>-1</code> if the history
	 *         token is not a view history token.
	 */
	public static int extractViewKeyHash(String historyToken) {
		if(historyToken == null || !historyToken.startsWith(ViewKey.VIEW_TOKEN_PREFIX)) {
			return -1;
		}
		return Integer.parseInt(historyToken.substring(VIEW_TOKEN_PREFIX_LENGTH));
	}

	/**
	 * The ViewClass which may never be <code>null</code>.
	 */
	private final ViewClass viewClass;

	/**
	 * The runtime unique view id for the associated ViewClass.
	 */
	private final int viewId;

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
	 * Uniquely identifies a view at runtime. AbstractView ids should be based on the unique view type related
	 * properties specific to the view implementation.
	 * @return int representing the unique view id. If <code>0</code>, the view id is "un-defined"
	 *         which is supported (E.g.: "static" views).
	 */
	public int getViewId() {
		return viewId;
	}

	/**
	 * Provides a String wise token suitable for use as browser history event identifier.
	 * @return History token equivalent for the given view key.
	 */
	public String getViewKeyHistoryToken() {
		return VIEW_TOKEN_PREFIX + hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof ViewKey == false) return false;
		ViewKey that = (ViewKey) obj;
		return that.viewClass.equals(this.viewClass) && that.viewId == viewId;
	}

	@Override
	public int hashCode() {
		return viewId * 31 + viewClass.hashCode();
	}

	@Override
	public String toString() {
		return "view Class: " + viewClass.toString() + ", view id: " + getViewId();
	}
}
