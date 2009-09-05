package com.tll.model;

import java.util.Collection;


import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.model.schema.Managed;
import com.tll.util.StringUtil;

/**
 * EntityBase - Base class for all entities.
 * @author jpk
 */
//@PersistenceCapable(identityType = IdentityType.APPLICATION)
// @Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION",
// extensions = { @Extension(vendorName = "datanucleus", key = "field-name",
// value = "version") })
public abstract class EntityBase implements IEntity {

	private static final long serialVersionUID = -4641847785797486723L;

	protected static final Log LOG = LogFactory.getLog(EntityBase.class);

	//@PrimaryKey
	//@Persistent(valueStrategy = IdGeneratorStrategy.UUIDSTRING)
	// @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value =
	// "true")
	private String id;

	//@NotPersistent
	private boolean generated;

	/**
	 * Manually driven flag to indicate entity newness.
	 */
	//@NotPersistent
	private boolean _new = true;

	@Managed
	private Integer version;

	/**
	 * finds an entity of the given id in the set or null if not found. If the
	 * given id is null, null is returned.
	 * @param <E>
	 * @param clc the collection to look in
	 * @param id the id of the entity to find
	 * @return the found entity or null if not found
	 */
	protected final static <E extends IEntity> E findEntityInCollection(Collection<E> clc, Integer id) {
		if(id == null || clc == null) {
			return null;
		}
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
	 * @param id
	 */
	public EntityBase(String id) {
		this();
		setId(id);
	}

	@Override
	public String typeName() {
		return StringUtil.camelCaseToPresentation(entityClass().getSimpleName());
	}

	@NotNull
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isGenerated() {
		return generated;
	}

	/**
	 * This method <b>must only</b> be called when a new entity is created and the
	 * id is generated. It will set the id and set the generated flag to true.
	 * @param id the id to set
	 */
	public void setGenerated(String id) {
		setId(id);
		generated = true;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final EntityBase other = (EntityBase) obj;
		if(id == null) {
			if(other.id != null) return false;
		}
		else if(!id.equals(other.id)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public final String toString() {
		return typeName() + ", id: " + getId() + ", version: " + getVersion();
	}

	public final boolean isNew() {
		return _new;
	}

	public final void setNew(boolean b) {
		this._new = b;
	}

	/*
	 * May be overridden by sub-classes for a better descriptor.
	 */
	public String descriptor() {
		return typeName() + " (Id: " + getId() + ")";
	}
}
