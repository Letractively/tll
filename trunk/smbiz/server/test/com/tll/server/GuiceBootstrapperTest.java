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
				"com.tll.di.VelocityModule \r\n"
				+ "com.tll.di.MailModule \r\n" 
				+ "com.tll.di.AppRefDataModule \r\n"
				+ "com.tll.di.MockEntitiesModule \r\n" 
				+ "com.tll.di.JpaModule \r\n" 
				+ "com.tll.di.DaoModule \r\n"
				+ "com.tll.di.EntityServiceModule \r\n" 
				+ "com.tll.di.SecurityModule \r\n");
		return context;
	}

	@Test
	public void test() throws Exception {
		ServletContext context = getMockServletContext();
		Bootstrapper gb = new Bootstrapper();
		ServletContextEvent event = new ServletContextEvent(context);
		gb.contextInitialized(event);
		Injector injector = (Injector) context.getAttribute(Constants.GUICE_INJECTOR_CONTEXT_ATTRIBUTE);
		Assert.assertNotNull(injector);
	}
}
