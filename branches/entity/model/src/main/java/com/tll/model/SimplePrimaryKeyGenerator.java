/*
 * The Logic Lab
 */
package com.tll.model;

import java.util.HashMap;
import java.util.Map;


/**
 * SimplePrimaryKeyGenerator - In memory map of integers classified by entity
 * class that are incremented as primary keys are requested.
 * @author jpk
 */
public class SimplePrimaryKeyGenerator implements IPrimaryKeyGenerator<GlobalLongPrimaryKey> {

	private static final Map<Class<?>, Long> idMap = new HashMap<Class<?>, Long>();

	public synchronized GlobalLongPrimaryKey generateIdentifier(Class<?> entityType) {
		final Class<?> rootEntityClass = EntityUtil.getRootEntityClass(entityType);
		Long nextId = idMap.get(rootEntityClass);
		if(nextId == null) {
			nextId = Long.valueOf(0);
		}
		nextId = Long.valueOf(nextId.longValue()+1);
		idMap.put(rootEntityClass, nextId);
		return new GlobalLongPrimaryKey(entityType, nextId);
	}
}
