package com.tll.service.entity;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityProvider;
import com.tll.model.NestedEntity;

/**
 * MockEntityAssembler - Decorator around the {@link IEntityFactory} with additional
 * build functionality specific to the entity type.
 * @author jpk
 */
public final class MockEntityAssembler implements IEntityFactory, IEntityAssembler {

	/**
	 * The decorated entity factory.
	 */
	private final IEntityFactory entityFactory;

	/**
	 * Constructor
	 * @param entityFactory
	 */
	@Inject
	public MockEntityAssembler(IEntityFactory entityFactory) {
		super();
		this.entityFactory = entityFactory;
	}

	public <E extends IEntity> E createEntity(Class<E> entityClass, boolean generate) {
		return entityFactory.createEntity(entityClass, generate);
	}

	public <E extends IEntity> void setGenerated(E entity) {
		entityFactory.setGenerated(entity);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> E assembleEntity(Class<E> entityType, IEntityProvider entityProvider, boolean generate) {
		E e = null;
		if(AccountAddress.class.equals(entityType)) {
			final AccountAddress ae = createEntity(AccountAddress.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			Address a = entityProvider == null ? null : entityProvider.getEntityByType(Address.class);
			if(a == null) {
				a = createEntity(Address.class, generate);
			}
			ae.setAddress(a);
			e = (E) ae;
		}
		else if(Address.class.equals(entityType)) {
			final Address ae = createEntity(Address.class, generate);
			e = (E) ae;
		}
		else if(Account.class.equals(entityType)) {
			final Account ae = createEntity(Account.class, generate);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(NestedEntity.class.equals(entityType)) {
			final NestedEntity ae = createEntity(NestedEntity.class, generate);
			e = (E) ae;
		}

		else
			throw new SystemError("Unsupported entity type '" + entityType + "' for assembly");
		return e;
	}
}