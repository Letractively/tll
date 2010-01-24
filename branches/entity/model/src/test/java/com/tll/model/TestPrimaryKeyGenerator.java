/*
 * The Logic Lab
 */
package com.tll.model;

import java.util.HashMap;
import java.util.Map;


/**
 * TestPrimaryKeyGenerator - In memory map of integers classified by entity
 * class that are incremented as primary keys are requested.
 * @author jpk
 */
public class TestPrimaryKeyGenerator implements IPrimaryKeyGenerator<Long> {

	private static final Map<Class<?>, Long> idMap = new HashMap<Class<?>, Long>();

	public synchronized Long generateIdentifier(IEntity entity) {
		final Class<?> rootEntityClass = EntityUtil.getRootEntityClass(entity.entityClass());
		Long nextId = idMap.get(rootEntityClass);
		if(nextId == null) {
			nextId = Long.valueOf(0);
		}
		nextId = Long.valueOf(nextId.longValue()+1);
		idMap.put(rootEntityClass, nextId);
		//entity.setPrimaryKey(nextId);
		return nextId;
	}
}
