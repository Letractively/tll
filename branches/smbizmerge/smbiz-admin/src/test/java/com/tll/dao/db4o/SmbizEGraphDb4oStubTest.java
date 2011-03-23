/**
 * The Logic Lab
 * @author jpk
 * @since Mar 23, 2011
 */
package com.tll.dao.db4o;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.testng.annotations.Test;

import com.db4o.EmbeddedObjectContainer;
import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.db4o.test.Db4oDbShellModule;
import com.tll.model.SmbizEGraphModule;

/**
 * Tests the creation of the db4o db file from the smbiz entity graph builder
 * and subsequent access to the entities created therein.
 * @author jkirton
 */
@Test(groups = {
	"dao", "db4o", "model" })
public class SmbizEGraphDb4oStubTest extends AbstractDbAwareTest {

	@Override
	protected Config doGetConfig() {
		Config c = Config.load(new ConfigRef("db4o-config.properties"));
		c.setProperty("db.transaction.bindToSpringAtTransactional", Boolean.FALSE);
		return c;
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new SmbizDb4oDaoModule(getConfig())); 
		modules.add(new SmbizEGraphModule());
		modules.add(new Db4oDbShellModule());
	}

	@Override
	protected void beforeClass() {
		// kill existing test db4o db file if present
		File f = new File(".");	// NOTE: this is presumed to point to the root project dir!
		String rootDir;
		try {
			rootDir = f.getCanonicalPath();
		}
		catch(IOException e) {
			throw new Error(e);
		}
		String dbPath = rootDir + "/target/testdb";
		f = new File(dbPath);
		f.delete();

		super.beforeClass();
	}
	
	@Override
	protected void afterClass() {
		super.afterClass();
		// kill db4o's object container
		injector.getInstance(EmbeddedObjectContainer.class).close();
	}

	public void test() {
		// stub the db (presumes the db file does NOT exist)
		Db4oDbShell dbShell = (Db4oDbShell) getDbShell();
		dbShell.create();
		dbShell.addData();

		// subsequent access test
		// TODO finish
	}
}
