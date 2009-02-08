/**
 * The Logic Lab
 * @author jpk
 * Jan 31, 2009
 */
package com.tll.model.mock;

import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.tll.model.IEntity;
import com.tll.model.key.NonUniqueBusinessKeyException;


/**
 * EntityGraphBuilderHelper
 * @author jpk
 */
public abstract class AbstractEntityGraphBuilder implements IEntityGraphBuilder {

	/**
	 * Responsible for generating prototypical entity instances that are subject
	 * to addition to the entity graph.
	 */
	private final MockEntityFactory mockEntityFactory;
	
	/**
	 * The entity graph.
	 */
	private final EntityGraph graph = new EntityGraph(); 

	/**
	 * Constructor
	 * @param mockEntityFactory The mock entity factory
	 */
	public AbstractEntityGraphBuilder(MockEntityFactory mockEntityFactory) {
		super();
		this.mockEntityFactory = mockEntityFactory;
	}

	public final EntityGraph buildEntityGraph() throws IllegalStateException {
		graph.clear();
		stub();
		try {
			graph.validate();
		}
		catch(NonUniqueBusinessKeyException e) {
			throw new IllegalStateException("Non-business key unique entity graph", e);
		}
		return graph;
	}
	
	/**
	 * Stubs the entity graph.
	 * <p>
	 * <em><b>NOTE:</b> All entities added to the graph will have their version set to
	 * <code>0</code></em>.
	 */
	protected abstract void stub();
	
	/**
	 * Generates a fresh entity that is <em>not</em> added to the graph.
	 * @param <E>
	 * @param entityType
	 * @param makeUnique
	 * @return The generated entity.
	 */
	protected final <E extends IEntity> E generateEntity(Class<E> entityType, boolean makeUnique) {
		return mockEntityFactory.getEntityCopy(entityType, makeUnique);
	}

	/**
	 * Returns a ref to the entity set held in the graph keyed by the given entity
	 * type. If it doesn't exist, it is first created and added to the graph.
	 * @param entityType
	 * @return The never <code>null</code> entity set.
	 */
	protected final Set<? extends IEntity> getNonNullEntitySet(Class<? extends IEntity> entityType) {
		return graph.getNonNullEntitySet(entityType);
	}

	/**
	 * Adds the given entity to the graph.
	 * @param <E>
	 * @param e
	 * @return The added entity for convenience.
	 */
	@SuppressWarnings("unchecked")
	private <E extends IEntity> E addEntity(E e) {
		Set set = getNonNullEntitySet(e.entityClass());
		if(!set.add(e)) {
			throw new IllegalStateException("Unable to add entity to the graph: " + e);
		}
		e.setVersion(0); // since we are now in the graph
		return e;
	}

	/**
	 * Convenience method that first generates an entity of the given type then
	 * adds it to the graph.
	 * @param <E>
	 * @param entityType
	 * @param makeUnique
	 * @return The generated entity that was added to the graph.
	 */
	protected final <E extends IEntity> E add(Class<E> entityType, boolean makeUnique) {
		return addEntity(generateEntity(entityType, makeUnique));
	}

	/**
	 * Generates N unique entity copies of the given type then adds them to the
	 * graph.
	 * @param <E>
	 * @param entityType
	 * @param makeUnique Attempt to make the entities business key unique before
	 *        adding?
	 * @parm n The number of copies to generate
	 * @return The generated entity set that was added to the graph.
	 */
	protected final <E extends IEntity> Set<E> addN(Class<E> entityType, boolean makeUnique, int n) {
		Set<E> set = mockEntityFactory.getNEntityCopies(entityType, n, makeUnique);
		for(E e : set) {
			addEntity(e);
		}
		return set;
	}

	/**
	 * Grabs <em>all</em> entity instances held in the {@link MockEntityFactory}
	 * of the given type then adds them to the graph.
	 * @param <E>
	 * @param entityType
	 * @return The generated entity set that was added to the graph.
	 */
	protected final <E extends IEntity> Set<E> addAll(Class<E> entityType) {
		Set<E> set = mockEntityFactory.getAllEntityCopies(entityType);
		for(E e : set) {
			addEntity(e);
		}
		return set;
	}

	/**
	 * Attempts to retrive the Nth (1-based) entity of the given type.
	 * @param <E>
	 * @param entityType
	 * @param n 1-based
	 * @return the Nth entity or <code>null</code> if n is greater than the number
	 *         of entities of the given type.
	 */
	protected final <E extends IEntity> E getNthEntity(Class<E> entityType, int n) {
		Set<E> set = (Set<E>) graph.getEntitiesByType(entityType);
		final int size = set == null ? 0 : set.size();
		if(set != null && size >= n) {
			int i = 0;
			for(E e : set) {
				if(++i == n) {
					return e;
				}
			}
		}
		throw new IllegalStateException(size + " entities exist of type " + entityType + " exist but number " + n
				+ " was requested.");
	}

	/**
	 * Retrieves an entity of the given type from the graph randomly.
	 * @param <E>
	 * @param entityType
	 * @return The randomly selected entity from the graph.
	 */
	protected final <E extends IEntity> E getRandomEntity(Class<E> entityType) {
		Set<E> set = (Set<E>) graph.getEntitiesByType(entityType);
		if(set == null || set.size() == 0) {
			throw new IllegalStateException("No entities of the given type yet exist.");
		}
		else if(set.size() == 1) {
			return set.iterator().next();
		}
		return getNthEntity(entityType, RandomUtils.nextInt(set.size()) + 1);
	}
	
	/**
	 * Makes the given entity unique by its defined business keys.
	 * @param <E>
	 * @param e
	 */
	protected final <E extends IEntity> void makeUnique(E e) {
		MockEntityFactory.makeBusinessKeyUnique(e);
	}

	/**
	 * Convenience method that generate a random integer between
	 * <code>0<code> and <code>uboundExclusive</code> (exclusive).
	 * @param uboundExclusive The exclusive upper bound
	 * @return integer
	 */
	protected final int randomInt(int uboundExclusive) {
		return RandomUtils.nextInt(uboundExclusive);
	}
}
