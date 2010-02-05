/**
 * The Logic Lab
 * @author jpk
 * @since Oct 31, 2009
 */
package com.tll.listhandler;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.IDbShell;
import com.tll.di.JdoRdbmsDaoModule;
import com.tll.di.test.JdbcDbShellModule;
import com.tll.di.test.JdoTransModule;


/**
 * JdoPagingSearchListHandlerTest
 * @author jpk
 */
@Test(groups = {
	"listhandler", "jdo"
})
public class JdoPagingSearchListHandlerTest extends AbstractPagingSearchListHandlerTest {

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new JdoRdbmsDaoModule(getConfig()));
		modules.add(new JdoTransModule());
		modules.add(new JdbcDbShellModule(getConfig()));
	}

	@Override
	protected void beforeClass() {
		// reset test db
		final Config cfg = getConfig();
		final Injector i = buildInjector(new JdbcDbShellModule(cfg) );
		final IDbShell dbs = i.getInstance(IDbShell.class);
		dbs.create();
		dbs.clearData();

		super.beforeClass();
	}
}
