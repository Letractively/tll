/**
 * The Logic Lab
 * @author jpk Jan 25, 2008
 */
package com.tll.client.ui;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.ViewKey;

/**
 * ViewRequestLink - Link that delegates a view request to the mvc dispatcher.
 * @author jpk
 */
public final class ViewRequestLink extends SimpleHyperLink implements ClickListener {

	/**
	 * The sourcing view request.
	 */
	private ViewRequestEvent viewRequest;

	/**
	 * Constructor - Default constructor. The view request property <em>must</em>
	 * be set prior to an onclick event.
	 */
	public ViewRequestLink() {
		this(null, null, null);
	}

	/**
	 * Constructor
	 * @param shortViewName
	 * @param longViewName
	 * @param viewRequest
	 */
	public ViewRequestLink(String shortViewName, String longViewName, ViewRequestEvent viewRequest) {
		super(shortViewName);
		setTitle(longViewName);
		setViewRequest(viewRequest);
		addClickListener(this);
	}

	public void setViewRequest(ViewRequestEvent viewRequest) {
		this.viewRequest = viewRequest;
	}

	public void onClick(Widget sender) {
		if(sender == this) {
			assert viewRequest != null;
			ViewManager.instance().dispatch(viewRequest);
		}
	}

	/*
	public String getLongName() {
		return getTitle();
	}

	public void setLongName(String name) {
		setTitle(name);
	}

	public String getShortName() {
		return getText();
	}

	public void setShortName(String name) {
		setText(name);
	}
	*/

	public ViewKey getViewKey() {
		return viewRequest == null ? null : viewRequest.getViewKey();
	}
}
