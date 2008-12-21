/**
 * The Logic Lab
 */
package com.tll.di;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tll.TestBase;
import com.tll.di.DaoModule;
import com.tll.di.EntityServiceModule;
import com.tll.di.SecurityModule;

/**
 * SecurityModuleTest
 * @author jpk
 */
@Test(groups = {
	"bootstrap",
	"security" })
public class SecurityModuleTest extends TestBase {

	@Test
	public void testLoad() throws Exception {
		final DaoModule dm = new DaoModule();
		final EntityServiceModule esm = new EntityServiceModule();
		final SecurityModule sm = new SecurityModule();
		final Injector injector = Guice.createInjector(dm, esm, sm);
		assert injector != null;
	}
}
