/**
 * The Logic Lab
 */
package com.tll.di;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * SecurityModuleTest
 * @author jpk
 */
@Test(groups = {
	"bootstrap", "security" })
public class SecurityModuleTest {

	@Test
	public void testLoad() throws Exception {
		final DaoModule dm = new DaoModule();
		final EntityServiceImplModule esm = new EntityServiceImplModule();
		final SecurityModule sm = new SecurityModule();
		final Injector injector = Guice.createInjector(dm, esm, sm);
		assert injector != null;
	}
}
