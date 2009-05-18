/*
 * The Logic Lab
 */
package com.tll.model.key;

import java.util.HashMap;
import java.util.Map;

import com.tll.model.EntityUtil;
import com.tll.model.IEntity;

/**
 * MockPrimaryKeyGenerator
 * @author jpk
 */
public class MockPrimaryKeyGenerator implements IPrimaryKeyGenerator {

	private static final Map<Class<? extends IEntity>, Integer> idMap = new HashMap<Class<? extends IEntity>, Integer>();

	public synchronized Integer generateIdentifier(Class<? extends IEntity> entityClass) {
		final Class<? extends IEntity> rootEntityClass = EntityUtil.getRootEntityClass(entityClass);
		Integer nextId = idMap.get(rootEntityClass);
		if(nextId == null) {
			nextId = Integer.valueOf(0);
		}
		idMap.put(rootEntityClass, ++nextId);
		return nextId;
	}
}
