/**
 * The Logic Lab
 * @author jpk Jan 25, 2008
 */
package com.tll.client.ui.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.IViewInitializer;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewRef;
import com.tll.client.ui.SimpleHyperLink;

/**
 * ViewLink - Link that delegates a show view request to the mvc
 * dispatcher.
 * @author jpk
 */
public final class ViewLink extends SimpleHyperLink {

	/**
	 * The view initializer.
	 */
	private IViewInitializer init;

	/**
	 * Constructor
	 */
	public ViewLink() {
		super();
	}

	/**
	 * Constructor
	 * @param shortViewName
	 * @param longViewName
	 */
	public ViewLink(String shortViewName, String longViewName) {
		this(shortViewName, longViewName, null);
	}

	/**
	 * Constructor
	 * @param shortViewName
	 * @param longViewName
	 * @param init
	 */
	public ViewLink(String shortViewName, String longViewName, IViewInitializer init) {
		setText(shortViewName);
		setTitle(longViewName);
		setViewInitializer(init);
		addClickHandler(new ClickHandler() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void onClick(ClickEvent event) {
				if(ViewLink.this.init == null) throw new IllegalStateException();
				ViewManager.get().dispatch(new ShowViewRequest(ViewLink.this.init));
			}
		});
	}

	/**
	 * Constructor
	 * @param viewRef
	 */
	public ViewLink(ViewRef viewRef) {
		this(viewRef.getShortViewName(), viewRef.getLongViewName(), viewRef.getViewInitializer());
	}

	public void setViewInitializer(final IViewInitializer init) {
		this.init = init;
	}
}
