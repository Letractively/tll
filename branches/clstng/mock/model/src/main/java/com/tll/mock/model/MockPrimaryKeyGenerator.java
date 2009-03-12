/*
 * The Logic Lab 
 */
package com.tll.mock.model;

import java.util.HashMap;
import java.util.Map;

import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * MockPrimaryKeyGenerator
 * @author jpk
 */
public class MockPrimaryKeyGenerator implements IPrimaryKeyGenerator {

	private static final Map<Class<? extends IEntity>, Integer> idMap = new HashMap<Class<? extends IEntity>, Integer>();
	
	public synchronized Integer generateIdentifier(Class<? extends IEntity> entityClass) {
		Class<? extends IEntity> rootEntityClass = EntityUtil.getRootEntityClass(entityClass);
		Integer nextId = idMap.get(rootEntityClass);
		if(nextId == null) {
			nextId = new Integer(0);
		}
		idMap.put(rootEntityClass, ++nextId);
		return nextId;
  }
}
