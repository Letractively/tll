/**
 * The Logic Lab
 */
package com.tll.guice;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tll.TestBase;

/**
 * SecurityModuleTest
 * @author jpk
 */
@Test(groups = "security")
public class SecurityModuleTest extends TestBase {

	@Test
	public void testLoad() throws Exception {
		DaoModule dm = new DaoModule();
		EntityServiceModule esm = new EntityServiceModule();
		SecurityModule sm = new SecurityModule();
		Injector injector = Guice.createInjector(dm, esm, sm);
		assert injector != null;
	}
}
