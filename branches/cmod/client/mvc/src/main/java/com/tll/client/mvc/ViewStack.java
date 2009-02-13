package com.tll.client.mvc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewRef;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.ui.view.ViewContainer;

/**
 * ViewStack - Manages two lists:
 * <ul>
 * <li>Distinct LIFO collection of cached {@link ViewContainer}s limited by a
 * specified capacity.</li>
 * <li>Distinct list of {@link IViewRef}s representing <em>all</em> visited
 * views relative to the app's life-cycle in chronological order.</li>
 * @author jpk
 */
final class ViewStack {

	/**
	 * The maximum number of elements to hold in the cache.
	 */
	private final int capacity;

	/**
	 * LIFO list of {@link ViewContainer}s representing the view cache. This list
	 * effectively serves as the first level cache as the entire view is retained.
	 */
	private final List<ViewContainer> cache;

	/**
	 * Distinct list of {@link IViewRef}s representing all visited views relative
	 * to the app's life-cycle in chronological order <em>EXCLUDING</em> those
	 * whose associated views are present in the {@link #cache} list.
	 * <em>This</em> list effectively serves as the second-level cache enabling
	 * the ability to "re-constitute" a view at any time during the app's loaded
	 * life-cycle.
	 */
	private final List<IViewRef> visited = new ArrayList<IViewRef>();

	/**
	 * Constructor
	 * @param capacity The cache capacity
	 */
	public ViewStack(int capacity) {
		this.capacity = capacity;
		cache = new ArrayList<ViewContainer>(capacity);
	}

	/**
	 * @return The cache size
	 */
	public int cacheSize() {
		return cache.size();
	}

	/**
	 * Empties the cache
	 */
	public void emptyCache() {
		cache.clear();
	}

	/**
	 * @return The visited list size
	 */
	public int visitedSize() {
		return visited.size();
	}

	/**
	 * Empties the visited list
	 */
	public void emptyVisited() {
		visited.clear();
	}

	/**
	 * @param viewKey The viewContainer ref
	 * @return The index of the viewContainer on the cache or -1 if not present.
	 */
	public int searchCache(ViewKey viewKey) {
		final Iterator<ViewContainer> itr = cacheIterator(0);
		if(itr != null) {
			int i = 0;
			while(itr.hasNext()) {
				final ViewContainer cv = itr.next();
				if(viewKey.equals(cv.getView().getViewKey())) return i;
				i++;
			}
		}
		return -1;
	}

	/**
	 * Calculates the index of the associated view ref in the visisted list for a
	 * given ViewContainer.
	 * @param vc The ViewContainer
	 * @return The index or <code>-1</code> if no associated view ref exists in
	 *         the visited list.
	 */
	int searchVisited(ViewKey key) {
		int i = 0;
		for(final IViewRef vref : visited) {
			final ViewKey vkey = vref.getViewKey();
			if(vkey.equals(key)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	/**
	 * Adds the viewContainer to the cache returning the viewContainer removed
	 * from the cache in the event the cache was at capacity at the time this
	 * method is called.
	 * @param viewContainer The viewContainer to add to the cache
	 * @return The "expired" {@link ViewContainer} when the cache is at capacity
	 *         or <code>null</code> if the cache is not yet at capacity.
	 */
	public ViewContainer push(ViewContainer viewContainer) {
		assert viewContainer != null;
		final IView view = viewContainer.getView();
		assert view != null;
		final ViewKey key = view.getViewKey();
		assert key != null;

		final int cacheIndex = searchCache(key);
		final int visitedIndex = searchVisited(key);

		// add to primary cache removing from secondary cache if present
		if(cacheIndex >= 0) {
			cache.remove(cacheIndex);
		}
		cache.add(0, viewContainer);
		if(visitedIndex >= 0) {
			visited.remove(visitedIndex);
		}

		ViewContainer expired = null;

		// ensure capacity
		if(cache.size() > capacity) {
			expired = cache.remove(cache.size() - 1);
			// demote expired to the 2nd-level cache
			visited.add(0, expired.getView().getViewRequest());
		}

		return expired;
	}

	/**
	 * Moves a cached {@link ViewContainer}'s position in the cache list to the
	 * last position thus making it the "oldest" entry.
	 * @param viewContainer The element moved to the last list position
	 */
	public void moveToLast(ViewContainer viewContainer) {
		assert viewContainer != null;
		final int index = cache.indexOf(viewContainer);
		assert index >= 0;
		cache.remove(index);
		cache.add(viewContainer);
	}

	/**
	 * Insert a view container in the cache at the given index.
	 * @param index
	 * @param viewContainer
	 */
	public void add(int index, ViewContainer viewContainer) {
		cache.add(index, viewContainer);
	}

	/**
	 * Remove a view container from the cache at the given index.
	 * @param index
	 * @return The removed element
	 */
	public ViewContainer remove(int index) {
		return cache.remove(index);
	}

	/**
	 * @param startIndex The index iterating begins on.
	 * @return Iterator from newest to oldest of the cached views beginning at the
	 *         given starting index or <code>null</code> if the
	 *         <code>startIndex</code> equals or exceeds the cache size.
	 */
	public Iterator<ViewContainer> cacheIterator(int startIndex) {
		assert startIndex >= 0;
		return cache.size() == 0 || cache.size() < startIndex + 1 ? null : cache.listIterator(startIndex);
	}

	/**
	 * @param startIndex The index iterating begins on.
	 * @return Iterator of the visited views beginning at the given starting index
	 *         or <code>null</code> if the <code>startIndex</code> equals or
	 *         exceeds the number of currently visited views.
	 */
	public Iterator<IViewRef> visitedIterator(int startIndex) {
		assert startIndex >= 0;
		return visited.size() == 0 || visited.size() < startIndex + 1 ? null : visited.listIterator(startIndex);
	}
}