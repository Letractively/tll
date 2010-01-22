/*
 * The Logic Lab
 */
package com.tll.model.key;

import java.util.HashMap;
import java.util.Map;

import com.tll.model.EntityUtil;

/**
 * SimplePrimaryKeyGenerator - In memory map of integers classified by entity
 * class that are incremented as primary keys are requested.
 * @author jpk
 */
public class SimplePrimaryKeyGenerator implements IPrimaryKeyGenerator<PrimaryKey> {

	private static final Map<Class<?>, Long> idMap = new HashMap<Class<?>, Long>();

	public synchronized PrimaryKey generateIdentifier(Class<?> entityType) {
		final Class<?> rootEntityClass = EntityUtil.getRootEntityClass(entityType);
		Long nextId = idMap.get(rootEntityClass);
		if(nextId == null) {
			nextId = Long.valueOf(0);
		}
		nextId = Long.valueOf(nextId.longValue()+1);
		idMap.put(rootEntityClass, nextId);
		return new PrimaryKey(entityType, nextId);
	}
}
