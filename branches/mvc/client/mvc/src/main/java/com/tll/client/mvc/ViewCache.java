package com.tll.client.mvc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tll.client.mvc.view.IViewKey;
import com.tll.client.mvc.view.IViewRef;
import com.tll.client.mvc.view.ViewRef;
import com.tll.client.ui.view.ViewContainer;

/**
 * ViewCache - Manages two lists:
 * <ul>
 * <li>Distinct LIFO collection of cached {@link ViewContainer}s limited by a
 * specified capacity.</li>
 * <li>Distinct list of {@link IViewRef}s representing <em>all</em> visited
 * views relative to the app's life-cycle in chronological order.</li>
 * @author jpk
 */
final class ViewCache {

	/**
	 * The maximum number of elements to hold in the cache.
	 */
	private final int capacity;

	/**
	 * LIFO list of {@link ViewContainer}s keyed by their corres. view keys. This
	 * ordered map is the "primary" cache.
	 */
	private final ArrayList<ViewContainer> cache;

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
	ViewCache(int capacity) {
		this.capacity = capacity;
		cache = new ArrayList<ViewContainer>(capacity);
	}

	/**
	 * Sets the given cache element returning the "expired" cache element if the
	 * cache is at capacity prior to calling this method or <code>null</code> if
	 * the cache was not at capacity.
	 * @param viewContainer the element to cache
	 * @return The removed {@link ViewContainer} or <code>null</code> if the cache
	 *         is not yet at capacity.
	 */
	ViewContainer set(ViewContainer viewContainer) {
		assert viewContainer != null;
		final IViewKey key = viewContainer.getViewKey();
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
			visited.add(0, new ViewRef(expired.getViewKey(), expired.getView().getShortViewName(), expired.getView()
					.getShortViewName()));
		}
	
		return expired;
	}

	/**
	 * Remove a primary cache element.
	 * @param viewKey the key of the element to remove
	 * @return The removed element.
	 */
	ViewContainer remove(IViewKey viewKey) {
		return removeAt(searchCache(viewKey));
	}

	/**
	 * Removes a primary cache element at the given index.
	 * @param index the index at which to remove the cache element
	 * @return The removed element.
	 */
	ViewContainer removeAt(int index) {
		return cache.remove(index);
	}

	/**
	 * Moves a cached {@link ViewContainer}'s position in the cache list to the
	 * last position thus making it the "oldest" entry.
	 * @param viewContainer The element moved to the last list position
	 */
	void moveToLast(ViewContainer viewContainer) {
		assert viewContainer != null;
		final int index = cache.indexOf(viewContainer);
		assert index >= 0;
		cache.remove(index);
		cache.add(viewContainer);
	}

	/**
	 * @return The number of cached views.
	 */
	int cacheSize() {
		return cache.size();
	}

	/**
	 * Empties the cache.
	 */
	void emptyCache() {
		cache.clear();
	}

	/**
	 * @param viewKey The viewContainer ref
	 * @return The index of the viewContainer on the cache or -1 if not present.
	 */
	int searchCache(IViewKey viewKey) {
		for(int i = 0; i < cache.size(); i++) {
			if(viewKey.equals(cache.get(i).getViewKey())) return i;
		}
		return -1;
	}

	/**
	 * Returns the index of the associated view ref in the visisted list for a
	 * given view key.
	 * @param key The view key
	 * @return The index or <code>-1</code> if no associated view ref exists in
	 *         the visited list.
	 */
	private int searchVisited(IViewKey key) {
		for(int i = 0; i < visited.size(); i++) {
			if(visited.get(i).getViewKey().equals(key)) return i;
		}
		return -1;
	}

	/**
	 * @return Iterator from newest to oldest of the cached views beginning at the
	 *         given starting index or <code>null</code> if the
	 *         <code>startIndex</code> equals or exceeds the cache size.
	 */
	Iterator<ViewContainer> cacheIterator() {
		return cache.size() == 0 ? null : cache.listIterator(0);
	}

	/**
	 * @return Iterator of the visited views beginning at the given starting index
	 *         or <code>null</code> if the <code>startIndex</code> equals or
	 *         exceeds the number of currently visited views.
	 */
	Iterator<IViewRef> visitedIterator() {
		return visited.size() == 0 ? null : visited.listIterator(0);
	}
}