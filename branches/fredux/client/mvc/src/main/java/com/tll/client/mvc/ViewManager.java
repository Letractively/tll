/**
 * The Logic Lab
 * @author jpk Jan 3, 2008
 */
package com.tll.client.mvc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Panel;
import com.tll.client.model.IModelChangeListener;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.mvc.view.ISourcesViewEvents;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewEventListener;
import com.tll.client.mvc.view.IViewRef;
import com.tll.client.mvc.view.ViewChangedEvent;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.mvc.view.ViewRequestEvent;
import com.tll.client.ui.view.ViewContainer;

/**
 * ViewManager - Singleton managing view life-cycles and view caching. Also
 * serves as an MVC dispatcher dispatching view requests to the appropriate view
 * controller. View history is also managed here.
 * @author jpk
 */
public final class ViewManager implements ISourcesViewEvents, IModelChangeListener, ValueChangeHandler<String> {

	/**
	 * The default number of views to cache.
	 */
	private static final int CACHE_CAPACITY = 4;

	/**
	 * The maximum number of elements a view path may contain.
	 */
	private static final int MAX_VIEW_PATH_ELEMENTS = 6;

	// singleton
	private static ViewManager instance;

	public static ViewManager get() {
		if(instance == null) instance = new ViewManager();
		return instance;
	}

	/**
	 * The parent view panel. This property must be set so that views can attach
	 * to the DOM.
	 */
	private Panel parentViewPanel;

	/**
	 * The cache of visited views.
	 * <p>
	 * NOTE: The first view in the cache stack is considered the current view.
	 */
	private final ViewStack cache = new ViewStack(CACHE_CAPACITY);

	/**
	 * The first pinned {@link ViewContainer}.
	 */
	private ViewContainer initialViewContainer;

	/**
	 * The currently pinned ViewContainer.
	 */
	private ViewContainer currentViewContainer;

	/**
	 * The view listener collection.
	 */
	private final ViewEventListenerCollection viewListeners = new ViewEventListenerCollection();

	private final List<IController> controllers = new ArrayList<IController>();

	/**
	 * The view request that is pending.
	 * <p>
	 * In order to comply with the history event system, we must be routed from
	 * the browser history context.
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
	private ViewManager() {
		super();

		History.addValueChangeHandler(this);

		// add supported controllers
		controllers.add(new ShowViewController());
		controllers.add(new UnloadViewController());
		controllers.add(new PinPopViewController());
	}

	public void addViewEventListener(IViewEventListener listener) {
		viewListeners.add(listener);
	}

	public void removeViewEventListener(IViewEventListener listener) {
		viewListeners.remove(listener);
	}

	/**
	 * Must be called once by the app on startup.
	 * @param parentViewPanel The Panel that is the parent of the pinned view
	 *        container. Must not be <code>null</code>.
	 */
	public void initialize(Panel parentViewPanel) {
		if(parentViewPanel == null) throw new IllegalArgumentException("A parent view panel must be specified.");
		this.parentViewPanel = parentViewPanel;
	}

	/**
	 * Sets the current view. The current view is defined as the visible pinned
	 * view.
	 * @param view The view to be set as current
	 */
	void setCurrentView(IView view) {
		assert view != null;
		Log.debug("ViewManager.setCurrentView() - " + view);

		final int cacheIndex = cache.searchCache(view.getViewKey());

		ViewContainer vc;

		// if the given view is currently in the
		// cache we need to remove it BEFORE pushing the current view to the cache
		// to prevent the caching limit from being capacity-1 (as opposed to what
		// it should be: capacity)
		if(cacheIndex != -1) {
			vc = cache.remove(cacheIndex);
		}
		else {
			vc = new ViewContainer(view);
		}

		// determine whether to show pinned or popped
		final boolean showPopped = ((cacheIndex == -1) && view.getOptions().isInitiallyPopped());

		// set the view
		try {
			if(showPopped) {
				// NOTE: the view history is not affected!
				vc.pop(parentViewPanel);
			}
			else {
				assert vc != null;
				// parentViewPanel.clear();
				if(currentViewContainer != null) {
					parentViewPanel.remove(currentViewContainer);
				}
				vc.pin(parentViewPanel);
				currentViewContainer = vc;
			}
		}
		catch(final RuntimeException re) {
			if(cacheIndex != -1 && currentViewContainer != null) {
				// revert the cache state
				cache.add(cacheIndex, currentViewContainer);
			}
			throw re;
		}

		// add the view to the cache
		ViewContainer oldViewContainer = cache.push(vc);
		if(oldViewContainer != null) {
			Log.debug("Destroying view - " + oldViewContainer.getView().toString() + "..");
			// view life-cycle destroy
			oldViewContainer.onDestroy();
			oldViewContainer = null;
		}

		// set the initial view if not set
		if(initialViewContainer == null) initialViewContainer = vc;

		// fire view changed event
		viewListeners.fireOnViewChanged(new ViewChangedEvent(vc));
	}

	/**
	 * Unloads the given view from the DOM document retaining it in the view
	 * cache.
	 * <p>
	 * <strong>NOTE: </strong>This method does NOT subsequently set the current
	 * view.
	 * @param vc The view container of the view to unload
	 * @param removeFromCache Remove the view from the view cache?
	 * @return The most recently viewed pinned IView <em>never</em> equalling the
	 *         IView being unloaded or <code>null</code> if no alternate pinned
	 *         IView is available.
	 */
	IView unloadView(ViewContainer vc, boolean removeFromCache) {
		assert vc != null;

		// find the newest pinned view excluding the one to be unloaded
		final ViewContainer newestPinned = findFirstView(0, false, vc.getView().getViewKey());
		if(newestPinned == null) {
			// we have no alternate pinned view to show!
			return null;
		}

		// unload the given view
		vc.close();

		if(removeFromCache) {
			// remove the view from cache
			cache.remove(cache.searchCache(vc.getView().getViewKey()));
		}
		else {
			// demote the view
			cache.moveToLast(vc);
		}

		// fire view changed event
		// NOTE: this is necessary to ensure the view history panel is properly
		// updated
		viewListeners.fireOnViewChanged(new ViewChangedEvent(vc));

		return newestPinned.getView();
	}

	/**
	 * Removes all view artifacts from the DOM and clears the cache.
	 */
	public void clear() {
		Log.debug("Clearing view cache..");
		if(cache.cacheSize() == 0) {
			return;
		}
		for(final Iterator<ViewContainer> itr = cache.cacheIterator(0); itr.hasNext();) {
			final ViewContainer vc = itr.next();
			vc.close();
			vc.onDestroy();
		}
		cache.emptyCache();
		Log.debug("View cache cleared");
	}

	/**
	 * Generic find view method returning the first found match in the view cache
	 * starting at the given index.
	 * @param beginIndex The starting view cache where searching starts
	 * @param popped Is desired view popped?
	 * @param exclude The view key of a view to exclude from the search. May be
	 *        <code>null</code>.
	 * @return The first found matching view or <code>null</code> if no match
	 *         found.
	 */
	private ViewContainer findFirstView(int beginIndex, boolean popped, ViewKey exclude) {
		assert beginIndex >= 0 && beginIndex <= cache.cacheSize();
		final Iterator<ViewContainer> itr = cache.cacheIterator(beginIndex);
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewContainer vc = itr.next();
				if(exclude == null || !exclude.equals(vc.getView().getViewKey())) {
					if(vc.isPopped() == popped) {
						return vc;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Locates a cached view given the view widget or child view widget.
	 * @param child
	 * @return The found {@link IView} or <code>null</code> if not present in the
	 *         view cache.
	 */
	/*
	ViewContainer findView(Widget child) {
		Iterator itr = cache.cacheIterator(0);
		if(itr != null) {
			final Element elem = child.getElement();
			while(itr.hasNext()) {
				ViewContainer vc = (ViewContainer) itr.next();
				if(DOM.isOrHasChild(vc.getElement(), elem)) {
					return vc;
				}
			}
		}
		return null;
	}
	*/

	/**
	 * Locates a cached view given a view key.
	 * @param viewKey The view key
	 * @return The found {@link IView} or <code>null</code> if not present in the
	 *         view cache.
	 */
	ViewContainer findView(ViewKey viewKey) {
		final Iterator<ViewContainer> itr = cache.cacheIterator(0);
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewContainer vc = itr.next();
				if(vc.getView().getViewKey().equals(viewKey)) {
					return vc;
				}
			}
		}
		return null;
	}

	/**
	 * Locates a cached view given a view key hash.
	 * @param viewKeyHash
	 * @return The found {@link IView} or <code>null</code> if not present in the
	 *         view cache.
	 */
	ViewContainer findView(int viewKeyHash) {
		final Iterator<ViewContainer> itr = cache.cacheIterator(0);
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewContainer vc = itr.next();
				final int hc = vc.getView().getViewKey().hashCode();
				if(hc == viewKeyHash) {
					return vc;
				}
			}
		}
		return null;
	}

	/**
	 * Provides an array of all cached views that are NOT in the popped state from
	 * oldest to newest.
	 * @return Array of {@link IViewRef}s
	 */
	public IViewRef[] getRecentViews() {
		final List<ViewContainer> list = new ArrayList<ViewContainer>();
		for(final Iterator<ViewContainer> itr = cache.cacheIterator(0); itr.hasNext();) {
			final ViewContainer vc = itr.next();
			if(!vc.getViewState().isPopped()) {
				list.add(vc);
			}
		}
		final int cnt = list.size();
		final IViewRef[] array = new IViewRef[cnt];
		if(cnt > 0) {
			int i = cnt;
			for(final Iterator<ViewContainer> itr = list.iterator(); itr.hasNext();) {
				final ViewContainer vc = itr.next();
				array[--i] = vc.getView();
			}
		}
		return array;
	}

	/**
	 * Provides an array of {@link IViewRef}s representing the current "view
	 * path". The view path is a list of visited views beginning from the
	 * initially viewed view ending one before the current pinned view with a
	 * subset of visited views in between. Determining the views in between the
	 * initial and current view follows this logic:
	 * <ol>
	 * <li>TODO
	 * </ol>
	 * @return Array of {@link IViewRef}s representing the current view path.
	 */
	public IViewRef[] getViewPath() {
		assert initialViewContainer != null;

		if(cache.cacheSize() < 2) return null;

		final List<IViewRef> list = new ArrayList<IViewRef>(MAX_VIEW_PATH_ELEMENTS);

		// spew out primary view cache
		for(final Iterator<ViewContainer> itr = cache.cacheIterator(0); itr.hasNext();) {
			final ViewContainer vc = itr.next();
			if(vc != initialViewContainer && vc != currentViewContainer && !vc.isPopped()) {
				if(list.size() == MAX_VIEW_PATH_ELEMENTS) break;
				list.add(0, vc.getView());
			}
		}

		// fill up to max allowed view path elements with secondary cache entries
		if(list.size() < MAX_VIEW_PATH_ELEMENTS) {
			final Iterator<IViewRef> itr = cache.visitedIterator(0);
			if(itr != null) {
				while(itr.hasNext() && list.size() <= MAX_VIEW_PATH_ELEMENTS) {
					final IViewRef ref = itr.next();
					if(!ref.getViewKey().equals(currentViewContainer.getView().getViewKey())) {
						list.add(0, ref);
					}
				}
			}
		}

		// ensure initial view is present
		if(currentViewContainer != initialViewContainer) {
			boolean exists = false;
			for(final Iterator<IViewRef> itr = list.iterator(); itr.hasNext();) {
				final IViewRef ref = itr.next();
				if(ref.getViewKey().equals(initialViewContainer.getView().getViewKey())) {
					exists = true;
					break;
				}
			}
			if(!exists) {
				list.add(0, initialViewContainer.getView());
				if(list.size() > MAX_VIEW_PATH_ELEMENTS) {
					list.remove(list.size() - 1);
				}
			}
		}

		return list.toArray(new IViewRef[list.size()]);
	}

	/**
	 * Pops the currently visible pinned view out of the natural flow of the DOM
	 * document returning the {@link ViewKey} of the view that is to be the
	 * subsequent pinned view. We don't set it here as it needs to be routed
	 * through the history mechanism so the view history integrity is maintained.
	 * @return {@link ViewKey} representing the target for the subsequent pinned
	 *         view or <code>null</code> if no target was resolved.
	 */
	ViewKey popCurrentView() {
		if(currentViewContainer == null) return null;
		assert !currentViewContainer.isPopped();

		// pop the view
		currentViewContainer.pop(parentViewPanel);

		final ViewContainer nextCurrent = findFirstView(1, false, null);
		return nextCurrent == null ? null : nextCurrent.getView().getViewKey();
	}

	/**
	 * Closes a popped view.
	 * @param vc The view container to close.
	 * @return <code>true</code> if the given view is in popped state and
	 *         successful.
	 */
	boolean closePoppedView(ViewContainer vc) {
		if(vc == null || !vc.isPopped()) return false;
		vc.close();
		return true;
	}

	public void onModelChangeEvent(ModelChangeEvent event) {
		// pass the event to all cached views
		final Iterator<ViewContainer> itr = cache.cacheIterator(0);
		while(itr.hasNext()) {
			final IView view = itr.next().getView();
			view.onModelChangeEvent(event);
		}
	}

	/**
	 * Dispatches the view request event to the appropriate controller for the
	 * ultimate purpose of changing the current view.
	 * @param request The view request event
	 * @throws IllegalStateException When no supporting controller can be found
	 *         for the given view requset.
	 */
	public void dispatch(ViewRequestEvent request) {
		assert request != null : "A view request must be set";

		if(pendingViewRequest == null) {
			// need to update history first
			if(request.addHistory()) {
				assert request.getViewKey() != null : "Unable to add history: No view key specified.";
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
		for(final IController c : controllers) {
			if(c.canHandle(request)) {
				c.handle(request);
				return;
			}
		}
		throw new IllegalStateException("Unhandled view request: " + request.toString());
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		final int viewKeyHash = ViewKey.extractViewKeyHash(event.getValue());
		if(viewKeyHash == -1) return;

		if(pendingViewRequest == null) {
			// presume user pressed the back button (or an IController impl altered
			// history directly)

			// is the view still cached?
			final ViewContainer vc = findView(viewKeyHash);
			if(vc != null) {
				setCurrentView(vc.getView());
			}
			else {
				// TODO figure out what to do here
				// currently a no-op
				Log.trace("ViewManager.onHistoryChanged() - Can't find view container for " + event.getValue());
			}
			return;
		}

		// dispatch the view request
		dispatch(pendingViewRequest);
	}
}
