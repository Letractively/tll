/**
 * The Logic Lab
 * @author jpk
 * Jan 31, 2009
 */
package com.tll.model.test;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.IEntityProvider;
import com.tll.model.key.BusinessKeyPropertyException;
import com.tll.model.key.BusinessKeyUtil;
import com.tll.model.key.NonUniqueBusinessKeyException;
import com.tll.model.key.PrimaryKey;

/**
 * EntityGraph - A really poor man's managed set of in memory entities.
 * @author jpk
 */
public final class EntityGraph implements IEntityProvider {

	/**
	 * The "graph" of entities held in a map keyed by entity type.
	 */
	private final LinkedHashMap<Class<? extends IEntity>, LinkedHashSet<? extends IEntity>> graph;

	/**
	 * Constructor
	 */
	public EntityGraph() {
		super();
		this.graph = new LinkedHashMap<Class<? extends IEntity>, LinkedHashSet<? extends IEntity>>();
	}

	/**
	 * Removes all entities from the graph.
	 */
	public void clear() {
		graph.clear();
	}

	/**
	 * @return Iterator of all held entity types in the graph ordered in the way
	 *         it was populated.
	 */
	public Iterator<Class<? extends IEntity>> getEntityTypes() {
		return graph.keySet().iterator();
	}

	/**
	 * @param etype the entity type
	 * @return The total number of entities of the given type.
	 */
	public int size(Class<? extends IEntity> etype) {
		return graph.containsKey(etype) ? 0 : graph.get(etype).size();
	}

	/**
	 * @return The total number of entities in the graph.
	 */
	public int size() {
		int size = 0;
		for(final Set<? extends IEntity> set : graph.values()) {
			size += set.size();
		}
		return size;
	}

	/**
	 * Grabs the entity set from the calculated root entity type of the given
	 * entity type.
	 * @param <E>
	 * @param entityType
	 * @return set of distinct root entities
	 */
	@SuppressWarnings("unchecked")
	private <E extends IEntity> Set<? extends E> getRootEntitySet(Class<E> entityType) {
		return (Set<? extends E>) graph.get(EntityUtil.getRootEntityClass(entityType));

	}

	/**
	 * Grabs the entity set bound to the root entity type of the given entity type
	 * creating it if it doesn't yet exist.
	 * @param entityType
	 * @return The non-<code>null</code> entity set for the <em>root</em> entity
	 *         type of the given entity type.
	 */
	Set<? extends IEntity> getNonNullEntitySet(Class<? extends IEntity> entityType) {
		final Class<? extends IEntity> rootType = EntityUtil.getRootEntityClass(entityType);
		LinkedHashSet<? extends IEntity> set = graph.get(rootType);
		if(set == null) {
			set = new LinkedHashSet<IEntity>();
			graph.put(rootType, set);
		}
		return set;
	}

	public <E extends IEntity> Collection<E> getEntitiesByType(Class<E> type) {
		final Set<? extends E> rootset = getRootEntitySet(type);
		if(rootset == null) return null;
		final Set<E> set = new LinkedHashSet<E>();
		for(final E e : rootset) {
			if(type == e.entityClass() || type.isAssignableFrom(e.entityClass())) {
				set.add(e);
			}
		}
		return set;
	}

	public <E extends IEntity> E getEntityByType(Class<E> type) throws IllegalStateException {
		final Collection<E> clc = getEntitiesByType(type);
		if(clc == null || clc.size() == 0) return null;
		if(clc.size() == 1) {
			return clc.iterator().next();
		}
		throw new IllegalStateException("More than one entity exists of type: " + type.getName());
	}

	public <E extends IEntity> E getEntity(PrimaryKey<E> key) {
		if(key == null || !key.isSet()) {
			throw new IllegalArgumentException("The key is not specified or is not set");
		}
		final Collection<E> clc = getEntitiesByType(key.getType());
		if(clc != null) {
			for(final E e : clc) {
				if(key.getId().equals(e.getId())) {
					return e;
				}
			}
		}
		return null;
	}

	/**
	 * Checks to see if the graph contains the given entity by primary key.
	 * @param <E>
	 * @param pk The primary key
	 * @return true/false
	 */
	@SuppressWarnings("unchecked")
	public <E extends IEntity> boolean contains(PrimaryKey<E> pk) {
		final Set<E> set = (Set<E>) getRootEntitySet(pk.getType());
		if(set != null) {
			for(final E e : set) {
				if(e.getId().equals(pk.getId())) return true;
			}
		}
		return false;
	}

	/**
	 * Attempts to set the given entity failing when the graph is subsequently
	 * found invalid.
	 * @param <E>
	 * @param entity The entity to set
	 * @throws IllegalStateException When the entity already exist in the graph.
	 * @throws NonUniqueBusinessKeyException When the set operation fails due to a
	 *         non-unique business key among like type entities in the graph as a
	 *         result of the added entity.
	 */
	@SuppressWarnings("unchecked")
	public <E extends IEntity> void setEntity(E entity) throws IllegalStateException, NonUniqueBusinessKeyException {
		if(entity != null) {
			final Set<E> set = (Set<E>) getNonNullEntitySet(entity.entityClass());
			if(!set.add(entity)) {
				throw new IllegalStateException("Unable to add entity to entity set");
			}
			boolean ok = false;
			try {
				validateEntitySet((Class<E>) entity.entityClass());
				ok = true;
			}
			finally {
				if(!ok) set.remove(entity);
			}
		}
	}

	/**
	 * Attempts to set the given entities failing when the graph is subsequently
	 * found invalid.
	 * @param <E>
	 * @param entities The entities to set
	 * @throws IllegalStateException When one or more of the given entites already
	 *         exist in the graph.
	 * @throws NonUniqueBusinessKeyException When the set operation fails due to a
	 *         non-unique business key among like type entities in the graph as a
	 *         result of the added entities.
	 */
	@SuppressWarnings("unchecked")
	public <E extends IEntity> void setEntities(Collection<E> entities) throws IllegalStateException,
	NonUniqueBusinessKeyException {
		if(entities != null && entities.size() > 0) {
			final Class<E> entityType = (Class<E>) entities.iterator().next().entityClass();
			final Set<E> set = (Set<E>) getNonNullEntitySet(entityType);
			if(!set.addAll(entities)) {
				throw new IllegalStateException("Unable to add entities to entity set");
			}
			boolean ok = false;
			try {
				validateEntitySet(entityType);
				ok = true;
			}
			finally {
				if(!ok) set.removeAll(entities);
			}
		}
	}

	/**
	 * Attempts to remove the given entity.
	 * @param <E>
	 * @param entity
	 * @return The removed entity or <code>null</code> if unsuccessful.
	 */
	public <E extends IEntity> E removeEntity(E entity) {
		if(entity != null) {
			if(getNonNullEntitySet(entity.entityClass()).remove(entity)) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * Validates the set of entities for a particular entity type ensuring all
	 * entities in the set are unique by defined business keys.
	 * @param <E>
	 * @param entityType
	 * @throws NonUniqueBusinessKeyException When the entity set is found to be
	 *         business key non-unique.
	 */
	private <E extends IEntity> void validateEntitySet(Class<E> entityType) throws NonUniqueBusinessKeyException {
		final Set<? extends IEntity> set = getRootEntitySet(entityType);
		try {
			BusinessKeyUtil.isBusinessKeyUnique(set);
		}
		catch(final BusinessKeyPropertyException e) {
			throw new IllegalStateException("Unable to validate entity graph: " + e.getMessage(), e);
		}
	}

	/**
	 * Validates the entity graph.
	 * @throws NonUniqueBusinessKeyException When an entity set is found to be
	 *         business key non-unique.
	 * @see #validateEntitySet(Class)
	 */
	void validate() throws NonUniqueBusinessKeyException {
		for(final Class<? extends IEntity> entityType : graph.keySet()) {
			validateEntitySet(entityType);
		}
	}
}
