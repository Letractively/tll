/**
 * The Logic Lab
 * @author jpk
 * @since Mar 14, 2010
 */
package com.tll.model;

/**
 * EntityMetadata
 * @author jpk
 */
public class EntityMetadata extends SimpleEntityMetadata {

	@Override
	public Class<?> getEntityClass(Object entity) {
		if(entity instanceof IEntity) {
			((IEntity) entity).entityClass();
		}
		return super.getEntityClass(entity);
	}

	@Override
	public String getEntityInstanceDescriptor(Object entity) {
		if(entity instanceof IEntity) {
			return ((IEntity) entity).descriptor(); 
		}
		return super.getEntityInstanceDescriptor(entity);
	}

	@Override
	public String getEntityTypeDescriptor(Object entity) {
		if(entity instanceof IEntity) {
			return ((IEntity) entity).typeDesc(); 
		}
		return super.getEntityTypeDescriptor(entity);
	}

	@Override
	public boolean isEntityType(Class<?> claz) {
		return IEntity.class.isAssignableFrom(claz);
	}

}
