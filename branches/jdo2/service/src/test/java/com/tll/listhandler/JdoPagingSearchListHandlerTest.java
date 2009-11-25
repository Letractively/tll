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
import com.tll.di.test.JdbcDbShellModule;
import com.tll.di.test.TestJdoDaoModule;


/**
 * JdoPagingSearchListHandlerTest
 * @author jpk
 */
@Test(groups = {
	"dao", "jdo"
})
public class JdoPagingSearchListHandlerTest extends AbstractPagingSearchListHandlerTest {

	@Override
	protected Config doGetConfig() {
		return Config.load(new ConfigRef("jdo-config.properties"));
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new TestJdoDaoModule(getConfig()));
		modules.add(new JdbcDbShellModule(getConfig()));
	}

	@Override
	protected void beforeClass() {
		// reset test db
		final Config cfg = getConfig();
		final Injector i = buildInjector(new JdbcDbShellModule(cfg) );
		final IDbShell dbs = i.getInstance(IDbShell.class);
		dbs.create();
		dbs.clear();
		
		super.beforeClass();
	}
}
