/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.event.shared.GwtEvent;

/**
 * ViewChangeEvent
 * @author jpk
 */
public final class ViewChangeEvent extends GwtEvent<IViewChangeHandler> {

	public static final Type<IViewChangeHandler> TYPE = new Type<IViewChangeHandler>();
	
	/**
	 * Constructor
	 */
	public ViewChangeEvent() {
	}

	@Override
	public Type<IViewChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(IViewChangeHandler handler) {
		handler.onViewChange(this);
	}

}
