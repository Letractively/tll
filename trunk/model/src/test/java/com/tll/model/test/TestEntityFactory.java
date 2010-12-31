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
 * TestEntityFactory - Simple {@link IEntityFactory} generating {@link Long}
 * primary keys <em>w/o retaining state of the last provided id between jvm
 * sessions</em>.
 * @author jpk
 */
public class TestEntityFactory extends AbstractEntityFactory {

	private static final Map<Class<?>, Long> idMap = new HashMap<Class<?>, Long>();

	@Override
	public boolean isPrimaryKeyGeneratable() {
		return true;
	}

	@Override
	public synchronized Long generatePrimaryKey(IEntity entity) {
		Long nextId = idMap.get(entity.rootEntityClass());
		if(nextId == null) {
			nextId = Long.valueOf(0);
		}
		nextId = Long.valueOf(nextId.longValue() + 1);
		idMap.put(entity.rootEntityClass(), nextId);
		// this is a complete primary key
		entity.setGenerated(nextId);
		return nextId;
	}
}
