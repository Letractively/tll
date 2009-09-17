package com.tll.dao.db4o;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.AbstractInjectedTest;
import com.tll.dao.IDbShell;
import com.tll.di.Db4oDbShellModule;
import com.tll.di.EGraphModule;
import com.tll.di.ModelModule;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.TestPersistenceUnitEntityAssembler;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.SimplePrimaryKeyGenerator;

/**
 * Db4oDbShellTest
 * @author jpk
 */
@Test(groups = { "dao", "db4o" } )
public class Db4oDbShellTest extends AbstractInjectedTest {

	@Override
	@BeforeClass
	protected void beforeClass() {
		super.beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new ModelModule() {

			@Override
			protected void bindEntityAssembler() {
				bind(IPrimaryKeyGenerator.class).to(SimplePrimaryKeyGenerator.class).in(Scopes.SINGLETON);
				bind(IEntityAssembler.class).to(TestPersistenceUnitEntityAssembler.class).in(Scopes.SINGLETON);
			}

		});
		modules.add(new EGraphModule() {

			@Override
			protected void bindEntityGraphBuilder() {
				bind(IEntityGraphPopulator.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new Db4oDbShellModule());
	}

	public void test() throws Exception {
		final IDbShell db = injector.getInstance(IDbShell.class);
		try {
			Assert.assertTrue(db instanceof Db4oDbShell);

			Assert.assertTrue(db.create());
			Assert.assertTrue(db.stub());
			Assert.assertTrue(db.clear());
			Assert.assertTrue(db.delete());

			db.restub();
		}
		finally {
			// reset
			Assert.assertTrue(db.delete());
		}
	}
}
