package com.tll.service.acl.afterinvocation;

import java.util.Collection;
import java.util.Map;

/**
 * Transforms <code>Map</code> objects to <code>Collection</code> objects
 * and visa-versa.
 * @author jpk
 */
public interface IMapFiltererTransformer {

	/**
	 * Is the serviceMap transformable by this transformer?
	 * @param serviceMap the serviceMap for which applicability is determined
	 * @return true if applicable, false otherwise
	 */
	boolean isApplicable(Map<Object, Object> map);

	/**
	 * Transforms the given serviceMap to a collection.
	 * @param serviceMap
	 */
	Collection<Object> toCollection(Map<Object, Object> map);

	/**
	 * Transforms the given collection to a serviceMap.
	 * @param clc the collection
	 */
	Map<Object, Object> toMap(Collection<Object> clc);
}
