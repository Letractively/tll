package com.tll.service.acl.afterinvocation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * The <code>Map</code> filterer. Thin wrapper around the already established
 * collection filterer A {@link com.tll.service.acl.afterinvocation.IMapFiltererTransformer} converts a
 * serviceMap/collection to/from a collection/serviceMap so the wrapped
 * collection based filterer can perform the actual filtering.
 * @author jpk
 */
public class MapFilterer implements IFilterer {

	/**
	 * The wrapped collection-based filterer. This is the filterer that performs
	 * the actual filtering.
	 */
	private CollectionFilterer collectionFilterer;

	/**
	 * The transformer that performs the conversion to/from the
	 * collection/serviceMap before and after filtering.
	 */
	private IMapFiltererTransformer transformer;

	/**
	 * Constructor
	 */
	public MapFilterer(Map<Object, Object> map, IMapFiltererTransformer transformer) {
		super();
		init(map, transformer);
	}

	protected void init(Map<Object, Object> map, IMapFiltererTransformer transformer) {
		this.collectionFilterer = new CollectionFilterer(transformer.toCollection(map));
		this.transformer = transformer;
	}

	@SuppressWarnings("unchecked")
	public Object getFilteredObject() {
		return transformer.toMap((Collection) collectionFilterer.getFilteredObject());
	}

	public Iterator<Object> iterator() {
		return collectionFilterer.iterator();
	}

	public void remove(Object object) {
		collectionFilterer.remove(object);
	}

}
