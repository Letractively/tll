package com.tll.client.mvc;

import java.util.ArrayList;
import java.util.Iterator;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.mvc.view.IViewInitializer;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.mvc.view.ViewRef;
import com.tll.client.ui.view.ViewContainer;

/**
 * ViewCache - Manages two lists:
 * <ul>
 * <li>Distinct LIFO collection of cached {@link ViewContainer}s limited by a
 * specified primaryCapacity.</li>
 * <li>Distinct list of {@link ViewRef}s representing <em>all</em> secondary
 * views relative to the app's life-cycle in chronological order.</li>
 * @author jpk
 */
final class ViewCache {

	/**
	 * ViewAndInit - Simple encapsulation of a view and its initializer.
	 * @author jpk
	 */
	static class ViewAndInit {

		ViewContainer vc;
		IViewInitializer init;

		/**
		 * Constructor
		 * @param vc
		 * @param init
		 */
		public ViewAndInit(ViewContainer vc, IViewInitializer init) {
			super();
			this.vc = vc;
			this.init = init;
		}
	}

	/**
	 * The maximum number of elements to hold in the primary cache.
	 */
	private final int primaryCapacity;

	/**
	 * The maximum number of elements to hold in the secondary primary.
	 */
	private final int secondaryCapacity;

	/**
	 * list of views in order of newest cached (head) to oldest cached (tail).
	 */
	private final ArrayList<ViewAndInit> primary;

	/**
	 * list of view refs filled when primary cache elements expire in order of
	 * newest cached (head) to oldest cached (tail).
	 * <p>
	 * This is a distinct list of view key providers as compared to the primary
	 * cache elements.
	 */
	private final ArrayList<ViewRef> secondary;

	/**
	 * Constructor
	 * @param primaryCapacity The max number of primary cache elements to store at
	 *        any given time
	 * @param secondaryCapacity The max number of secondary cache elements to
	 *        store at any given time
	 */
	ViewCache(int primaryCapacity, int secondaryCapacity) {
		this.primaryCapacity = primaryCapacity;
		this.secondaryCapacity = secondaryCapacity;
		this.primary = new ArrayList<ViewAndInit>(primaryCapacity);
		this.secondary = new ArrayList<ViewRef>(secondaryCapacity);
	}

	int getPrimaryCapacity() {
		return primaryCapacity;
	}

	int getSecondaryCapacity() {
		return secondaryCapacity;
	}

	/**
	 * Caches views.
	 * <p>
	 * Sets the given primary element returning the "expired" primary element if
	 * the primary cache is at capacity prior to calling this method or
	 * <code>null</code> if the primary cache was not at capacity. The expired
	 * primary element is added to the secondary cache before it is returned.
	 * @param viewContainer the element to cache
	 * @param init the view initializer
	 * @return The removed cache element or <code>null</code> if the primary cache
	 *         is not yet at capacity.
	 */
	ViewAndInit cache(ViewContainer viewContainer, IViewInitializer init) {
		assert viewContainer != null;
		final ViewKey key = viewContainer.getViewKey();
		assert key != null;

		// add to primary removing from secondary if present
		final int pindex = searchPrimary(key);
		if(pindex >= 0) {
			Log.debug("ViewCache.set() - removing OLD primary cache entry: " + key);
			primary.remove(pindex);
		}
		// insert at head
		primary.add(0, new ViewAndInit(viewContainer, init));

		// ensure not in secondary
		final int sindex = searchSecondary(key);
		if(sindex >= 0) {
			Log.debug("ViewCache.set() - removing secondary cache entry: " + key);
			// remove at tail
			secondary.remove(sindex);
		}

		ViewAndInit expired = null;

		// primary capacity check
		if(primary.size() > primaryCapacity) {
			expired = primary.remove(primary.size() - 1);
			// demote expired to secondary cache
			Log.debug("ViewCache.set() - demoting expired view '" + key + "' from primary to secondary cache..");
			// insert at head
			secondary.add(0, new ViewRef(expired.init, expired.vc.getView().getShortViewName(), expired.vc.getView()
					.getLongViewName()));
			// secondary capacity check
			if(secondary.size() > secondaryCapacity) {
				// remove at tail
				// NOTE: we don't need to handle any life-cycle clean up for secondary cache elements
				secondary.remove(secondary.size() - 1);
			}
		}

		return expired == null ? null : expired;
	}

	/**
	 * Remove a primary primary element.
	 * @param key the key of the element to remove
	 * @return The removed element.
	 */
	ViewAndInit remove(ViewKey key) {
		return removeAt(searchPrimary(key));
	}

	/**
	 * Removes a primary primary element at the given index.
	 * @param index the index at which to remove the primary element
	 * @return The removed element.
	 */
	ViewAndInit removeAt(int index) {
		return primary.remove(index);
	}

	/**
	 * Moves a cached {@link ViewContainer}'s position in the primary list to the
	 * last position thus making it the "oldest" entry.
	 * @param viewContainer The element moved to the last list position
	 */
	/*
	void moveToLast(ViewContainer viewContainer) {
		assert viewContainer != null;
		final int index = primary.indexOf(viewContainer);
		assert index >= 0;
		primary.remove(index);
		primary.add(viewContainer);
	}
	*/

	/**
	 * @return The number of primary cache elements.
	 */
	int primarySize() {
		return primary.size();
	}

	/**
	 * @return The number of secondary cache elements.
	 */
	int secondarySize() {
		return secondary.size();
	}

	/**
	 * Clears out all cached elements in the primary and secondary caches.
	 */
	void clear() {
		primary.clear();
		secondary.clear();
	}

	/**
	 * Searches the primary cache.
	 * @param key The view key
	 * @return The matching cache list index or <code>-1</code> if not present.
	 */
	int searchPrimary(ViewKey key) {
		for(int i = 0; i < primary.size(); i++) {
			if(key.equals(primary.get(i).vc.getViewKey())) return i;
		}
		return -1;
	}

	/**
	 * Searches the secondary cache.
	 * @param key The view key
	 * @return The matching cache list index or <code>-1</code> if not present.
	 */
	private int searchSecondary(ViewKey key) {
		for(int i = 0; i < secondary.size(); i++) {
			if(secondary.get(i).getViewInitializer().getViewKey().equals(key)) return i;
		}
		return -1;
	}

	/**
	 * @return A newly created primary cache element iterator from newest to
	 *         oldest.
	 */
	Iterator<ViewAndInit> primaryIterator() {
		return primary.size() == 0 ? null : primary.listIterator(0);
	}

	/**
	 * @return A newly created secondary cache element iterator from newest to
	 *         oldest.
	 */
	Iterator<ViewRef> secondaryIterator() {
		return secondary.size() == 0 ? null : secondary.listIterator(0);
	}
}