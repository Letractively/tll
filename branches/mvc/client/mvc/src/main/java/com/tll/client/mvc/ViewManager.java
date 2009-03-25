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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Panel;
import com.tll.client.mvc.ViewCache.ViewAndInit;
import com.tll.client.mvc.view.IHasViewChangeHandlers;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewChangeHandler;
import com.tll.client.mvc.view.IViewInitializer;
import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.ViewChangeEvent;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.mvc.view.ViewOptions;
import com.tll.client.mvc.view.ViewRef;
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
	 * Generates a history token from a {@link ViewKey}.
	 * @param key
	 * @return the complimenting history token.
	 */
	private static String generateViewKeyHistoryToken(ViewKey key) {
		return VIEW_TOKEN_PREFIX + Integer.toString(key.hashCode());
	}

	/**
	 * Extracts the view key hash from the given history token. <code>-1</code> is
	 * returned if the historyToken is <em>not</em> view related.
	 * @param historyToken The possibly view related history token
	 * @return Extracted hash of the associated {@link ViewKey} or <code>-1</code>
	 *         if the history token is not a view history token.
	 */
	@SuppressWarnings("null")
	private static int extractViewKeyHash(String historyToken) {
		final int len = historyToken == null ? 0 : historyToken.length();
		if(len < 2) return -1;
		if(historyToken.charAt(0) != VIEW_TOKEN_PREFIX) return -1;
		return Integer.parseInt(historyToken.substring(1));
	}

	private static ViewManager instance;

	/**
	 * Must be called once by the app on startup.
	 * @param parentViewPanel The Panel that is the parent of the pinned view
	 *        container. Must not be <code>null</code>.
	 * @param primaryCacheCapacity The limit of the number of views to hold in
	 *        memory.
	 * @param secondaryCacheCapacity The limit of the the number of
	 *        {@link ViewRef}s to hold in memory which is filled when views
	 *        "expire" from the primary cache.
	 */
	public static void initialize(Panel parentViewPanel, int primaryCacheCapacity, int secondaryCacheCapacity) {
		instance = new ViewManager(parentViewPanel, primaryCacheCapacity, secondaryCacheCapacity);
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
	 * The first cached view.
	 */
	private ViewAndInit initialView;

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
	 * <strong>view request procedure:</strong>
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
	 * @param primaryCacheCapacity
	 * @param secondaryCacheCapacity
	 * @see ViewManager#initialize(Panel, int, int)
	 */
	private ViewManager(Panel parentPanel, int primaryCacheCapacity, int secondaryCacheCapacity) {
		parentViewPanel = parentPanel;
		cache = new ViewCache(primaryCacheCapacity, secondaryCacheCapacity);
		History.addValueChangeHandler(this);

		// add supported controllers
		controllers.add(new ShowViewController());
		controllers.add(new UnloadViewController());
		controllers.add(new PinPopViewController());
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
	 * @param init The view initializer
	 * @param options The view options
	 */
	@SuppressWarnings("unchecked")
	void setCurrentView(IViewInitializer init, ViewOptions options) {
		final ViewKey key = init.getViewKey();
		Log.debug("Setting current view: '" + key + "' ..");

		ViewAndInit e;

		final int cacheIndex = cache.searchPrimary(key);

		if(cacheIndex != -1) {
			// existing cached view
			e = cache.removeAt(cacheIndex);
			assert e != null;
		}
		else {
			// non-cached view
			final IView<IViewInitializer> view = (IView<IViewInitializer>) key.getViewClass().newView();
			// initialize the view
			view.initialize(init);
			// load the view
			view.refresh();
			
			e = new ViewAndInit(new ViewContainer(view, options, key), init);
		}
		
		setCurrentView(e, ((cacheIndex == -1) && (options != null && options
				.isInitiallyPopped())));
	}

	/**
	 * Sets the current view given a presently cached view container.
	 * @param e primary cache element
	 * @param showPopped show popped or pinned?
	 */
	private void setCurrentView(ViewAndInit e, boolean showPopped) {
		// determine whether to show pinned or popped
		// only show as popped if not currently in cache
		
		final ViewContainer vc = e.vc;
		assert vc != null;

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
		ViewAndInit old = cache.cache(vc, e.init);
		if(old != null) {
			assert old != e && !old.vc.getViewKey().equals(vc.getViewKey());
			Log.debug("Destroying view - " + old.vc.getView().toString() + "..");
			// view life-cycle destroy
			old.vc.getView().onDestroy();
			old = null;
		}

		// set the initial view if not set
		if(initialView == null) initialView = e;

		// fire view changed event
		fireViewChangeEvent();
	}

	/**
	 * Fires a view change event to all subscribed handlers.
	 */
	private void fireViewChangeEvent() {
		DeferredCommand.addCommand(new Command() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void execute() {
				viewChangeHandlers.fireEvent(new ViewChangeEvent());
			}
		});
	}

	/**
	 * Unloads the given view optionally removing it from the view cache then
	 * updates the current pinned view routing through the history system.
	 * @param key The view key of the view to unload
	 * @param removeFromCache Remove the view from the view cache?
	 */
	void unloadView(ViewKey key, boolean removeFromCache) {
		final ViewAndInit e = findView(key);
		if(e == null) return;
		
		// unload the given view
		e.vc.close();

		if(removeFromCache) {
			// remove the view from cache
			cache.remove(key);
		}
		/*
		else {
			// demote the view
			cache.moveToLast(vc);
		}
		*/
		
		// fire view changed event
		// NOTE: this is necessary to ensure the view history panel is properly
		// updated
		//viewChangeHandlers.fireEvent(new ViewChangeEvent());

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
		if(cache.primarySize() > 0) {
			for(final Iterator<ViewAndInit> itr = cache.primaryIterator(); itr.hasNext();) {
				final ViewAndInit e = itr.next();
				e.vc.close();
				e.vc.getView().onDestroy();
			}
		}
		cache.clear();
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
	private ViewContainer findFirstView(boolean popped, ViewKey exclude) {
		final Iterator<ViewAndInit> itr = cache.primaryIterator();
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewAndInit e = itr.next();
				if(exclude == null || !exclude.equals(e.vc.getViewKey())) {
					if(e.vc.isPopped() == popped) {
						return e.vc;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Locates a cached view given a view key.
	 * @param key The view key
	 * @return The found {@link IView} or <code>null</code> if not present in the
	 *         view cache.
	 */
	private ViewAndInit findView(ViewKey key) {
		final Iterator<ViewAndInit> itr = cache.primaryIterator();
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewAndInit e = itr.next();
				if(e.vc.getViewKey().equals(key)) {
					return e;
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
	private ViewAndInit findView(int viewKeyHash) {
		final Iterator<ViewAndInit> itr = cache.primaryIterator();
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewAndInit e = itr.next();
				final int hc = e.vc.getViewKey().hashCode();
				if(hc == viewKeyHash) {
					return e;
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
		if(cache.primarySize() == 0) {
			return new IView[] {};
		}
		final ArrayList<IView<?>> list = new ArrayList<IView<?>>(cache.primarySize());
		for(final Iterator<ViewAndInit> itr = cache.primaryIterator(); itr.hasNext();) {
			list.add(itr.next().vc.getView());
		}
		return list.toArray(new IView[list.size()]);
	}

	/**
	 * Provides an array of all cached views that are NOT in the popped state from
	 * oldest to newest.
	 * @return Array of never <code>null</code> {@link ViewRef}s which may be
	 *         empty.
	 */
	public ViewRef[] getRecentViews() {
		final List<ViewAndInit> list = new ArrayList<ViewAndInit>();
		if(cache.primarySize() == 0) {
			return new ViewRef[] {};
		}
		for(final Iterator<ViewAndInit> itr = cache.primaryIterator(); itr.hasNext();) {
			final ViewAndInit e = itr.next();
			if(!e.vc.isPopped()) {
				list.add(e);
			}
		}
		final int cnt = list.size();
		final ViewRef[] array = new ViewRef[cnt];
		int i = cnt;
		for(final Iterator<ViewAndInit> itr = list.iterator(); itr.hasNext();) {
			final ViewAndInit e = itr.next();
			array[--i] =
					new ViewRef(e.init, e.vc.getView().getShortViewName(), e.vc.getView().getLongViewName());
		}
		return array;
	}

	/**
	 * Provides an array of {@link ViewRef}s representing the current "view
	 * path". The view path is a list of visited views beginning from the
	 * initially viewed view ending one before the current pinned view with a
	 * subset of visited views in between. Determining the views in between the
	 * initial and current view follows this logic:
	 * <ol>
	 * <li>TODO
	 * </ol>
	 * @return Array of {@link ViewRef}s representing the current view path.
	 */
	public ViewRef[] getViewPath() {
		assert initialView != null;

		if(cache.primarySize() < 2) return null;

		final List<ViewRef> list = new ArrayList<ViewRef>(cache.getPrimaryCapacity());

		// spew out primary view cache
		for(final Iterator<ViewAndInit> itr = cache.primaryIterator(); itr.hasNext();) {
			final ViewAndInit e = itr.next();
			if(e != initialView && e.vc != currentViewContainer && !e.vc.isPopped()) {
				if(list.size() == cache.getSecondaryCapacity()) break;
				list.add(0, new ViewRef(e.init, e.vc.getView().getShortViewName(), e.vc.getView()
						.getLongViewName()));
			}
		}

		// fill up to max allowed view path elements with secondary cache entries
		if(list.size() < cache.getSecondaryCapacity()) {
			final Iterator<ViewRef> itr = cache.secondaryIterator();
			if(itr != null) {
				while(itr.hasNext() && list.size() <= cache.getSecondaryCapacity()) {
					final ViewRef ref = itr.next();
					if(!ref.getViewInitializer().getViewKey().equals(currentViewContainer.getViewKey())) {
						list.add(0, ref);
					}
				}
			}
		}

		// ensure initial view is present
		/*
		if(currentViewContainer != initialView) {
			boolean exists = false;
			for(final Iterator<ViewRef> itr = list.iterator(); itr.hasNext();) {
				final ViewRef ref = itr.next();
				if(ref.getViewKey().equals(initialView.getViewKey())) {
					exists = true;
					break;
				}
			}
			if(!exists) {
				list.add(0, initialView.getView());
				if(list.size() > MAX_VIEW_PATH_ELEMENTS) {
					list.remove(list.size() - 1);
				}
			}
		}
		*/
		return list.toArray(new ViewRef[list.size()]);
	}

	/**
	 * Pops the currently visible pinned view out of the natural flow of the DOM
	 * document routing the {@link ViewKey} of the view that is to be the
	 * subsequent pinned view through the browser history system.
	 */
	void popCurrentView() {
		if(currentViewContainer != null) {
			assert !currentViewContainer.isPopped();

			// pop the view
			currentViewContainer.pop(parentViewPanel);

			final ViewContainer nextCurrent = findFirstView(false, currentViewContainer.getViewKey());
			final ViewKey vk = nextCurrent == null ? null : nextCurrent.getViewKey();
			if(vk != null) {
				History.newItem(generateViewKeyHistoryToken(vk));
			}
		}
	}

	/**
	 * Pins a popped view.
	 * @param key the view key of the popped view to pin
	 */
	void pinPoppedView(ViewKey key) {
		final ViewAndInit e = findView(key);
		if(e == null) throw new IllegalStateException();
		if(e.vc.isPopped()) {
			setCurrentView(e, false);
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
			if(request.addHistory()) {
				// history routing required
				assert request.getViewKey() != null : "Unable to add history: No view key specified.";
				final int hash = extractViewKeyHash(History.getToken());
				if(hash != -1 && request.getViewKey().hashCode() == hash) {
					doDispatch(request);
				}
				else {
					// need to route through history first
					this.pendingViewRequest = request;
					final String htoken = generateViewKeyHistoryToken(request.getViewKey());
					Log.debug("Routing view '" + request.getViewKey() + "' through history with hash: " + htoken);
					History.newItem(htoken);
				}
			}
			else {
				// no history routing required
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
		throw new IllegalStateException("Unhandled view request: " + request);
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		final int viewKeyHash = extractViewKeyHash(event.getValue());
		if(viewKeyHash != -1) {
			Log.debug("Handling view history token: " + viewKeyHash + "..");
			if(pendingViewRequest != null) {
				// dispatch the view request
				dispatch(pendingViewRequest);
			}
			else {
				// user pressed the back button or a non-show type view request was invoked or equivalant
				ViewAndInit e = findView(viewKeyHash);
				if(e == null) {
					// probably the user is clicking the back button a number of times beyond the cache capacity
					// resort to the first shown view
					e = initialView;
				}
				if(e != null) {
					setCurrentView(e, false);
				}
				else {
					Log.debug("Un-resolvable view hash: " + viewKeyHash + ". No action performed.");
				}
			}
		}
	}
}
