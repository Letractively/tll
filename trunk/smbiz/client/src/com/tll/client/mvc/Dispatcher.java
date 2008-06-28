/**
 * The Logic Lab
 * @author jpk Jan 14, 2008
 */
package com.tll.client.mvc;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.ui.view.ViewContainer;

/**
 * Dispatcher - Dispatches view requests to the appropriate controller and handles history
 * management relating to views.
 * @author jpk
 */
public final class Dispatcher implements HistoryListener {

	private static final Dispatcher instance = new Dispatcher();

	public static Dispatcher instance() {
		return instance;
	}

	private final List<IController> controllers = new ArrayList<IController>();

	/**
	 * The view request that is pending.
	 * <p>
	 * In order to comply with the history event system, we must be routed from the
	 * {@link HistoryListener#onHistoryChanged(String)} context.
	 * <p>
	 * <strong>AbstractView request procedure:</strong>
	 * <ol>
	 * <li>Dispatch a view request via {@link #dispatch(ViewRequestEvent)}.
	 * <li>Temporarily cache the view request in {@link #pendingViewRequest}.
	 * <li>Invoke {@link History#newItem(String)}
	 * <li>Re-acquire the view request held in {@link #pendingViewRequest}.
	 * <li>Dispatch normally
	 * </ol>
	 */
	private ViewRequestEvent pendingViewRequest;

	/**
	 * Constructor
	 */
	private Dispatcher() {
		super();
		History.addHistoryListener(this);

		// add supported controllers
		controllers.add(new ShowViewController());
		controllers.add(new UnloadViewController());
		controllers.add(new PinPopViewController());
	}

	/**
	 * Dispatches the view request event to the appropriate controller for the ultimate purpose of
	 * changing the current view.
	 * @param request The view request event
	 * @throws IllegalStateException When no supporting controller can be found for the given view
	 *           requset.
	 */
	public void dispatch(ViewRequestEvent request) {
		assert request != null : "A view request must be set";

		if(pendingViewRequest == null) {
			// need to update history first
			if(request.addHistory()) {
				assert request.getViewKey() != null : "Unable to add history: No view name specified.";
				if(request.getViewKey().getViewKeyHistoryToken().equals(History.getToken())) {
					doDispatch(request);
				}
				else {
					this.pendingViewRequest = request;
					History.newItem(request.getViewKey().getViewKeyHistoryToken());
				}
			}
			else {
				doDispatch(request);
			}
		}
		else {
			assert request == pendingViewRequest;
			pendingViewRequest = null; // reset
			doDispatch(request);
		}
	}

	private void doDispatch(ViewRequestEvent request) {
		// do actual disptach
		for(IController c : controllers) {
			if(c.canHandle(request)) {
				c.handle(request);
				return;
			}
		}
		throw new IllegalStateException("Unhandled view request: " + request.toString());
	}

	public void onHistoryChanged(String historyToken) {
		final int viewKeyHash = ViewKey.extractViewKeyHash(historyToken);
		if(viewKeyHash == -1) return;

		if(pendingViewRequest == null) {
			// presume user pressed the back button (or an IController impl altered history directly)

			// is the view still cached?
			ViewContainer vc = ViewManager.instance().findView(viewKeyHash);
			if(vc != null) {
				ViewManager.instance().setCurrentView(vc.getView());
			}
			else {
				// TODO figure out what to do here
				// currently a no-op
			}
			return;
		}

		// dispatch the view request
		dispatch(pendingViewRequest);
	}
}
