/**
 * The Logic Lab
 * @author jpk
 * Jan 31, 2009
 */
package com.tll.model.egraph;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.tll.model.IEntity;
import com.tll.model.IEntityMetadata;
import com.tll.model.IEntityProvider;
import com.tll.model.bk.BusinessKeyFactory;
import com.tll.model.bk.BusinessKeyPropertyException;
import com.tll.model.bk.NonUniqueBusinessKeyException;

/**
 * EntityGraph - A really poor man's managed set of in memory entities.
 * @author jpk
 */
public final class EntityGraph implements IEntityProvider {

	private final IEntityMetadata entityMetadata;
	
	private final BusinessKeyFactory bkf;
	
	/**
	 * The "graph" of entities held in a map keyed by entity type.
	 */
	private final LinkedHashMap<Class<?>, LinkedHashSet<?>> graph;
	
	/**
	 * Constructor
	 * @param entityMetadata 
	 * @param bkf 
	 */
	@Inject
	public EntityGraph(IEntityMetadata entityMetadata, BusinessKeyFactory bkf) {
		super();
		this.entityMetadata = entityMetadata;
		this.bkf = bkf;
		this.graph = new LinkedHashMap<Class<?>, LinkedHashSet<?>>();
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
	public Iterator<Class<?>> getEntityTypes() {
		return graph.keySet().iterator();
	}

	/**
	 * @param etype the entity type
	 * @return The total number of entities of the given type.
	 */
	public int size(Class<?> etype) {
		return graph.containsKey(etype) ? 0 : graph.get(etype).size();
	}

	/**
	 * @return The total number of entities in the graph.
	 */
	public int size() {
		int size = 0;
		for(final Set<?> set : graph.values()) {
			size += set.size();
		}
		return size;
	}

	/**
	 * Grabs the entity set from the calculated root entity type of the given
	 * entity type.
	 * @param entityType
	 * @return set of distinct root entities
	 */
	private Set<?> getRootEntitySet(Class<?> entityType) {
		return graph.get(entityMetadata.getRootEntityClass(entityType));

	}

	/**
	 * Grabs the entity set bound to the root entity type of the given entity type
	 * creating it if it doesn't yet exist.
	 * @param entityType
	 * @return The non-<code>null</code> entity set for the <em>root</em> entity
	 *         type of the given entity type.
	 */
	Set<?> getNonNullEntitySet(Class<?> entityType) {
		final Class<?> rootType = entityMetadata.getRootEntityClass(entityType);
		LinkedHashSet<?> set = graph.get(rootType);
		if(set == null) {
			set = new LinkedHashSet<IEntity>();
			graph.put(rootType, set);
		}
		return set;
	}

	@Override
	public <E> Collection<E> getEntitiesByType(Class<E> type) {
		@SuppressWarnings("unchecked")
		final Set<E> rootset = (Set<E>) getRootEntitySet(type);
		if(rootset == null) return null;
		final Set<E> set = new LinkedHashSet<E>();
		for(final E e : rootset) {
			Class<?> eclass = entityMetadata.getEntityClass(e);
			if(type == eclass || type.isAssignableFrom(eclass)) {
				set.add(e);
			}
		}
		return set;
	}

	@Override
	public <E> E getEntityByType(Class<E> type) throws IllegalStateException {
		final Collection<E> clc = getEntitiesByType(type);
		if(clc == null || clc.size() == 0) return null;
		if(clc.size() == 1) {
			return clc.iterator().next();
		}
		throw new IllegalStateException("More than one entity exists of type: " + type.getName());
	}

	@Override
	public <E> E getEntity(Class<E> entityType, Object pk) {
		if(pk == null) throw new IllegalArgumentException("No primary key specified");
		final Collection<E> clc = getEntitiesByType(entityType);
		if(clc != null) {
			for(final E e : clc) {
				Object eid = entityMetadata.getId(e);
				if(pk.equals(eid)) {
					return e;
				}
			}
		}
		return null;
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
	private <E> void validateEntitySet(Class<E> entityType) throws NonUniqueBusinessKeyException {
		final Set<?> set = getRootEntitySet(entityType);
		try {
			bkf.isBusinessKeyUnique(set);
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
		for(final Class<?> entityType : graph.keySet()) {
			validateEntitySet(entityType);
		}
	}
}
