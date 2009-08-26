/**
 * The Logic Lab
 * @author jpk
 * @since Jul 6, 2009
 */
package com.tll.db;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.AbstractInjectedTest;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.di.EGraphModule;
import com.tll.di.ModelModule;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityGraphBuilder;
import com.tll.model.TestPersistenceUnitEntityAssembler;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.SimplePrimaryKeyGenerator;


/**
 * Db4oShellTest
 * @author jpk
 */
@Test(groups = "db")
public class Db4oShellTest extends AbstractInjectedTest {

	@Override
	@BeforeClass
	protected void beforeClass() {
		super.beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		final Config c = Config.load(new ConfigRef("config-db4o.properties"));
		modules.add(new ModelModule() {

			@Override
			protected void bindPrimaryKeyGenerator() {
				bind(IPrimaryKeyGenerator.class).to(SimplePrimaryKeyGenerator.class).in(Scopes.SINGLETON);
			}

			@Override
			protected void bindEntityAssembler() {
				bind(IEntityAssembler.class).to(TestPersistenceUnitEntityAssembler.class).in(Scopes.SINGLETON);
			}

		});
		modules.add(new EGraphModule() {

			@Override
			protected void bindEntityGraphBuilder() {
				bind(IEntityGraphBuilder.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new Module() {

			@Override
			public void configure(Binder b) {
				b.bind(IDbShell.class).toProvider(new Provider<IDbShell>() {

					@Inject
					IEntityGraphBuilder egb;

					@Override
					public IDbShell get() {
						try {
							return DbShellBuilder.getDbShell(c, egb);
						}
						catch(final Exception e) {
							throw new RuntimeException(e);
						}
					}
				}).in(Scopes.SINGLETON);
			}
		});
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
