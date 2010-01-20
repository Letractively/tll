package com.tll.dao.jdo;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbShell;
import com.tll.di.test.JdbcDbShellModule;
import com.tll.di.test.TestPersistenceUnitModule;

/**
 * JdbcDbShellTest
 * @author jpk
 */
@Test(groups = { "dao", "jdo" } )
public class JdbcDbShellTest extends AbstractDbAwareTest {

	@Override
	@BeforeClass
	protected void beforeClass() {
		super.beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new TestPersistenceUnitModule());
		modules.add(new JdbcDbShellModule(getConfig()));
	}

	public void test() throws Exception {
		final IDbShell db = injector.getInstance(IDbShell.class);
		try {
			Assert.assertTrue(db instanceof JdbcDbShell);

			db.create();
			db.clearData();
			db.addData();
			db.drop();
		}
		finally {
			// reset
			try {
				db.clearData();
			}
			catch(final Exception ex) {
				// ok
			}
		}
	}
}
