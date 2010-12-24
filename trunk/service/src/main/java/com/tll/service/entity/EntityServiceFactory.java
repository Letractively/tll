package com.tll.service.entity;

import java.util.Map;

import com.tll.model.IEntity;
import com.tll.service.IService;

/**
 * Factory that provides refs to all loaded entity services in the app.
 * 
 * @author jpk
 */
public final class EntityServiceFactory implements IEntityServiceFactory {
	final Map<Class<? extends IService>, IService> map;

	/**
	 * Constructor
	 * @param map
	 */
	public EntityServiceFactory(Map<Class<? extends IService>, IService> map) {
		super();
		this.map = map;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends IService> S instance(Class<S> type) {
		final S s = (S) map.get(type);
		if(s == null) {
			throw new IllegalArgumentException("Entity Service of type: " + type + " not found.");
		}
		return s;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> IEntityService<E> instanceByEntityType(Class<E> entityType) {
		for(final IService es : map.values()) {
			if(es instanceof IEntityService) {
				if(((IEntityService<? extends IEntity>) es).getEntityClass().isAssignableFrom(entityType)) {
					return (IEntityService<E>) es;
				}
			}
		}
		throw new IllegalArgumentException("Entity Service for entity of type: " + entityType + " not found.");
	}
}
