/**
 * The Logic Lab
 * @author jpk
 * @since Mar 14, 2010
 */
package com.tll.model;

import com.tll.IDescriptorProvider;
import com.tll.ITypeDescriptorProvider;
import com.tll.schema.Extended;
import com.tll.schema.Root;

/**
 * Default {@link IEntityMetadata} impl.
 * @author jpk
 */
public class SimpleEntityMetadata implements IEntityMetadata {

	@Override
	public Class<?> getEntityClass(Object entity) {
		return entity == null ? null : entity.getClass();
	}

	@Override
	public String getEntityInstanceDescriptor(Object entity) {
		if(entity == null) return null;
		if(entity instanceof IDescriptorProvider) return ((IDescriptorProvider) entity).descriptor();
		return entity.toString();
	}

	@Override
	public String getEntityTypeDescriptor(Object entity) {
		if(entity == null) return null;
		if(entity instanceof ITypeDescriptorProvider) return ((ITypeDescriptorProvider) entity).typeDesc();
		return entity.getClass().getSimpleName();
	}

	@Override
	public Class<?> getRootEntityClass(Class<?> entityClass) {
		if(entityClass.getAnnotation(Extended.class) != null) {
			Class<?> ec = entityClass;
			do {
				ec = ec.getSuperclass();
			} while(ec != null && ec.getAnnotation(Root.class) == null);
			if(ec != null) return ec;
		}
		return entityClass;
	}

	@Override
	public boolean isEntityType(Class<?> claz) {
		return claz.getAnnotation(Root.class) != null || claz.getAnnotation(Extended.class) != null;
	}

}
