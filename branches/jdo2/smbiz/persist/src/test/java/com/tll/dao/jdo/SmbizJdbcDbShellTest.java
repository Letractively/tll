package com.tll.dao.jdo;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbShell;
import com.tll.di.SmbizEGraphModule;
import com.tll.di.SmbizModelModule;
import com.tll.di.test.JdbcDbShellModule;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.SimplePrimaryKeyGenerator;

/**
 * SmbizJdbcDbShellTest
 * @author jpk
 */
@Test(groups = { "dao", "jdo" } )
public class SmbizJdbcDbShellTest extends AbstractDbAwareTest {

	@Override
	@BeforeClass
	protected void beforeClass() {
		super.beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(IPrimaryKeyGenerator.class).to(SimplePrimaryKeyGenerator.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new SmbizModelModule());
		modules.add(new SmbizEGraphModule());
		modules.add(new JdbcDbShellModule(getConfig()));
	}

	public void test() throws Exception {
		final IDbShell db = injector.getInstance(IDbShell.class);
		try {
			Assert.assertTrue(db instanceof JdbcDbShell);

			// NOTE: db.create() will return false if the db already exists
			// so don't require a true return value only that no exception occurrs.
			db.create();

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
