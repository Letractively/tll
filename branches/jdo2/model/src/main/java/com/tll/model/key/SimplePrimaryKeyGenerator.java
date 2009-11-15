/*
 * The Logic Lab
 */
package com.tll.model.key;

import java.util.HashMap;
import java.util.Map;

import com.tll.model.EntityUtil;
import com.tll.model.IEntity;

/**
 * SimplePrimaryKeyGenerator - In memory map of integers classified by entity
 * class that are incremented as primary keys are requested.
 * @author jpk
 */
public class SimplePrimaryKeyGenerator implements IPrimaryKeyGenerator {

	private static final Map<Class<? extends IEntity>, Long> idMap = new HashMap<Class<? extends IEntity>, Long>();

	public synchronized long generateIdentifier(Class<? extends IEntity> entityClass) {
		final Class<? extends IEntity> rootEntityClass = EntityUtil.getRootEntityClass(entityClass);
		Long nextId = idMap.get(rootEntityClass);
		if(nextId == null) {
			nextId = Long.valueOf(0);
		}
		nextId = Long.valueOf(nextId.longValue()+1);
		idMap.put(rootEntityClass, nextId);
		return nextId.longValue();
	}
}
