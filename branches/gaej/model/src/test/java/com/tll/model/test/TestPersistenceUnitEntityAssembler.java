package com.tll.model.test;

import com.google.inject.Inject;
import com.tll.model.IEntity;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityProvider;

/**
 * TestPersistenceUnitEntityAssembler - Decorator around the
 * {@link IEntityFactory} with additional build functionality specific to the
 * entity type.
 * @author jpk
 */
public final class TestPersistenceUnitEntityAssembler implements IEntityFactory, IEntityAssembler {

	/**
	 * The decorated entity factory.
	 */
	private final IEntityFactory entityFactory;

	/**
	 * Constructor
	 * @param entityFactory
	 */
	@Inject
	public TestPersistenceUnitEntityAssembler(IEntityFactory entityFactory) {
		super();
		this.entityFactory = entityFactory;
	}

	public <E extends IEntity> E createEntity(Class<E> entityClass) {
		return entityFactory.createEntity(entityClass);
	}

	public <E extends IEntity> void assignPrimaryKey(E entity) {
		entityFactory.assignPrimaryKey(entity);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> E assembleEntity(Class<E> entityType, IEntityProvider entityProvider, boolean generate) {
		E e = null;
		if(AccountAddress.class.equals(entityType)) {
			final AccountAddress ae = createEntity(AccountAddress.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			Address a = entityProvider == null ? null : entityProvider.getEntityByType(Address.class);
			if(a == null) {
				a = createEntity(Address.class);
				if(generate) assignPrimaryKey(a);
			}
			ae.setAddress(a);
			e = (E) ae;
		}
		else if(Address.class.equals(entityType)) {
			final Address ae = createEntity(Address.class);
			e = (E) ae;
		}
		else if(Account.class.equals(entityType)) {
			final Account ae = createEntity(Account.class);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(NestedEntity.class.equals(entityType)) {
			final NestedEntity ae = createEntity(NestedEntity.class);
			e = (E) ae;
		}

		else
			throw new IllegalArgumentException("Unsupported entity type '" + entityType + "' for assembly");

		if(generate) assignPrimaryKey(e);

		return e;
	}
}