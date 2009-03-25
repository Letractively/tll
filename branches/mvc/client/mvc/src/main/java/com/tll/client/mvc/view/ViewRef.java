/**
 * The Logic Lab
 * @author jpk
 * @since Mar 24, 2009
 */
package com.tll.client.mvc.view;

/**
 * ViewRef - Stand-alone {@link IViewRef} implementation.
 * @author jpk
 */
public final class ViewRef implements IViewRef {

	private final IViewKey viewKey;
	private final String shortViewName, longViewName;

	/**
	 * Constructor
	 * @param viewKey
	 * @param shortViewName
	 * @param longViewName
	 */
	public ViewRef(IViewKey viewKey, String shortViewName, String longViewName) {
		this.viewKey = viewKey;
		this.shortViewName = shortViewName;
		this.longViewName = longViewName;
	}

	@Override
	public IViewKey getViewKey() {
		return viewKey;
	}

	@Override
	public String getShortViewName() {
		return shortViewName;
	}

	@Override
	public String getLongViewName() {
		return longViewName;
	}
}
