/**
 * The Logic Lab
 * @author jpk Dec 25, 2007
 */
package com.tll.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * EntityCache - Generic holder of an arbitrary set of entities. Used, in
 * particular, in the generic assembly of entities.
 * @author jpk
 */
public class EntityCache implements IEntityProvider {

	private final Map<Object, IEntity> map = new HashMap<Object, IEntity>();

	/**
	 * Constructor
	 */
	public EntityCache() {
		super();
	}

	/**
	 * Constructor
	 * @param entities
	 */
	public EntityCache(IEntity... entities) {
		super();
		addEntities(entities);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E getEntity(Class<E> entityType, Object key) {
		return (E) map.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> Set<E> getEntitiesByType(Class<E> type) {
		if(type == null) return null;
		HashSet<E> list = new HashSet<E>();
		for(IEntity e : map.values()) {
			if(type.isAssignableFrom(e.entityClass())) {
				list.add((E) e);
			}
		}
		return list;
	}

	@Override
	public <E> E getEntityByType(Class<E> type) throws IllegalStateException {
		if(type == null) return null;
		Collection<? extends E> clc = getEntitiesByType(type);
		if(clc.size() > 1) {
			throw new IllegalStateException("More than one entity of the type: " + type.getSimpleName() + " exists.");
		}
		else if(clc.size() == 1) {
			return clc.iterator().next();
		}
		return null;
	}

	public void addEntity(IEntity e) {
		if(e != null) map.put(e.getId(), e);
	}

	public void addEntities(Collection<IEntity> entities) {
		for(IEntity e : entities) {
			addEntity(e);
		}
	}

	public void addEntities(IEntity[] entities) {
		for(IEntity e : entities) {
			addEntity(e);
		}
	}
}
