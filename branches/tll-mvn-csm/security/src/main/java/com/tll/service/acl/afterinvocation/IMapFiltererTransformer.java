package com.tll.service.acl.afterinvocation;

import java.util.Collection;
import java.util.Map;

/**
 * Transforms <code>Map</code> objects to <code>Collection</code> objects and
 * visa-versa.
 * @author jpk
 */
public interface IMapFiltererTransformer {

	/**
	 * Is the map transformable by this transformer?
	 * @param map the map for which applicability is determined
	 * @return true if applicable, false otherwise
	 */
	boolean isApplicable(Map<Object, Object> map);

	/**
	 * Transforms the given map to a collection.
	 * @param map
	 * @return transformed collection
	 */
	Collection<Object> toCollection(Map<Object, Object> map);

	/**
	 * Transforms the given collection to a map.
	 * @param clc the collection
	 * @return transformed map
	 */
	Map<Object, Object> toMap(Collection<Object> clc);
}
