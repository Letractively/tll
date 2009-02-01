/**
 * The Logic Lab
 * @author jpk
 * Jan 27, 2009
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.INamedEntity;
import com.tll.model.mock.MockEntityFactory;

/**
 * AbstractEntityDaoTestHandler
 * @param <E> The entity type
 * @author jpk
 */
public abstract class AbstractEntityDaoTestHandler<E extends IEntity> implements IEntityDaoTestHandler<E> {

	protected IEntityDao entityDao;
	protected IEntityFactory entityFactory;
	protected MockEntityFactory mockEntityFactory;

	@Override
	public final void init(IEntityDao entityDao, IEntityFactory entityFactory, MockEntityFactory mockEntityFactory) {
		if(entityDao == null || entityFactory == null || mockEntityFactory == null) {
			throw new IllegalStateException("Bad init arg(s)");
		}
		this.entityDao = entityDao;
		this.entityFactory = entityFactory;
		this.mockEntityFactory = mockEntityFactory;
	}

	@Override
	public boolean supportsPaging() {
		return true; // true by default
	}

	@Override
	public void makeUnique(E e) {
		// base impl no-op
	}

	@Override
	public void teardownTestEntity(E e) {
		// base impl no-op
	}

	@Override
	public void verifyLoadedEntityState(E e) throws Exception {
		if(e instanceof INamedEntity) {
			Assert.assertNotNull(((INamedEntity) e).getName(), "The name property is null");
		}
	}

	@Override
	public void alterTestEntity(E e) {
		if(e instanceof INamedEntity) {
			((INamedEntity) e).setName("altered");
		}
	}

	@Override
	public void verifyEntityAlteration(E e) throws Exception {
		if(e instanceof INamedEntity) {
			Assert.assertTrue("altered".equals(((INamedEntity) e).getName()), "Named entity alteration does not match");
		}
	}

}
