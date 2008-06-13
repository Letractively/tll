package com.tll.service.acl.afterinvocation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A filter used to filter Collections.
 */
class CollectionFilterer implements IFilterer {

	protected static final Log logger = LogFactory.getLog(CollectionFilterer.class);

	// ~ Instance fields
	// ================================================================================================

	private final Collection<Object> collection;

	// collectionIter offers significant performance optimisations (as
	// per acegisecurity-developer mailing list conversation 19/5/05)
	private Iterator<Object> collectionIter;
	private final Set<Object> removeList;

	// ~ Constructors
	// ===================================================================================================

	CollectionFilterer(Collection<Object> collection) {
		this.collection = collection;

		// We create a Set of objects to be removed from the Collection,
		// as ConcurrentModificationException prevents removal during
		// iteration, and making a new Collection to be returned is
		// problematic as the original Collection implementation passed
		// to the method may not necessarily be re-constructable (as
		// the Collection(collection) constructor is not guaranteed and
		// manually adding may lose sort order or other capabilities)
		removeList = new HashSet<Object>();
	}

	// ~ Methods
	// ========================================================================================================

	public Object getFilteredObject() {
		// Now the Iterator has ended, remove Objects from Collection
		Iterator<Object> removeIter = removeList.iterator();

		int originalSize = collection.size();

		while(removeIter.hasNext()) {
			collection.remove(removeIter.next());
		}

		if(logger.isDebugEnabled()) {
			logger.debug("Original collection contained " + originalSize + " elements; now contains " + collection.size()
					+ " elements");
		}

		return collection;
	}

	public Iterator<Object> iterator() {
		collectionIter = collection.iterator();

		return collectionIter;
	}

	public void remove(Object object) {
		collectionIter.remove();
	}
}
