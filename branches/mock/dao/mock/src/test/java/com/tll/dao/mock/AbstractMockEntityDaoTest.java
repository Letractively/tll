package com.tll.dao.mock;


import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.di.EGraphModule;
import com.tll.di.MockDaoModule;
import com.tll.di.ModelModule;
import com.tll.di.test.MockDbTestModule;
import com.tll.model.IEntity;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractMockEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "mock" })
public abstract class AbstractMockEntityDaoTest extends AbstractEntityDaoTest {

	/**
	 * Constructor
	 */
	public AbstractMockEntityDaoTest() {
		super();
	}

	@Override
	protected final void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new ModelModule() {

			@Override
			protected void bindEntityAssembler() {
				// not-needed
			}
		});
		modules.add(new EGraphModule() {

			@Override
			protected void bindEntityGraphBuilder() {
				bind(IEntityGraphPopulator.class).to(getEntityGraphPopulator()).in(Scopes.SINGLETON);
			}
		});
		modules.add(new MockDaoModule());
		modules.add(new MockDbTestModule());
	}

	@Override
	protected final void doBeforeClass() {
		// no-op
	}

	@Override
	protected final IEntity getEntityFromDb(PrimaryKey<IEntity> key) {
		return dao.load(key);
	}
	
	/**
	 * @return The desired {@link IEntityGraphPopulator} type.
	 */
	protected abstract Class<? extends IEntityGraphPopulator> getEntityGraphPopulator();
}
