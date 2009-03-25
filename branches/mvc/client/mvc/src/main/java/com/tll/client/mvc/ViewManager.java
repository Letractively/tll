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
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Panel;
import com.tll.client.mvc.view.IHasViewChangeHandlers;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewChangeHandler;
import com.tll.client.mvc.view.IViewKey;
import com.tll.client.mvc.view.IViewRef;
import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.ViewChangeEvent;
import com.tll.client.ui.view.ViewContainer;

/**
 * ViewManager - Singleton managing view life-cycles and view caching. Also
 * serves as an MVC dispatcher dispatching view requests to the appropriate view
 * controller. View history is also managed here.
 * @author jpk
 */
public final class ViewManager implements ValueChangeHandler<String>, IHasViewChangeHandlers {

	/**
	 * ViewChangeHandlers
	 * @author jpk
	 */
	@SuppressWarnings("serial")
	static final class ViewChangeHandlers extends ArrayList<IViewChangeHandler> {

		public void fireEvent(ViewChangeEvent event) {
			for(final IViewChangeHandler handler : this) {
				handler.onViewChange(event);
			}
		}
	}

	/**
	 * Used to dis-ambiguate {@link History} tokens. I.e. whether the history
	 * token passed to the {@link History} {@link ValueChangeEvent} method is a
	 * call for a view.
	 */
	private static final char VIEW_TOKEN_PREFIX = 'v';

	/**
	 * Generates a history token from a {@link IViewKey}.
	 * @param key
	 * @return the complimenting history token.
	 */
	private static String generateViewKeyHistoryToken(IViewKey key) {
		return VIEW_TOKEN_PREFIX + Integer.toString(key.hashCode());
	}

	/**
	 * Extracts the view key hash from the given history token. <code>-1</code> is
	 * returned if the historyToken is <em>not</em> view related.
	 * @param historyToken The possibly view related history token
	 * @return Extracted hash of the associated {@link IViewKey} or
	 *         <code>-1</code> if the history token is not a view history token.
	 */
	@SuppressWarnings("null")
	private static int extractViewKeyHash(String historyToken) {
		final int len = historyToken == null ? 0 : historyToken.length();
		if(len < 2) return -1;
		if(historyToken.charAt(0) != VIEW_TOKEN_PREFIX) return -1;
		return Integer.parseInt(historyToken.substring(1));
	}

	/**
	 * The maximum number of elements a view path may contain.
	 */
	private static final int MAX_VIEW_PATH_ELEMENTS = 6;

	// singleton
	private static ViewManager instance;

	/**
	 * Must be called once by the app on startup.
	 * @param parentViewPanel The Panel that is the parent of the pinned view
	 *        container. Must not be <code>null</code>.
	 * @param cacheCapacity The limit of the number of views to hold in memory.
	 */
	public static void initialize(Panel parentViewPanel, int cacheCapacity) {
		instance = new ViewManager(parentViewPanel, cacheCapacity);
	}
	
	/**
	 * Clears and nullifies the singleton instance.
	 */
	public static void shutdown() {
		if(instance != null) {
			instance.clear();
			instance = null;
		}
	}

	/**
	 * @return The singleton {@link ViewManager} instance.
	 */
	public static ViewManager get() {
		if(instance == null) throw new IllegalStateException("Not initialized.");
		return instance;
	}

	/**
	 * The parent view panel. This property must be set so that views can attach
	 * to the DOM.
	 */
	private final Panel parentViewPanel;

	/**
	 * The cache of visited views.
	 * <p>
	 * NOTE: The first view in the cache stack is considered the current view.
	 */
	private final ViewCache cache;

	/**
	 * The first pinned {@link ViewContainer}.
	 */
	private ViewContainer initialViewContainer;

	/**
	 * The currently pinned ViewContainer.
	 */
	private ViewContainer currentViewContainer;

	private final List<IController> controllers = new ArrayList<IController>();

	private final ViewChangeHandlers viewChangeHandlers = new ViewChangeHandlers();

	/**
	 * The view request that is pending.
	 * <p>
	 * In order to comply with the history event system, we must be routed from
	 * the browser history context.
	 * <p>
	 * <strong>AbstractView request procedure:</strong>
	 * <ol>
	 * <li>Dispatch a view request via {@link #dispatch(IViewRequest)}.
	 * <li>Temporarily cache the view request in {@link #pendingViewRequest}.
	 * <li>Invoke {@link History#newItem(String)}
	 * <li>Re-acquire the view request held in {@link #pendingViewRequest}.
	 * <li>Dispatch normally
	 * </ol>
	 */
	private IViewRequest pendingViewRequest;

	/**
	 * Constructor
	 * @param parentPanel The panel that will contained pinned views.
	 * @param cacheCapacity The limit of the number of views to hold in the cache.
	 */
	private ViewManager(Panel parentPanel, int cacheCapacity) {
		parentViewPanel = parentPanel;
		cache = new ViewCache(cacheCapacity);
		History.addValueChangeHandler(this);

		// add supported controllers
		controllers.add(new ShowViewController());
		controllers.add(new UnloadViewController());
		controllers.add(new PinPopViewController());
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		if(event.getAssociatedType() == ViewChangeEvent.getType()) {
			viewChangeHandlers.fireEvent((ViewChangeEvent) event);
		}
	}

	@Override
	public void addViewChangeHandler(IViewChangeHandler handler) {
		viewChangeHandlers.add(handler);
	}

	@Override
	public void removeViewChangeHandler(IViewChangeHandler handler) {
		viewChangeHandlers.remove(handler);
	}

	/**
	 * Sets the current view. The current view is defined as the visible pinned
	 * view.
	 * @param view The view to be set as current
	 */
	void setCurrentView(IView<?> view) {
		assert view != null;
		Log.debug("ViewManager.setCurrentView() - " + view);

		final int cacheIndex = cache.searchCache(view.getViewKey());

		ViewContainer vc;

		// if the given view is currently in the
		// cache we need to remove it BEFORE pushing the current view to the cache
		// to prevent the caching limit from being capacity-1 (as opposed to what
		// it should be: capacity)
		if(cacheIndex != -1) {
			vc = cache.removeAt(cacheIndex);
			assert vc != null;
		}
		else {
			vc = new ViewContainer(view);
		}

		// determine whether to show pinned or popped
		// only show as popped if not currently in cache
		final boolean showPopped = ((cacheIndex == -1) && view.getOptions().isInitiallyPopped());

		// set the view
		if(showPopped) {
			// NOTE: the view history is not affected!
			vc.pop(parentViewPanel);
		}
		else {
			// remove current pinned view
			if(currentViewContainer != null) {
				parentViewPanel.remove(currentViewContainer);
			}
			vc.pin(parentViewPanel);
			currentViewContainer = vc;
		}

		// add the view to the cache
		ViewContainer old = cache.set(vc);
		if(old != null) {
			assert old != vc && !old.getViewKey().equals(vc.getViewKey());
			Log.debug("Destroying view - " + old.getView().toString() + "..");
			// view life-cycle destroy
			old.onDestroy();
			old = null;
		}

		// set the initial view if not set
		if(initialViewContainer == null) initialViewContainer = vc;

		// fire view changed event
		viewChangeHandlers.fireEvent(new ViewChangeEvent());
	}

	/**
	 * Unloads the given view optionally removing it from the view cache then
	 * updates the current pinned view routing through the history system.
	 * @param key The view key of the view to unload
	 * @param removeFromCache Remove the view from the view cache?
	 */
	void unloadView(IViewKey key, boolean removeFromCache) {
		final ViewContainer vc = findView(key);
		if(vc == null) throw new IllegalArgumentException("View not in cache.");
		
		// unload the given view
		vc.close();

		if(removeFromCache) {
			// remove the view from cache
			cache.remove(key);
		}
		else {
			// demote the view
			cache.moveToLast(vc);
		}

		// fire view changed event
		// NOTE: this is necessary to ensure the view history panel is properly
		// updated
		viewChangeHandlers.fireEvent(new ViewChangeEvent());

		// find the newest pinned view excluding the one to be unloaded
		final ViewContainer newestPinned = findFirstView(false, key);
		if(newestPinned != null) {
			History.newItem(generateViewKeyHistoryToken(newestPinned.getViewKey()));
		}
	}

	/**
	 * Removes all view artifacts from the DOM and clears the cache.
	 */
	public void clear() {
		Log.debug("Clearing view cache..");
		if(cache.cacheSize() == 0) {
			return;
		}
		for(final Iterator<ViewContainer> itr = cache.cacheIterator(); itr.hasNext();) {
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
	 * @param popped Is desired view popped?
	 * @param exclude The view key of a view to exclude from the search. May be
	 *        <code>null</code>.
	 * @return The first found matching view or <code>null</code> if no match
	 *         found.
	 */
	private ViewContainer findFirstView(boolean popped, IViewKey exclude) {
		final Iterator<ViewContainer> itr = cache.cacheIterator();
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewContainer vc = itr.next();
				if(exclude == null || !exclude.equals(vc.getViewKey())) {
					if(vc.isPopped() == popped) {
						return vc;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Locates a cached view given a view key.
	 * @param viewKey The view key
	 * @return The found {@link IView} or <code>null</code> if not present in the
	 *         view cache.
	 */
	public ViewContainer findView(IViewKey viewKey) {
		final Iterator<ViewContainer> itr = cache.cacheIterator();
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewContainer vc = itr.next();
				if(vc.getViewKey().equals(viewKey)) {
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
	private ViewContainer findView(int viewKeyHash) {
		final Iterator<ViewContainer> itr = cache.cacheIterator();
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewContainer vc = itr.next();
				final int hc = vc.getViewKey().hashCode();
				if(hc == viewKeyHash) {
					return vc;
				}
			}
		}
		return null;
	}

	/**
	 * @return Never <code>null</code> array of the currently cached views which
	 *         may be empty indicating there are no cached views.
	 */
	public IView<?>[] getCachedViews() {
		if(cache.cacheSize() == 0) {
			return new IView[] {};
		}
		final ArrayList<IView<?>> list = new ArrayList<IView<?>>(cache.cacheSize());
		for(final Iterator<ViewContainer> itr = cache.cacheIterator(); itr.hasNext();) {
			list.add(itr.next().getView());
		}
		return list.toArray(new IView[list.size()]);
	}

	/**
	 * Provides an array of all cached views that are NOT in the popped state from
	 * oldest to newest.
	 * @return Array of never <code>null</code> {@link IViewRef}s which may be
	 *         empty.
	 */
	public IViewRef[] getRecentViews() {
		final List<ViewContainer> list = new ArrayList<ViewContainer>();
		if(cache.cacheSize() == 0) {
			return new IViewRef[] {};
		}
		for(final Iterator<ViewContainer> itr = cache.cacheIterator(); itr.hasNext();) {
			final ViewContainer vc = itr.next();
			if(!vc.getViewState().isPopped()) {
				list.add(vc);
			}
		}
		final int cnt = list.size();
		final IViewRef[] array = new IViewRef[cnt];
		int i = cnt;
		for(final Iterator<ViewContainer> itr = list.iterator(); itr.hasNext();) {
			final ViewContainer vc = itr.next();
			array[--i] = vc.getView();
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
		for(final Iterator<ViewContainer> itr = cache.cacheIterator(); itr.hasNext();) {
			final ViewContainer vc = itr.next();
			if(vc != initialViewContainer && vc != currentViewContainer && !vc.isPopped()) {
				if(list.size() == MAX_VIEW_PATH_ELEMENTS) break;
				list.add(0, vc.getView());
			}
		}

		// fill up to max allowed view path elements with secondary cache entries
		if(list.size() < MAX_VIEW_PATH_ELEMENTS) {
			final Iterator<IViewRef> itr = cache.visitedIterator();
			if(itr != null) {
				while(itr.hasNext() && list.size() <= MAX_VIEW_PATH_ELEMENTS) {
					final IViewRef ref = itr.next();
					if(!ref.getViewKey().equals(currentViewContainer.getViewKey())) {
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
	 * document routing the {@link IViewKey} of the view that is to be the
	 * subsequent pinned view through the browser history system.
	 */
	void popCurrentView() {
		if(currentViewContainer != null) {
			assert !currentViewContainer.isPopped();

			// pop the view
			currentViewContainer.pop(parentViewPanel);

			final ViewContainer nextCurrent = findFirstView(false, currentViewContainer.getViewKey());
			final IViewKey vk = nextCurrent == null ? null : nextCurrent.getViewKey();
			if(vk != null) {
				History.newItem(generateViewKeyHistoryToken(vk));
			}
		}
	}

	/**
	 * Pins a popped view.
	 * @param key the view key of the popped view to pin
	 */
	void pinPoppedView(IViewKey key) {
		final ViewContainer vc = findView(key);
		if(vc != null && vc.isPopped()) {
			setCurrentView(vc.getView());
		}
	}

	/**
	 * Dispatches the view request event to the appropriate controller for the
	 * ultimate purpose of changing the current view.
	 * @param request The view request event
	 * @throws IllegalStateException When no supporting controller can be found
	 *         for the given view requset.
	 */
	public void dispatch(IViewRequest request) {
		assert request != null : "A view request must be set";

		if(pendingViewRequest == null) {
			// need to update history first
			if(request.addHistory()) {
				assert request.getViewKey() != null : "Unable to add history: No view key specified.";
				final int hash = extractViewKeyHash(History.getToken());
				if(hash != -1 && request.getViewKey().hashCode() == hash) {
					doDispatch(request);
				}
				else {
					this.pendingViewRequest = request;
					final String htoken = generateViewKeyHistoryToken(request.getViewKey());
					Log.debug("Routing view '" + request.getViewKey() + "' through history with hash: " + htoken);
					History.newItem(htoken);
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

	private void doDispatch(IViewRequest request) {
		// do actual disptach
		Log.debug("Dispatching view request: " + request + " ..");
		for(final IController c : controllers) {
			if(c.canHandle(request)) {
				c.handle(request);
				return;
			}
		}
		throw new IllegalStateException("Unhandled view request: " + request.toString());
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		final int viewKeyHash = extractViewKeyHash(event.getValue());
		if(viewKeyHash == -1) return;
		Log.debug("Handling view history hash: " + viewKeyHash + "..");

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
