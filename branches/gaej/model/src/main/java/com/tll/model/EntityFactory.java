package com.tll.model;

import com.google.inject.Inject;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.util.StringUtil;

/**
 * EntityFactory - This class generates the ids for the entities that are
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
	 * @param keyGenerator Required primary key generator impl
	 */
	@Inject
	public EntityFactory(IPrimaryKeyGenerator keyGenerator) {
		super();
		if(keyGenerator == null) throw new IllegalArgumentException("No primary key generator specified.");
		this.keyGenerator = keyGenerator;
	}

	public <E extends IEntity> E createEntity(Class<E> entityClass) {
		E entity;
		try {
			entity = entityClass.newInstance();
		}
		catch(final IllegalAccessException iae) {
			throw new IllegalStateException(StringUtil.replaceVariables(
					"Could not access default constructor for entity type: '%1'.", entityClass.getName()), iae);
		}
		catch(final InstantiationException ie) {
			throw new IllegalStateException(StringUtil.replaceVariables("Unable to instantiate the entity: %1", entityClass
					.getName()), ie);
		}
		return entity;
	}

	public <E extends IEntity> void assignPrimaryKey(E entity) {
		((EntityBase) entity).setGenerated(keyGenerator.generateIdentifier(entity));
	}
}
