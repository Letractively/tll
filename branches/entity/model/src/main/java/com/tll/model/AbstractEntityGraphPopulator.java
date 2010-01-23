/**
 * The Logic Lab
 * @author jpk
 * Jan 31, 2009 Jan 31, 2009
 */
package com.tll.model;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.model.bk.NonUniqueBusinessKeyException;

/**
 * AbstractEntityGraphPopulator
 * @author jpk
 */
public abstract class AbstractEntityGraphPopulator implements IEntityGraphPopulator {

	protected final Log log;

	/**
	 * Responsible for generating prototypical entity instances that are subject
	 * to addition to the entity graph.
	 */
	private final EntityBeanFactory entityBeanFactory;

	/**
	 * The entity graph.
	 */
	private EntityGraph graph;

	/**
	 * Constructor
	 * @param entityBeanFactory Required entity bean factory ref
	 */
	public AbstractEntityGraphPopulator(EntityBeanFactory entityBeanFactory) {
		this.log = LogFactory.getLog(getClass());
		this.entityBeanFactory = entityBeanFactory;
	}

	@Override
	public final EntityGraph getEntityGraph() {
		return graph;
	}

	@Override
	public final void setEntityGraph(EntityGraph graph) {
		this.graph = graph;
	}

	@Override
	public final void populateEntityGraph() throws IllegalStateException {
		graph.clear();
		log.info("Stubbing entity graph..");
		stub();
		try {
			graph.validate();
		}
		catch(final NonUniqueBusinessKeyException e) {
			throw new IllegalStateException("Non-business key unique entity graph", e);
		}
	}

	/**
	 * Stubs the entity graph.
	 * <p>
	 * <em><b>NOTE:</b> All entities added to the graph will have their version set to
	 * <code>0</code></em>.
	 * @throws IllegalStateException when an error occurs.
	 */
	protected abstract void stub() throws IllegalStateException;

	/**
	 * Generates a fresh entity that is <em>not</em> added to the graph.
	 * @param <E>
	 * @param entityType
	 * @param makeUnique
	 * @return The generated entity.
	 */
	protected final <E extends IEntity> E generateEntity(Class<E> entityType, boolean makeUnique) {
		return entityBeanFactory.getEntityCopy(entityType, makeUnique);
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
		final Set set = getNonNullEntitySet(e.entityClass());
		if(!set.add(e)) {
			throw new IllegalStateException("Unable to add entity to the graph: " + e);
		}
		// since we are now in the graph, mimic persistence behavior:
		e.setVersion(0);
		if(e instanceof ITimeStampEntity) {
			final Date now = new Date();
			((ITimeStampEntity) e).setDateCreated(now);
			((ITimeStampEntity) e).setDateModified(now);
		}
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
		try {
			return addEntity(generateEntity(entityType, makeUnique));
		}
		catch(final Throwable t) {
			log.error(t);
			throw new RuntimeException(t);
		}
	}

	/**
	 * Generates N unique entity copies of the given type then adds them to the
	 * graph.
	 * @param <E>
	 * @param entityType
	 * @param makeUnique Attempt to make the entities business key unique before
	 *        adding?
	 * @param n The number of copies to generate
	 * @return The generated entity set that was added to the graph.
	 */
	protected final <E extends IEntity> Set<E> addN(Class<E> entityType, boolean makeUnique, int n) {
		final Set<E> set = entityBeanFactory.getNEntityCopies(entityType, n, makeUnique);
		for(final E e : set) {
			addEntity(e);
		}
		return set;
	}

	/**
	 * Grabs <em>all</em> entity instances held in the {@link EntityBeanFactory}
	 * of the given type then adds them to the graph.
	 * @param <E>
	 * @param entityType
	 * @return The generated entity set that was added to the graph.
	 */
	protected final <E extends IEntity> Set<E> addAll(Class<E> entityType) {
		final Set<E> set = entityBeanFactory.getAllEntityCopies(entityType);
		for(final E e : set) {
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
		final Set<E> set = (Set<E>) graph.getEntitiesByType(entityType);
		final int size = set == null ? 0 : set.size();
		if(set != null && size >= n) {
			int i = 0;
			for(final E e : set) {
				if(++i == n) {
					return e;
				}
			}
		}
		throw new IllegalStateException(size + " entities exist of type " + entityType + " exist but number " + n
				+ " was requested.");
	}

	/**
	 * Attempts to retrive the Nth (1-based) entity of the given type.
	 * @param <E>
	 * @param entityType
	 * @param name the entity name (entity type must be implement
	 *        {@link INamedEntity}).
	 * @return the matching entity or <code>null</code> if not found
	 * @throws IllegalArgumentException When the given entity type does not implement {@link INamedEntity}
	 */
	protected final <E extends IEntity> E getEntityByName(Class<E> entityType, String name) throws IllegalArgumentException {
		if(!INamedEntity.class.isAssignableFrom(entityType)) {
			throw new IllegalArgumentException("Entity type: " + entityType + " is not a named entity type");
		}
		final Set<E> set = (Set<E>) graph.getEntitiesByType(entityType);
		if(set != null) {
			for(final E e : set) {
				final String ename = ((INamedEntity)e).getName();
				if(ename != null && ename.equals(name)) return e;
			}
		}
		// not found
		return null;
	}

	/**
	 * Retrieves an entity of the given type from the graph randomly.
	 * @param <E>
	 * @param entityType
	 * @return The randomly selected entity from the graph.
	 */
	protected final <E extends IEntity> E getRandomEntity(Class<E> entityType) {
		final Set<E> set = (Set<E>) graph.getEntitiesByType(entityType);
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
		EntityBeanFactory.makeBusinessKeyUnique(e);
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
