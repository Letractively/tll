/**
 * The Logic Lab
 * @author jpk
 * @since Jan 25, 2010
 */
package com.tll.model.test;

import java.util.HashMap;
import java.util.Map;

import com.tll.model.AbstractEntityFactory;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;

/**
 * TestEntityFactory - Simple {@link IEntityFactory} generating {@link Long} primary keys.
 * @author jpk
 */
public class TestEntityFactory extends AbstractEntityFactory<Long> {

	private static final Map<Class<?>, Long> idMap = new HashMap<Class<?>, Long>();

	@Override
	public <E extends IEntity> E createEntity(Class<E> entityClass, boolean generate) throws IllegalStateException {
		return newEntity(entityClass);
	}

	@Override
	public synchronized Long generatePrimaryKey(IEntity entity) {
		Long nextId = idMap.get(entity.rootEntityClass());
		if(nextId == null) {
			nextId = Long.valueOf(0);
		}
		nextId = Long.valueOf(nextId.longValue()+1);
		idMap.put(entity.rootEntityClass(), nextId);
		// this is a complete primary key
		entity.setGenerated(nextId);
		return nextId;
	}
	
	@Override
	public String primaryKeyToString(Long pk) {
		return pk == null ? null : pk.toString();
	}

	@Override
	public Long stringToPrimaryKey(String s) {
		return s == null ? null : Long.valueOf(s);
	}

}
