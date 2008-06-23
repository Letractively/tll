/**
 * The Logic Lab
 * @author jpk
 * Jun 22, 2008
 */
package com.tll.model.key;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.tll.key.IKey;
import com.tll.model.EntityType;
import com.tll.model.IEntity;

/**
 * ReflectiveBusinessKey - A business key able to perform generic accessor and
 * mutator operations to a target business key.
 * @author jpk
 */
@SuppressWarnings("serial")
public final class ReflectiveBusinessKey<E extends IEntity> implements IBusinessKeyDefinition<E> {

	private final BusinessKey<E> wrapped;

	/**
	 * Constructor
	 * @param wrapped The business key to be wrapped
	 */
	public ReflectiveBusinessKey(BusinessKey<E> wrapped) {
		super();
		if(wrapped == null) throw new IllegalArgumentException("A business key must be specified");
		this.wrapped = wrapped;
	}

	@Override
	public String[] getFieldNames() {
		return wrapped.getFieldNames();
	}

	private String getMethodName(String fieldName, boolean isAccessor) {
		assert fieldName != null && fieldName.length() > 1;
		return (isAccessor ? "get" : "set") + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}

	private Method getMethod(String fieldName, boolean isAccessor) {
		try {
			return wrapped.getClass().getMethod(getMethodName(fieldName, isAccessor), (Class<?>) null);
		}
		catch(SecurityException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		catch(NoSuchMethodException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * Provides a way to generically interrogate a business key used in
	 * conjunction with {@link IBusinessKeyDefinition#getFieldNames()}. Returns
	 * <code>null</code> when the field is not set.
	 * @see IBusinessKeyDefinition#getFieldNames()
	 * @param fieldName
	 * @return the field value
	 * @throws IllegalArgumentException When the given field name does not match
	 *         any of the defined key field names.
	 */
	public Object getFieldValue(String fieldName) throws IllegalArgumentException {
		final Method accessor = getMethod(fieldName, true);
		try {
			return accessor.invoke(wrapped, (Object[]) null);
		}
		catch(IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		catch(InvocationTargetException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * Provision to generically alter the state of the business key
	 * @param fieldName
	 * @param fieldValue
	 * @throws IllegalArgumentException When the given field name does not match
	 *         any of the defined key field names.
	 */
	public void setFieldValue(String fieldName, Object fieldValue) throws IllegalArgumentException {
		final Method accessor = getMethod(fieldName, false);
		try {
			accessor.invoke(wrapped, new Object[] { fieldValue });
		}
		catch(IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		catch(InvocationTargetException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public EntityType getEntityType() {
		return wrapped.getEntityType();
	}

	@Override
	public void clear() {
		wrapped.clear();
	}

	@Override
	public IKey<E> copy() {
		return wrapped.copy();
	}

	@Override
	public Class<E> getType() {
		return wrapped.getType();
	}

	@Override
	public boolean isSet() {
		return wrapped.isSet();
	}

	@Override
	public String descriptor() {
		return wrapped.descriptor();
	}

	@Override
	public int compareTo(IKey<E> o) {
		return wrapped.compareTo(o);
	}
}
