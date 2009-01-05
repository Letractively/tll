package com.tll.dao;

import java.util.Map;

import com.tll.SystemError;
import com.tll.model.IEntity;

/**
 * Factory that provides refs to all loaded daos in the app.
 * @author jpk
 */
public final class DaoFactory implements IDaoFactory {

	final Map<Class<? extends IDao>, IDao> map;

	/**
	 * Constructor
	 * @param map
	 */
	public DaoFactory(Map<Class<? extends IDao>, IDao> map) {
		super();
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public <D extends IDao> D instance(Class<D> type) {
		final D instnce = (D) map.get(type);
		if(instnce == null) {
			throw new SystemError("Dao of type: " + type + " not found.");
		}
		return instnce;
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> IEntityDao<E> instanceByEntityType(Class<E> entityType) {
		for(final IDao d : map.values()) {
			if(d instanceof IEntityDao) {
				if(((IEntityDao) d).getEntityClass().isAssignableFrom(entityType)) {
					return (IEntityDao<E>) d;
				}
			}
		}
		throw new SystemError("Entity Dao for entity of type: " + entityType + " not found.");
	}
}
