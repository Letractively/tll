package com.tll.dao.db4o;

import java.util.List;

import org.testng.annotations.Test;

import com.db4o.ObjectContainer;
import com.db4o.ext.ExtObjectContainer;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.dao.IDbTrans;
import com.tll.dao.db4o.test.Db4oTestDaoDecorator;
import com.tll.dao.db4o.test.Db4oTrans;
import com.tll.di.Db4oDaoModule;
import com.tll.di.Db4oDbShellModule;
import com.tll.di.EGraphModule;
import com.tll.di.ModelModule;
import com.tll.di.test.TestDb4oDaoModule;
import com.tll.model.IEntity;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractMockEntityDaoTest
 * @author jpk
 */
@Test(groups = {
	"dao", "db4o" })
	public abstract class AbstractDb4oEntityDaoTest extends AbstractEntityDaoTest<Db4oTestDaoDecorator> {

	/**
	 * Constructor
	 */
	public AbstractDb4oEntityDaoTest() {
		super(Db4oTestDaoDecorator.class);
	}

	@Override
	protected void doBeforeClass() {
		getConfig().setProperty(Db4oDaoModule.ConfigKeys.DB4O_EMPLOY_SPRING_TRANSACTIONS.getKey(), false);
		super.doBeforeClass();
		dao.setDb4oSession((ExtObjectContainer) injector.getInstance(ObjectContainer.class));
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
		getConfig().setProperty(Db4oDaoModule.ConfigKeys.DB4O_EMPLOY_SPRING_TRANSACTIONS.getKey(), false);
		modules.add(new TestDb4oDaoModule(getConfig()));
		modules.add(new Db4oDbShellModule());
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				// IDbTrans
				binder.bind(IDbTrans.class).to(Db4oTrans.class).in(Scopes.SINGLETON);
			}
		});
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
