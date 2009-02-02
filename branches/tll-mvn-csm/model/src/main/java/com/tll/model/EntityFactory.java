package com.tll.model;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * The entity factory. This class generates the ids for the entities that are
 * created for insert into the persistence store.
 * @author jpk
 */
public final class EntityFactory implements IEntityFactory {

	/**
	 * The primary key generator.
	 */
	private final IPrimaryKeyGenerator keyGenerator;

	/**
	 * Constructor
	 * @param keyGenerator
	 */
	@Inject
	public EntityFactory(IPrimaryKeyGenerator keyGenerator) {
		super();
		this.keyGenerator = keyGenerator;
	}

	public <E extends IEntity> E createEntity(Class<E> entityClass, boolean generate) {
		E entity;
		try {
			entity = entityClass.newInstance();
		}
		catch(IllegalAccessException iae) {
			throw new SystemError("Could not access %1 constructor -- make sure it is public", entityClass.getName(), iae);
		}
		catch(InstantiationException ie) {
			throw new SystemError("Unable to instantiate the entity: %1", entityClass.getName(), ie);
		}
		if(generate) setGenerated(entity);
		return entity;
	}

	public <E extends IEntity> void setGenerated(E entity) {
		((EntityBase) entity).setGenerated(keyGenerator.generateIdentifier(entity.entityClass()));
	}
}
