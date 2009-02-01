/**
 * The Logic Lab
 * @author jpk
 * Jan 31, 2009
 */
package com.tll.model.mock;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.tll.model.IEntity;


/**
 * EntityGraphBuilderHelper
 * @author jpk
 */
public abstract class AbstractEntityGraphBuilder implements IEntityGraphBuilder {

	private final MockEntityFactory mep;

	private final Map<Class<? extends IEntity>, Set<? extends IEntity>> map =
			new HashMap<Class<? extends IEntity>, Set<? extends IEntity>>();

	/**
	 * Constructor
	 * @param mep The mock entity factory
	 */
	public AbstractEntityGraphBuilder(MockEntityFactory mep) {
		super();
		this.mep = mep;
	}

	public final EntityGraph buildEntityGraph() {
		clear();
		stub();
		return new EntityGraph(map);
	}

	private void clear() {
		map.clear();
	}

	protected abstract void stub();

	/**
	 * Attempts to retrive the Nth (1-based) entity of the given type.
	 * @param <E>
	 * @param type
	 * @param n 1-based
	 * @return the Nth entity or <code>null</code> if n is greater than the number
	 *         of entities of the given type.
	 */
	@SuppressWarnings("unchecked")
	protected final <E extends IEntity> E getNthEntity(Class<E> type, int n) {
		Set<E> set = (Set<E>) map.get(type);
		if(set != null && set.size() >= n) {
			int i = 0;
			for(E e : set) {
				if(++i == n) {
					return e;
				}
			}
		}
		return null;
	}
	
	/**
	 * Generates a fresh entity that is <em>not</em> added to the map.
	 * @param <E>
	 * @param entityType
	 * @param version
	 * @param makeUnique
	 * @return The generated entity.
	 */
	protected final <E extends IEntity> E generateEntity(Class<E> entityType, int version, boolean makeUnique) {
		E e = mep.getEntityCopy(entityType, makeUnique);
		e.setVersion(version);
		return e;
	}

	/**
	 * Returns a ref to the entity set held in the map keyed by the given entity
	 * type. If it is <code>null</code>, the set is stubbed then returned thus
	 * ensuring a never <code>null</code> return value.
	 * @param <E>
	 * @param entityType
	 * @return The never <code>null</code> entity set.
	 */
	@SuppressWarnings("unchecked")
	protected final <E extends IEntity> Set<E> getNonNullEntitySet(Class<E> entityType) {
		Set<E> set = (Set<E>) map.get(entityType);
		if(set == null) {
			set = new LinkedHashSet<E>();
			map.put(entityType, set);
		}
		return set;
	}

	/**
	 * Adds the given entity to the map.
	 * @param <E>
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	private <E extends IEntity> void addEntity(E e) {
		getNonNullEntitySet((Class<E>) e.entityClass()).add(e);
	}

	/**
	 * Convenience method that firsrt generates an entity of the given type then
	 * adds it to the map (graph).
	 * @param <E>
	 * @param entityType
	 * @param version
	 * @param makeUnique
	 * @return The generated entity that was added to the map.
	 */
	protected final <E extends IEntity> E generateAndAdd(Class<E> entityType, int version, boolean makeUnique) {
		E e = generateEntity(entityType, version, makeUnique);
		addEntity(e);
		return e;
	}
	
	/**
	 * Generates N unique entity copies of the given type then adds them to the
	 * map.
	 * @param <E>
	 * @param entityType
	 * @param version
	 * @parm n The number of copies to generate
	 * @return The generated entity set that was added to the map.
	 */
	protected final <E extends IEntity> Set<E> generateAndAddN(Class<E> entityType, int version, int n) {
		Set<E> set = mep.getNEntityCopies(entityType, n, true);
		for(E e : set) {
			addEntity(e);
		}
		return set;
	}
	
	/**
	 * Grabs <em>all</em> entity instances held in the {@link MockEntityFactory}
	 * of the given type then adds them to the map.
	 * @param <E>
	 * @param entityType
	 * @param version
	 * @return The generated entity set that was added to the map.
	 */
	protected final <E extends IEntity> Set<E> generateAndAddAll(Class<E> entityType, int version) {
		Set<E> set = mep.getAllEntityCopies(entityType);
		for(E e : set) {
			e.setVersion(version);
			addEntity(e);
		}
		return set;
	}
	
	/**
	 * Retrieves an entity of the given type from the map randomly.
	 * @param <E>
	 * @param entityType
	 * @return The randomly selected entity from the map.
	 */
	protected final <E extends IEntity> E getRandomExisting(Class<E> entityType) {
		Set<E> set = getNonNullEntitySet(entityType);
		if(set.size() == 0) {
			throw new IllegalStateException("No entities of the given type yet exist.");
		}
		else if(set.size() == 1) {
			return set.iterator().next();
		}
		return getNthEntity(entityType, RandomUtils.nextInt(set.size()));
	}
}
