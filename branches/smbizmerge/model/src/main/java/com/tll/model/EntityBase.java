package com.tll.model;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.util.StringUtil;

/**
 * EntityBase - Base class for all entities.
 * @author jpk
 */
public abstract class EntityBase implements IEntity {

	private static final long serialVersionUID = -4641847785797486723L;

	protected static final Log LOG = LogFactory.getLog(EntityBase.class);
	
	private Long id;

	private boolean generated;

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
	 * @param id the primary key of the entity to find
	 * @return the found entity or null if not found
	 */
	protected final static <E extends IEntity> E findEntityInCollection(Collection<E> clc, Object id) {
		if(clc == null || id == null) return null;
		for(final E e : clc) {
			if(id.equals(e.getId())) {
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
	@SuppressWarnings({
		"unchecked", "rawtypes" })
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
	@SuppressWarnings({
		"unchecked", "rawtypes" })
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
	@SuppressWarnings({
		"unchecked", "rawtypes" })
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

	@Override
	public Class<? extends IEntity> rootEntityClass() {
		return entityClass();
	}

	/*
	 * Base impl which may be overridden.
	 */
	@Override
	public String getEntityType() {
		return typeDesc();
	}

	@Override
	public String typeDesc() {
		return StringUtil.camelCaseToPresentation(entityClass().getSimpleName());
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
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
	
	@Override
	public final boolean isGenerated() {
		return generated;
	}

	@Override
	public void setGenerated(Object id) {
		if(id == null) throw new IllegalArgumentException("Generated primary keys can't be null.");
		if(id instanceof Long == false) throw new IllegalStateException();
		setId((Long)id);
		this.generated = true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		final Object sid = getId();
		result = prime * result + ((sid == null) ? 0 : sid.hashCode());
		result = prime * result + (int) (version ^ (version >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final EntityBase other = (EntityBase) obj;
		final Object sid = getId(), otherId = other.getId();
		if(sid == null) {
			if(otherId != null) return false;
		}
		else if(!sid.equals(otherId)) return false;
		if(version != other.version) return false;
		return true;
	}

	@Override
	public String toString() {
		return typeDesc() + ", id: " + getId() + ", version: " + getVersion();
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
		return typeDesc() + " (Id: " + getId() + ")";
	}
}
