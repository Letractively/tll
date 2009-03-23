/**
 * The Logic Lab
 * @author jpk Jan 25, 2008
 */
package com.tll.client.ui.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.ui.SimpleHyperLink;

/**
 * ViewRequestLink - Link that delegates a view request to the mvc dispatcher.
 * @author jpk
 */
public final class ViewRequestLink extends SimpleHyperLink implements ClickHandler {

	/**
	 * The sourcing view request.
	 */
	private IViewRequest viewRequest;

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
	public ViewRequestLink(String shortViewName, String longViewName, IViewRequest viewRequest) {
		super(shortViewName);
		setTitle(longViewName);
		setViewRequest(viewRequest);
		addClickHandler(this);
	}

	public void setViewRequest(IViewRequest viewRequest) {
		this.viewRequest = viewRequest;
	}

	public void onClick(ClickEvent event) {
		if(event.getSource() == this) {
			assert viewRequest != null;
			ViewManager.get().dispatch(viewRequest);
		}
	}

	public ViewKey getViewKey() {
		return viewRequest == null ? null : viewRequest.getViewKey();
	}
}
