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
import com.tll.client.mvc.view.IHasViewChangeHandlers;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewChangeHandler;
import com.tll.client.mvc.view.IViewInitializer;
import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.ViewChangeEvent;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.mvc.view.ViewOptions;
import com.tll.client.mvc.view.ViewRef;
import com.tll.client.ui.UI;
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
	 * Creates a {@link ViewRef} given a {@link ViewAndInit}.
	 * @param e the view
	 * @return Newly created {@link ViewRef}.
	 */
	private static ViewRef ref(ViewAndInit e) {
		return new ViewRef(e.init, e.options, e.vc.getView().getShortViewName(), e.vc.getView().getLongViewName());
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
	 * The view cache.
	 */
	private final ViewCache cache;

	/**
	 * The first and currently pinned view.
	 */
	private ViewAndInit initial, current;

	/**
	 * The controllers to handle view requests.
	 */
	private final List<IController> controllers = new ArrayList<IController>();

	/**
	 * The collection of view change listeners that are notified when the current
	 * view changes.
	 */
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
	 * @param cacheCapacity
	 * @see ViewManager#initialize(Panel, int)
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
	 * @param init The view initializer employed only when the view is not present
	 *        in the view cache
	 * @param options The view options employed only when the view is not present
	 *        in the view cache
	 */
	@SuppressWarnings("unchecked")
	void setCurrentView(IViewInitializer init, ViewOptions options) {
		final ViewKey key = init.getViewKey();
		Log.debug("Setting current view: '" + key + "' ..");

		ViewAndInit e;

		final int cacheIndex = cache.searchQueue(key);

		if(cacheIndex != -1) {
			// existing cached view
			e = cache.removeAt(cacheIndex);
			assert e != null;
		}
		else {
			UI.busy();
			Log.debug("Creating and initializing view: " + key + " ..");
			// non-cached view
			final IView<IViewInitializer> view = (IView<IViewInitializer>) key.getViewClass().newView();
			// initialize the view
			view.initialize(init);
			// load the view
			view.refresh();

			e = new ViewAndInit(new ViewContainer(view, options, key), init, options);
		}

		// NOTE: only show as popped if not currently in cache
		setCurrentView(e, ((cacheIndex == -1) && (options != null && options.isInitiallyPopped())));
	}

	/**
	 * Sets the current view given a presently cached view container.
	 * @param e primary cache element
	 * @param showPopped show popped or pinned?
	 */
	private void setCurrentView(ViewAndInit e, boolean showPopped) {
		final ViewContainer vc = e.vc;

		// set the view
		if(showPopped) {
			// NOTE: the view history is not affected!
			vc.pop(parentViewPanel);
		}
		else {
			final boolean sameView = (current != null && current.equals(e));
			final boolean pndgIsPopped = e.vc.isPopped();
			final boolean crntIsPopped = current != null && current.vc.isPopped();

			final boolean rmvCrnt = current != null && ((!sameView && !crntIsPopped) || (sameView && pndgIsPopped));
			final boolean pinPndg = (rmvCrnt || (sameView && crntIsPopped) || !sameView || !e.vc.isAttached());

			if(rmvCrnt) {
				// remove current pinned view
				current.vc.removeFromParent();
			}
			if(pinPndg) {
				// pin the view and set as current
				vc.pin(parentViewPanel);
			}
			current = e;
		}

		// add the view to the cache
		ViewAndInit old = cache.cache(e);
		if(old != null) {
			assert old != e && !old.getViewKey().equals(e.getViewKey());
			Log.debug("Destroying view - " + old.vc.getView().toString() + "..");
			// view life-cycle destroy
			old.vc.getView().onDestroy();
			old = null;
		}

		// set the initial view if not set
		if(initial == null) initial = e;

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
				UI.unbusy();
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

		// find the newest pinned view excluding the one to be unloaded
		ViewAndInit pendingCurrent = findFirstView(e);
		if(pendingCurrent == null && e != initial) {
			pendingCurrent = initial;
		}
		if(pendingCurrent != null) {
			History.newItem(generateViewKeyHistoryToken(pendingCurrent.getViewKey()));
		}
	}

	/**
	 * Removes all view artifacts from the DOM and clears the cache.
	 */
	public void clear() {
		Log.debug("Clearing view cache..");
		if(cache.size() > 0) {
			for(final Iterator<ViewAndInit> itr = cache.queueIterator(); itr.hasNext();) {
				final ViewAndInit e = itr.next();
				e.vc.close();
				e.vc.getView().onDestroy();
			}
		}
		cache.clear();
		Log.debug("View cache cleared");
	}

	/**
	 * Generic find view method returning the first found match in the view cache.
	 * @param exclude The view to exclude from the search. May be
	 *        <code>null</code>.
	 * @return The first found view or <code>null</code> if no match found.
	 */
	private ViewAndInit findFirstView(ViewAndInit exclude) {
		final Iterator<ViewAndInit> itr = cache.queueIterator();
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewAndInit e = itr.next();
				if(exclude == null || exclude != e) {
					return e;
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
		final Iterator<ViewAndInit> itr = cache.queueIterator();
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewAndInit e = itr.next();
				if(e.getViewKey().equals(key)) {
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
		final Iterator<ViewAndInit> itr = cache.queueIterator();
		if(itr != null) {
			while(itr.hasNext()) {
				final ViewAndInit e = itr.next();
				final int hc = e.getViewKey().hashCode();
				if(hc == viewKeyHash) {
					return e;
				}
			}
		}
		return null;
	}

	/**
	 * Resolves a view key hash to the associated view ref held in the view cache.
	 * <p>
	 * <b>NOTE:</b> we could throw an exception here since we are retaining view
	 * refs for *all* visited views and, therefore, we expect to be able to
	 * resolve any view key hash to a view ref but we account for possibility the
	 * user may have manually changed the view key hash in the query string or
	 * equivalent.
	 * @param viewKeyHash
	 * @return The found view ref or <code>null</code> if not found.
	 */
	private ViewRef findViewRef(int viewKeyHash) {
		// try visited view ref cache
		final Iterator<ViewRef> vitr = cache.visitedRefIterator();
		if(vitr != null) {
			while(vitr.hasNext()) {
				final ViewRef r = vitr.next();
				final int hc = r.getViewKey().hashCode();
				if(hc == viewKeyHash) {
					return r;
				}
			}
		}
		return null;
	}

	/**
	 * @return Never <code>null</code> array of the currently cached views from
	 *         most recently visited (head) to oldest (tail) which may be empty
	 *         indicating there are currently no cached views.
	 */
	public IView<?>[] getCachedViews() {
		if(cache.size() == 0) {
			return new IView[] {};
		}
		final ArrayList<IView<?>> list = new ArrayList<IView<?>>(cache.size());
		for(final Iterator<ViewAndInit> itr = cache.queueIterator(); itr.hasNext();) {
			list.add(itr.next().vc.getView());
		}
		return list.toArray(new IView[list.size()]);
	}

	/**
	 * Provides an array of the cached views as stand-alone references in "cache"
	 * order (head is newest).
	 * @param capacity the maximum number of view refs to provide in the returned
	 *        array. <code>-1</code> indicates un-bounded in which case the
	 *        capacity is that of the number of distinct views visited.
	 * @param includePopped Included views that are currently popped?
	 * @param includeFirst Include the first cached view? <br>
	 *        NOTE: This view is <em>always</em> retained.
	 * @return A newly created never <code>null</code> array of view references.
	 */
	public ViewRef[] getViewRefs(int capacity, boolean includePopped, boolean includeFirst) {

		if(initial == null) return new ViewRef[0];

		if(capacity == -1) capacity = cache.numVisited();

		int count = 0;

		final ArrayList<ViewRef> plist = new ArrayList<ViewRef>();

		final Iterator<ViewRef> ritr = cache.visitedRefIterator();
		if(ritr != null) {
			while(ritr.hasNext() && count < capacity) {
				ViewRef r = ritr.next();
				if(!includePopped) {
					final ViewAndInit e = cache.peekQueue(r.getViewKey());
					if(e != null && e.vc.isPopped()) {
						r = null;
					}
				}
				if(r != null) {
					plist.add(r);
					count++;
				}
			}
		}

		// include the initial view if called for and not already in list
		if(includeFirst && initial != null) {
			// verify not already present
			int initialIndex = -1;
			for(int i = 0; i < plist.size(); i++) {
				if(initial.compareTo(plist.get(i)) == 0) {
					initialIndex = i;
					break;
				}
			}
			if(initialIndex == -1) {
				plist.add(ref(initial));
				++count;
			}
		}

		// trim plist if necessary
		int psize = plist.size();
		while(psize > capacity) {
			plist.remove(--psize);
		}

		return plist.toArray(new ViewRef[plist.size()]);
	}

	/**
	 * Pops the currently visible pinned view out of the natural flow of the DOM
	 * document routing the {@link ViewKey} of the view that is to be the
	 * subsequent pinned view through the browser history system.
	 */
	void popCurrentView() {
		if(current != null) {
			assert !current.vc.isPopped();

			// pop the view
			current.vc.pop(parentViewPanel);

			final ViewAndInit nextCurrent = findFirstView(current);
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
	 * Dispatches the view request event to the appropriate controller.
	 * @param request The view request
	 */
	public void dispatch(IViewRequest request) {
		if(request == null) throw new IllegalArgumentException("No view request specified.");

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
					// resort to the visited view ref cache
					final ViewRef r = findViewRef(viewKeyHash);
					if(r == null) {
						Log.warn("Un-resolved view hash: " + viewKeyHash);
					}
					else {
						setCurrentView(r.getViewInitializer(), r.getOptions());
						return;
					}
					// this should only happen when the user mucks with the view key hash in the query string
					// resort to the initial view
					e = initial;
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
