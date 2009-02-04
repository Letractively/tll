/**
 * The Logic Lab
 */
package com.tll.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.mock.web.MockServletContext;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * BootstrapperTest
 * @author jpk
 */
@Test(groups = {
	"server",
	"bootstrap" })
public class BootstrapperTest {

	private ServletContext getMockServletContext() {
		MockServletContext context = new MockServletContext();
		context.addInitParameter(Bootstrapper.DEPENDENCY_MODULE_CLASS_NAMES, 
				"com.tll.di.VelocityModule \r\n"
				+ "com.tll.di.MailModule \r\n" 
				+ "com.tll.di.RefDataModule \r\n"
				+ "com.tll.di.MockEntitiesModule \r\n" 
				+ "com.tll.di.JpaModule \r\n" 
				+ "com.tll.di.DaoModule \r\n"
				+ "com.tll.di.EntityServiceFactoryModule \r\n" 
				+ "com.tll.di.SecurityModule \r\n");
		return context;
	}

	@Test
	public void test() throws Exception {
		ServletContext context = getMockServletContext();
		Bootstrapper gb = new Bootstrapper();
		ServletContextEvent event = new ServletContextEvent(context);
		gb.contextInitialized(event);
		IAppContext ac = (IAppContext) context.getAttribute(IAppContext.SERVLET_CONTEXT_KEY);
		Assert.assertNotNull(ac);
	}
}
