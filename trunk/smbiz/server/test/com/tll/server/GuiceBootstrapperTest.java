/**
 * The Logic Lab
 */
package com.tll.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.mock.web.MockServletContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.inject.Injector;
import com.tll.TestBase;

/**
 * GuiceBootstrapperTest
 * @author jpk
 */
@Test(groups = {
	"server",
	"bootstrap" })
public class GuiceBootstrapperTest extends TestBase {

	private ServletContext getMockServletContext() {
		MockServletContext context = new MockServletContext();
		context.addInitParameter(Constants.GUICE_MODULE_CLASS_NAMES, 
				"com.tll.guice.VelocityModule \r\n"
				+ "com.tll.guice.MailModule \r\n" 
				+ "com.tll.guice.AppRefDataModule \r\n"
				+ "com.tll.guice.MockEntitiesModule \r\n" 
				+ "com.tll.guice.JpaModule \r\n" 
				+ "com.tll.guice.DaoModule \r\n"
				+ "com.tll.guice.EntityServiceModule \r\n" 
				+ "com.tll.guice.SecurityModule \r\n");
		return context;
	}

	@Test
	public void test() throws Exception {
		ServletContext context = getMockServletContext();
		GuiceBootstrapper gb = new GuiceBootstrapper();
		ServletContextEvent event = new ServletContextEvent(context);
		gb.contextInitialized(event);
		Injector injector = (Injector) context.getAttribute(Constants.GUICE_INJECTOR_CONTEXT_ATTRIBUTE);
		Assert.assertNotNull(injector);
	}
}
