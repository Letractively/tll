package com.tll.service.entity;

import java.util.Map;

import com.tll.SystemError;
import com.tll.model.IEntity;

/**
 * Factory that provides refs to all loaded entity services in the app.
 * 
 * @author jpk
 */
public final class EntityServiceFactory implements IEntityServiceFactory {
  final Map<Class<? extends IEntityService<? extends IEntity>>, IEntityService<? extends IEntity>> map;

  /**
   * Constructor
   * @param map
   */
  public EntityServiceFactory(Map<Class<? extends IEntityService<? extends IEntity>>, IEntityService<? extends IEntity>> map) {
    super();
    this.map = map;
  }

  @SuppressWarnings("unchecked")
  public <S extends IEntityService<? extends IEntity>> S instance(Class<S> type) {
    S s = (S) map.get(type);
    if(s == null) {
      throw new SystemError("Entity Service of type: " + type + " not found.");
    }
    return s;
  }

  @SuppressWarnings("unchecked")
  public <E extends IEntity> IEntityService<E> instanceByEntityType(Class<E> entityType) {
    for(IEntityService<? extends IEntity> es : map.values()) {
    	if(es.getEntityClass().isAssignableFrom(entityType)) {
    		return (IEntityService<E>) es;
    	}
    }
    throw new SystemError("Entity Service for entity of type: " + entityType + " not found.");
  }
}
