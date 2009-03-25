/**
 * The Logic Lab
 * @author jpk Jan 25, 2008
 */
package com.tll.client.ui.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.IViewKey;
import com.tll.client.mvc.view.IViewRef;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.ui.SimpleHyperLink;

/**
 * ViewRequestLink - Link that delegates a view request to the mvc dispatcher.
 * @author jpk
 */
public final class ViewRequestLink extends SimpleHyperLink {

	/**
	 * The view key.
	 */
	private IViewKey viewKey;

	/**
	 * Constructor
	 */
	public ViewRequestLink() {
		super();
	}
	
	/**
	 * Constructor
	 * @param shortViewName
	 * @param longViewName
	 * @param viewKey
	 */
	public ViewRequestLink(String shortViewName, String longViewName, IViewKey viewKey) {
		setText(shortViewName);
		setTitle(longViewName);
		setViewKey(viewKey);
		addClickHandler(new ClickHandler() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void onClick(ClickEvent event) {
				if(ViewRequestLink.this.viewKey == null) throw new IllegalStateException();
				ViewManager.get().dispatch(new ShowViewRequest() {
				
					@Override
					public IViewKey getViewKey() {
						return ViewRequestLink.this.viewKey;
					}
				});
			}
		});
	}

	/**
	 * Constructor
	 * @param viewRef the view ref
	 */
	public ViewRequestLink(IViewRef viewRef) {
		this(viewRef.getShortViewName(), viewRef.getLongViewName(), viewRef.getViewKey());
	}

	public void setViewKey(final IViewKey viewKey) {
		if(viewKey == null) throw new IllegalArgumentException();
		this.viewKey = viewKey;
	}
}
