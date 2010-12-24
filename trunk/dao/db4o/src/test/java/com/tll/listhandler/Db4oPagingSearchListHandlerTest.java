/**
 * The Logic Lab
 * @author jpk
 * @since Oct 31, 2009
 */
package com.tll.listhandler;

import java.util.List;

import org.testng.annotations.Test;

import com.db4o.ObjectContainer;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.dao.IDbShell;
import com.tll.dao.IDbTrans;
import com.tll.dao.db4o.test.Db4oDbShellModule;
import com.tll.dao.db4o.test.Db4oTrans;
import com.tll.dao.db4o.test.TestDb4oDaoModule;


/**
 * Db4oPagingSearchListHandlerTest
 * @author jpk
 */
@Test(groups = {
	"listhandler", "db4o"
})
public class Db4oPagingSearchListHandlerTest extends AbstractPagingSearchListHandlerTest {

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new TestDb4oDaoModule(getConfig()));
		modules.add(new Db4oDbShellModule());
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(IDbTrans.class).to(Db4oTrans.class).in(Scopes.SINGLETON);
			}
		});
	}

	@Override
	protected void beforeClass() {
		// create the db shell first (before test injector creation) to avoid db4o
		// file lock when objectcontainer is instantiated
		final Config cfg = getConfig();
		final Injector i = buildInjector(new TestDb4oDaoModule(cfg), new Db4oDbShellModule() );
		final IDbShell dbs = i.getInstance(IDbShell.class);
		dbs.drop();
		dbs.create();

		super.beforeClass();
	}

	@Override
	protected void afterClass() {
		super.afterClass();
		// kill db4o's object container
		injector.getInstance(ObjectContainer.class).close();
	}

}
