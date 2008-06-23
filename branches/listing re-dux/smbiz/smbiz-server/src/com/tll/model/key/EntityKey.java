package com.tll.model.key;

import com.tll.key.IKey;
import com.tll.model.EntityType;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;

/**
 * Abstract key class for entity related keys.
 * @author jpk
 */
public abstract class EntityKey<E extends IEntity> implements IEntityKey<E> {

	protected Class<E> entityClass;

	/**
	 * Constructor
	 * @param entityClass The key type
	 */
	protected EntityKey(Class<E> entityClass) {
		super();
		setType(entityClass);
	}

	public final Class<E> getType() {
		return entityClass;
	}

	public final EntityType getEntityType() {
		return EntityUtil.entityTypeFromClass(getType());
	}

	public final void setType(Class<E> type) {
		if(type == null) throw new IllegalArgumentException("An entity type must be specified.");
		this.entityClass = type;
	}

	public final String descriptor() {
		return getEntityType().getName() + " " + keyDescriptor();
	}

	/**
	 * Is the given class compatible with this key type? Used in
	 * {@link #compareTo(IKey)} for instance.
	 * @param type The type to compare to this key's type.
	 * @return <code>true</code> if compatible.
	 */
	protected final boolean typeCompatible(Class<? extends IEntity> type) {
		final Class<E> thisType = getType();
		return (type == null || thisType == null) ? false : thisType.isAssignableFrom(type)
				|| type.isAssignableFrom(thisType);
	}

	/**
	 * @return The key specific descriptor declaring the constituent field names.
	 *         Used to provide the descriptor string.
	 * @see #descriptor()
	 */
	protected abstract String keyDescriptor();

	@SuppressWarnings("unchecked")
	public void setEntity(E entity) {
		this.entityClass = (Class<E>) entity.entityClass();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected EntityKey<E> clone() throws CloneNotSupportedException {
		EntityKey<E> cln = (EntityKey) super.clone();
		cln.entityClass = entityClass;
		return cln;
	}

	@Override
	public final IKey<E> copy() {
		try {
			return clone();
		}
		catch(CloneNotSupportedException e) {
			throw new Error("Unable to clone entity key");
		}
	}
}
