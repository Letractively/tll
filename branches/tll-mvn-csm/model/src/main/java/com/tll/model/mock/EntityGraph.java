/**
 * The Logic Lab
 * @author jpk
 * Jan 31, 2009
 */
package com.tll.model.mock;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.tll.model.IEntity;
import com.tll.model.IEntityProvider;
import com.tll.model.key.PrimaryKey;


/**
 * EntityGraph
 * @author jpk
 */
public final class EntityGraph implements IEntityProvider {

	/**
	 * The "graph" of entities held in a map keyed by entity type.
	 */
	private final Map<Class<? extends IEntity>, Set<? extends IEntity>> map;

	/**
	 * Constructor
	 */
	public EntityGraph() {
		super();
		this.map = new HashMap<Class<? extends IEntity>, Set<? extends IEntity>>();
	}

	/**
	 * Constructor
	 * @param map The entity graph
	 */
	public EntityGraph(Map<Class<? extends IEntity>, Set<? extends IEntity>> map) {
		super();
		if(map == null) {
			throw new IllegalArgumentException("An entity map must be specified.");
		}
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	private <E extends IEntity> Set<E> getNonNullEntitySet(Class<E> entityType) {
		Set<? extends IEntity> set = map.get(entityType);
		if(set == null) {
			set = new LinkedHashSet<E>();
			map.put(entityType, set);
		}
		return (Set<E>) set;
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> void setEntities(Collection<E> entities) {
		if(entities != null && entities.size() > 0) {
			final Class<E> entityType = (Class<E>) entities.iterator().next().entityClass();
			getNonNullEntitySet(entityType).addAll(entities);
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> void setEntity(E entity) {
		if(entity != null) {
			getNonNullEntitySet((Class<E>) entity.entityClass()).add(entity);
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> Collection<E> getEntitiesByType(Class<E> type) {
		return (Collection<E>) map.get(type);
	}

	public <E extends IEntity> E getEntity(PrimaryKey<E> key) {
		if(key == null || !key.isSet()) {
			throw new IllegalArgumentException("The key is not specified or is not set");
		}
		Collection<E> clc = getEntitiesByType(key.getType());
		if(clc != null) {
			for(E e : clc) {
				if(key.getId().equals(e.getId())) {
					return e;
				}
			}
		}
		return null;
	}

	public <E extends IEntity> E getEntityByType(Class<E> type) throws IllegalStateException {
		Collection<E> clc = getEntitiesByType(type);
		if(clc != null && clc.size() != 1) {
			throw new IllegalStateException("More than one entity exists of type: " + type.getName());
		}
		return clc == null ? null : clc.iterator().next();
	}

}
