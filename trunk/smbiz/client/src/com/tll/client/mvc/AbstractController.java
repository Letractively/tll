/**
 * The Logic Lab
 * @author jpk Jan 14, 2008
 */
package com.tll.client.mvc;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.tll.client.App;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.ui.view.ViewContainer;

/**
 * AbstractController - Base class for view controller implementations.
 * @author jpk
 */
public abstract class AbstractController implements IController {

	/**
	 * Resolves a {@link AbstractView} given a view key by first checking the view
	 * cache. If no matching view is cached, a fresh view instance if provided by
	 * way of {@link ViewClass}'s factory method.
	 * @param viewRequest The view request. May NOT be <code>null</code>.
	 * @return Either a cached or fresh {@link AbstractView} instance that is
	 *         never <code>null</code>.
	 */
	protected static final IView resolveView(ViewRequestEvent viewRequest) {
		assert viewRequest != null;
		ViewKey viewKey = viewRequest.getViewKey();
		assert viewKey != null;
		ViewContainer vc = ViewManager.instance().findView(viewKey);
		if(vc == null) {
			// create a fresh instance
			IView view = viewKey.getViewClass().newView();
			// initialize the view
			view.initialize(viewRequest);
			// load the view
			view.refresh();
			return view;
		}
		return vc.getView();
	}

	/**
	 * Sets the view.
	 * @param managedViewProvider
	 */
	protected static final void onViewReady(final IView view) {
		final boolean showBusy = (ViewManager.instance().findView(view.getViewKey()) == null);
		if(showBusy) App.busy();
		DeferredCommand.addCommand(new Command() {

			public void execute() {
				try {
					ViewManager.instance().setCurrentView(view);
				}
				finally {
					if(showBusy) App.unbusy();
				}
			}
		});
	}

	/**
	 * Default implementation.
	 * <p>
	 * Particular controllers may need to override this method to ensure proper
	 * view initialization.
	 * @param request
	 */
	public void handle(ViewRequestEvent request) {
		onViewReady(resolveView(request));
	}
}
