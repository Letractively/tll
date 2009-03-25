/**
 * The Logic Lab
 * @author jpk
 * Apr 5, 2008
 */
package com.tll.client.mvc.view;

/**
 * StaticViewRequest - Show view request for views that are uniquely
 * identifiable at compile time (non-dynamic). <br>
 * I.e.: The {@link IViewKey} is resolvable at compile time.
 * @author jpk
 */
public final class StaticViewRequest extends ShowViewRequest {
	
	private final IViewKey key;

	/**
	 * Constructor
	 * @param viewClass
	 */
	public StaticViewRequest(ViewClass viewClass) {
		super();
		key = new ViewKey(viewClass, 0);
	}

	@Override
	public IViewKey getViewKey() {
		return key;
	}

}
