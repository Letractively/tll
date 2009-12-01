package com.tll.dao.db4o;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbShell;
import com.tll.di.AbstractDb4oDaoModule.Db4oFile;
import com.tll.di.test.Db4oDbShellModule;
import com.tll.di.test.TestDb4oDaoModule;
import com.tll.di.test.TestPersistenceUnitModule;

/**
 * Db4oDbShellTest
 * @author jpk
 */
@Test(groups = { "dao", "db4o" } )
public class Db4oDbShellTest extends AbstractDbAwareTest {

	/**
	 * Constructor
	 */
	public Db4oDbShellTest() {
		super();
		// kill the existing db4o file if present
		final Config cfg = Config.load();
		final Injector i = buildInjector(new TestPersistenceUnitModule(), new TestDb4oDaoModule(cfg));
		final File f = new File(i.getInstance(Key.get(URI.class, Db4oFile.class)));
		f.delete();
	}

	@Override
	@BeforeClass
	protected void beforeClass() {
		super.beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new TestPersistenceUnitModule());
		modules.add(new TestDb4oDaoModule(getConfig()));
		modules.add(new Db4oDbShellModule());
	}

	public void test() throws Exception {
		final IDbShell db = injector.getInstance(IDbShell.class);
		try {
			Assert.assertTrue(db instanceof Db4oDbShell);

			Assert.assertTrue(db.create());
			Assert.assertTrue(db.stub());
			Assert.assertTrue(db.delete());

			db.restub();
		}
		finally {
			// reset
			Assert.assertTrue(db.delete());
		}
	}
}
