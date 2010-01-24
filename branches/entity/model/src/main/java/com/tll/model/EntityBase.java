package com.tll.model;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.model.schema.Managed;
import com.tll.util.StringUtil;

/**
 * EntityBase - Base class for all entities.
 * @author jpk
 */
public abstract class EntityBase implements IEntity {

	private static final long serialVersionUID = -4641847785797486723L;

	protected static final Log LOG = LogFactory.getLog(EntityBase.class);

	private Object pk;
	
	//private boolean generated;

	/**
	 * At object creation, a version of <code>-1</code> is assigined indicating a
	 * <em>transient</em> (not persisted yet) entity.
	 */
	private long version = -1;

	/**
	 * finds an entity of the given id in the set or null if not found. If the
	 * given id is null, null is returned.
	 * @param <E>
	 * @param clc the collection to look in
	 * @param pk the primary key of the entity to find
	 * @return the found entity or null if not found
	 */
	protected final static <E extends IEntity> E findEntityInCollection(Collection<E> clc, Object pk) {
		if(clc == null || pk == null) return null;
		for(final E e : clc) {
			if(pk.equals(e.getPrimaryKey())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * finds an entity of the given name in the collection or null if not found.
	 * If the given id is null, null is returned.
	 * @param <N>
	 * @param clc the collection to look in
	 * @param name the name of the named entity to find
	 * @return the found named entity or null if not found
	 */
	protected final static <N extends INamedEntity> N findNamedEntityInCollection(Collection<N> clc, String name) {
		if(name == null || clc == null) {
			return null;
		}
		for(final N e : clc) {
			if(name.equals(e.getName())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * adds an entity to a set. if the set or entity is null, nothing happens.
	 * @param <E>
	 * @param clc the collection to which the entity is being added
	 * @param e the entity to be added
	 */
	@SuppressWarnings("unchecked")
	protected final <E extends IEntity> void addEntityToCollection(Collection<E> clc, E e) {
		if(e == null) {
			return;
		}
		if(clc == null) {
			throw new IllegalStateException("The collection argument must not be null");
		}
		clc.add(e);
		if(e instanceof IChildEntity) {
			((IChildEntity) e).setParent(this);
		}
	}

	/**
	 * Adds a collection of entities to a target entity collection.
	 * @param <E>
	 * @param toAdd
	 * @param clc the collection to which the entities are added
	 */
	protected final <E extends IEntity> void addEntitiesToCollection(Collection<E> toAdd, Collection<E> clc) {
		if(toAdd != null) {
			if(clc == null) {
				throw new IllegalStateException("The collection argument must not be null");
			}
			for(final E e : toAdd) {
				addEntityToCollection(clc, e);
			}
		}
	}

	/**
	 * Removes an entity from a collection.
	 * @param <E>
	 * @param clc
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	protected final static <E extends IEntity> void removeEntityFromCollection(Collection<E> clc, E e) {
		if(clc != null && clc.remove(e)) {
			if(e instanceof IChildEntity) {
				((IChildEntity) e).setParent(null);
			}
		}
	}

	/**
	 * @param <E>
	 * @param clc of entities (which may be child entities)
	 */
	@SuppressWarnings("unchecked")
	protected final static <E extends IEntity> void clearEntityCollection(Collection<E> clc) {
		if(clc == null || clc.size() < 1) {
			return;
		}
		for(final E e : clc) {
			if(e instanceof IChildEntity) {
				((IChildEntity) e).setParent(null);
			}
			else {
				break; // assume entire set is composed of non-child entities
			}
		}
		clc.clear();
	}

	/**
	 * Convenience method entity collection size.
	 * @param <E>
	 * @param clc
	 * @return the size of the entity collection
	 */
	protected final static <E extends IEntity> int getCollectionSize(Collection<E> clc) {
		return clc == null ? 0 : clc.size();
	}

	/**
	 * Constructor
	 */
	public EntityBase() {
		super();
	}

	/**
	 * Constructor
	 * @param pk primary key
	 */
	public EntityBase(Object pk) {
		this();
		setPrimaryKey(pk);
	}

	@Override
	public Class<? extends IEntity> rootEntityClass() {
		return entityClass();
	}

	@Override
	public String typeDesc() {
		return StringUtil.camelCaseToPresentation(entityClass().getSimpleName());
	}

	@Override
	public Object getPrimaryKey() {
		return pk;
	}

	@Override
	public void setPrimaryKey(Object pk) {
		this.pk = pk;
	}

	@Managed
	@Override
	public final long getVersion() {
		return version;
	}

	@Override
	public final void setVersion(long version) {
		this.version = version;
	}
	
	/*
	@Override
	public boolean isGenerated() {
		return generated;
	}

	@Override
	public void setGenerated(long id) {
		this.id = Long.valueOf(id);
		this.generated = true;
	}
	*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
		result = prime * result + (int) (version ^ (version >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final EntityBase other = (EntityBase) obj;
		if(pk == null) {
			if(other.pk != null) return false;
		}
		else if(!pk.equals(other.pk)) return false;
		if(version != other.version) return false;
		return true;
	}

	@Override
	public final String toString() {
		return typeDesc() + ", pk: " + getPrimaryKey() + ", version: " + getVersion();
	}

	@Override
	public final boolean isNew() {
		return version == -1;
	}

	/*
	 * May be overridden by sub-classes for a better descriptor.
	 */
	@Override
	public String descriptor() {
		return typeDesc() + " (Pk: " + getPrimaryKey() + ")";
	}
}
