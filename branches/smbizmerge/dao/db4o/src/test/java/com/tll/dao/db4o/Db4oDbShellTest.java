package com.tll.dao.db4o;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbShell;
import com.tll.dao.db4o.AbstractDb4oDaoModule.Db4oFile;
import com.tll.dao.db4o.test.Db4oDbShellModule;
import com.tll.dao.db4o.test.TestDb4oDaoModule;
import com.tll.model.EntityMetadata;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityMetadata;
import com.tll.model.egraph.EGraphModule;
import com.tll.model.test.TestEntityFactory;
import com.tll.model.test.TestPersistenceUnitEntityGraphBuilder;

/**
 * Db4oDbShellTest
 * @author jpk
 */
@Test(groups = {
	"dao", "db4o" })
public class Db4oDbShellTest extends AbstractDbAwareTest {

	/**
	 * Constructor
	 */
	public Db4oDbShellTest() {
		super();
		// kill the existing db4o file if present
		final Config cfg = Config.load();
		final Injector i = buildInjector(new TestDb4oDaoModule(cfg));
		final File f = new File(i.getInstance(Key.get(URI.class, Db4oFile.class)));
		f.delete();
	}

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new EGraphModule(TestPersistenceUnitEntityGraphBuilder.class));
		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IEntityMetadata.class).to(EntityMetadata.class);
				bind(IEntityFactory.class).to(TestEntityFactory.class);
				bind(URI.class).annotatedWith(Db4oFile.class).toInstance(AbstractDb4oDaoModule.getDb4oFileRef("target/testshelldb"));
			}
		});
		modules.add(new Db4oDbShellModule());
	}

	public void test() throws Exception {
		final IDbShell db = injector.getInstance(IDbShell.class);
		Assert.assertTrue(db instanceof Db4oDbShell);
		db.create();
		db.addData();
		db.drop();
	}
}
