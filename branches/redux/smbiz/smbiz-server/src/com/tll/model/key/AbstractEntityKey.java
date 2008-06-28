package com.tll.model.key;

import com.tll.key.IKey;
import com.tll.model.EntityType;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;

/**
 * Abstract key class for entity related keys.
 * @param <E> The entity type.
 * @author jpk
 */
public abstract class AbstractEntityKey<E extends IEntity> implements IKey<E> {

	private static final long serialVersionUID = -1282833100494542743L;
	/**
	 * The entity type.
	 */
	protected Class<E> entityClass;

	/**
	 * Constructor
	 * @param entityClass The key type
	 */
	protected AbstractEntityKey(Class<E> entityClass) {
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

	/**
	 * Is the given class compatible with this key type? Used in
	 * {@link #compareTo(IKey)} for instance.
	 * @param type The type to compare to this key's type.
	 * @return <code>true</code> if compatible.
	 */
	protected final boolean typeCompatible(Class<? extends IEntity> type) {
		final Class<? extends IEntity> thisType = getType();
		return (type == null || thisType == null) ? false : thisType.isAssignableFrom(type)
		/*|| type.isAssignableFrom(thisType)*/;
	}

	@Override
	public final String descriptor() {
		return getEntityType().getName() + " " + keyDescriptor();
	}

	/**
	 * @return The key specific descriptor declaring the constituent field names.
	 *         Used to provide the descriptor string.
	 * @see #descriptor()
	 */
	protected abstract String keyDescriptor();
}
