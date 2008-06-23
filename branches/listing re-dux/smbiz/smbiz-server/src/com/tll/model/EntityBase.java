package com.tll.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;

import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;
import com.tll.model.schema.Managed;

/**
 * Base class for all entities.
 * @author jpk
 */
@MappedSuperclass
@SuppressWarnings("unchecked")
public abstract class EntityBase implements IEntity {

	protected static final Log LOG = LogFactory.getLog(EntityBase.class);
	private Integer id;
	private boolean generated = false;
	private Integer version;

	/**
	 * finds an entity of the given id in the set or null if not found. If the
	 * given id is null, null is returned.
	 * @param clc the collection to look in
	 * @param id the id of the entity to find
	 * @return the found entity or null if not found
	 */
	protected final static <E extends IEntity> E findEntityInCollection(Collection<E> clc, Integer id) {
		if(id == null || clc == null) {
			return null;
		}
		for(E e : clc) {
			if(id.equals(e.getId())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * finds an entity of the given name in the collection or null if not found.
	 * If the given id is null, null is returned.
	 * @param clc the collection to look in
	 * @param name the name of the named entity to find
	 * @return the found named entity or null if not found
	 */
	protected final static <N extends INamedEntity> N findNamedEntityInCollection(Collection<N> clc, String name) {
		if(name == null || clc == null) {
			return null;
		}
		for(N e : clc) {
			if(name.equals(e.getName())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * adds an entity to a set. if the set or entity is null, nothing happens.
	 * @param clc the collection to which the entity is being added
	 * @param clcClass the collection class in case it needs to be
	 * @param e the entity to be added
	 */
	@Transient
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
	 * @param toAdd
	 * @param clc the collection to which the entities are added
	 * @param clcClass the collection class in case it needs to be
	 */
	@Transient
	protected final <E extends IEntity> void addEntitiesToCollection(Collection<E> toAdd, Collection<E> clc) {
		if(toAdd != null) {
			if(clc == null) {
				throw new IllegalStateException("The collection argument must not be null");
			}
			for(E e : toAdd) {
				addEntityToCollection(clc, e);
			}
		}
	}

	/**
	 * Removes an entity from a collection.
	 * @param clc
	 * @param e
	 */
	protected final static <E extends IEntity> void removeEntityFromCollection(Collection<E> clc, E e) {
		if(clc != null && clc.remove(e)) {
			if(e instanceof IChildEntity) {
				((IChildEntity) e).setParent(null);
			}
		}
	}

	/**
	 * @param clc of entities (which may be child entities)
	 */
	protected final static <E extends IEntity> void clearEntityCollection(Collection<E> clc) {
		if(clc == null || clc.size() < 1) {
			return;
		}
		for(E e : clc) {
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
	public EntityBase(Integer id) {
		this();
		setId(id);
	}

	@Id
	@NotNull
	@Column(name = IEntity.PK_FIELDNAME)
	@GeneratedValue(generator = "entity")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public boolean isGenerated() {
		return generated;
	}

	/**
	 * This method <b>must only</b> be called when a new entity is created and
	 * the id is generated. It will set the id and set the generated flag to true.
	 * @param id the id to set
	 */
	public void setGenerated(Integer id) {
		setId(id);
		generated = true;
	}

	/**
	 * @return the version
	 */
	@Version
	@Column(name = "version")
	@Managed
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o == null || !(o instanceof IEntity)) {
			return false;
		}
		IEntity entity = (IEntity) o;
		final IPrimaryKey<? extends IEntity> pk = KeyFactory.getPrimaryKey(this);
		return (pk != null && pk.equals(KeyFactory.getPrimaryKey(entity)));
	}

	@Override
	public int hashCode() {
		final IPrimaryKey<? extends IEntity> pk = KeyFactory.getPrimaryKey(this);
		return pk == null ? 0 : pk.hashCode();
	}

	protected ToStringBuilder toStringBuilder() {
		return new ToStringBuilder(this).append(IEntity.PK_FIELDNAME, getId()).append("version", getVersion());
	}

	@Override
	public String toString() {
		return toStringBuilder().toString();
	}

	@Transient
	public boolean isNew() {
		return (getVersion() == null);
	}

	@Transient
	public final String typeName() {
		return EntityUtil.typeName(entityClass());
	}

	/*
	 * May be overridden by sub-classes for a better descriptor.
	 */
	@Transient
	public String descriptor() {
		return typeName() + " (Id: " + getId() + ")";
	}
}
