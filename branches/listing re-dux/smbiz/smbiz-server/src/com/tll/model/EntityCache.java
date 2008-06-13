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

import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;

/**
 * EntityCache - Generic holder of an arbitrary set of entities. Used, in
 * particular, in the generic assembly of entities.
 * @author jpk
 */
public class EntityCache implements IEntityProvider {

	private final Map<IPrimaryKey<IEntity>, IEntity> map = new HashMap<IPrimaryKey<IEntity>, IEntity>();

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
	public <E extends IEntity> E getEntity(IPrimaryKey<E> key) {
		return (E) map.get(key);
	}

	public boolean hasEntity(IPrimaryKey<? extends IEntity> key) {
		return map.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<? extends E> getEntitiesByType(Class<E> type) {
		if(type == null) return null;
		List<IEntity> list = new ArrayList<IEntity>();
		for(IPrimaryKey<? extends IEntity> key : map.keySet()) {
			Class<? extends IEntity> etype = key.getType();
			if(type.isAssignableFrom(etype)) {
				list.add(map.get(key));
			}
		}
		return (List<? extends E>) list;
	}

	public <E extends IEntity> E getEntityByType(Class<E> type) throws IllegalStateException {
		if(type == null) return null;
		List<? extends E> list = getEntitiesByType(type);
		if(list.size() > 1) {
			throw new IllegalStateException("More than one entity of the type: " + type.getSimpleName() + " exists.");
		}
		else if(list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	public void addEntity(IEntity e) {
		if(e != null) map.put(KeyFactory.getPrimaryKey(e), e);
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