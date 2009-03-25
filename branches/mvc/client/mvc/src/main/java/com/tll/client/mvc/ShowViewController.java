/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.mvc;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewKey;
import com.tll.client.mvc.view.IViewKeyProvider;
import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.UI;
import com.tll.client.ui.view.ViewContainer;

/**
 * ShowViewController
 * @author jpk
 */
class ShowViewController implements IController {

	/**
	 * Resolves a {@link AbstractView} given a view key by first checking the view
	 * cache. If no matching view is cached, a fresh view instance if provided by
	 * way of {@link ViewClass}'s factory method.
	 * @param viewRequest The view request. May NOT be <code>null</code>.
	 * @return Either a cached or fresh {@link AbstractView} instance that is
	 *         never <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	private static final IView<IViewKeyProvider> resolveView(IViewKeyProvider viewRequest) {
		assert viewRequest != null;
		final IViewKey viewKey = viewRequest.getViewKey();
		assert viewKey != null;
		final ViewContainer vc = ViewManager.get().findView(viewKey);
		if(vc == null) {
			// create a fresh instance
			final IView<IViewKeyProvider> view = (IView<IViewKeyProvider>) viewKey.getViewClass().newView();
			// initialize the view - set its view key
			view.initialize(viewRequest);
			// load the view
			view.refresh();
			return view;
		}
		return (IView<IViewKeyProvider>) vc.getView();
	}

	/**
	 * Sets the view.
	 * @param view
	 */
	private static final void onViewReady(final IView<?> view) {
		final boolean showBusy = (ViewManager.get().findView(view.getViewKey()) == null);
		if(showBusy) UI.busy();
		DeferredCommand.addCommand(new Command() {

			public void execute() {
				try {
					ViewManager.get().setCurrentView(view);
				}
				finally {
					if(showBusy) UI.unbusy();
				}
			}
		});
	}

	public boolean canHandle(IViewRequest request) {
		return request instanceof ShowViewRequest;
	}

	/**
	 * Default implementation.
	 * <p>
	 * Particular controllers may need to override this method to ensure proper
	 * view initialization.
	 * @param request
	 */
	public void handle(IViewRequest request) {
		onViewReady(resolveView(request));
	}
}
