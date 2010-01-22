/**
 * The Logic Lab
 * @author jpk Dec 25, 2007
 */
package com.tll.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EntityCache - Generic holder of an arbitrary set of entities. Used, in
 * particular, in the generic assembly of entities.
 * @author jpk
 */
public class EntityCache implements IEntityProvider {

	private final Map<IPrimaryKey, IEntity> map = new HashMap<IPrimaryKey, IEntity>();

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
	public <E extends IEntity> E getEntity(IPrimaryKey key) {
		return (E) map.get(key);
	}

	public <E extends IEntity> boolean hasEntity(IPrimaryKey key) {
		return map.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> Collection<E> getEntitiesByType(Class<E> type) {
		if(type == null) return null;
		List<IEntity> list = new ArrayList<IEntity>();
		for(IPrimaryKey key : map.keySet()) {
			Class<? extends IEntity> etype = (Class<? extends IEntity>) key.getType();
			if(type.isAssignableFrom(etype)) {
				list.add(map.get(key));
			}
		}
		return (List<E>) list;
	}

	public <E extends IEntity> E getEntityByType(Class<E> type) throws IllegalStateException {
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
		if(e != null) map.put(e.getPrimaryKey(), e);
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
