package com.tll.dao.db4o;


import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.di.Db4oDaoModule;
import com.tll.di.EGraphModule;
import com.tll.di.ModelModule;
import com.tll.di.test.Db4oDbTestModule;
import com.tll.model.IEntity;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractMockEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "db4o" })
public abstract class AbstractDb4oEntityDaoTest extends AbstractEntityDaoTest {

	/**
	 * Constructor
	 */
	public AbstractDb4oEntityDaoTest() {
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
		modules.add(new Db4oDaoModule());
		modules.add(new Db4oDbTestModule());
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
