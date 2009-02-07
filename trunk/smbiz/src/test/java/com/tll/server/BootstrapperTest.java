/**
 * The Logic Lab
 */
package com.tll.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mock.web.MockServletContext;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.tll.config.Config;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.di.DaoModule;
import com.tll.di.JpaModule;
import com.tll.di.SecurityModule;
import com.tll.util.EnumUtil;

/**
 * BootstrapperTest
 * @author jpk
 */
@Test(groups = {
	"server",
	"bootstrap" })
public class BootstrapperTest {
	
	private static final Log log = LogFactory.getLog(BootstrapperTest.class);
	
	@BeforeTest(alwaysRun = true)
	@Parameters(value = {
		"daoMode", "securityMode" })
	public void beforeTest(String daoModeStr, String securityModeStr) {

		// handle the dao mode
		DaoMode daoMode = EnumUtil.fromString(DaoMode.class, daoModeStr);
		JpaMode jpaMode;
		if(daoMode == DaoMode.MOCK) {
			jpaMode = JpaMode.NONE;
		}
		else if(daoMode == DaoMode.ORM) {
			jpaMode = JpaMode.SPRING;
		}
		else {
			throw new IllegalStateException("Unhandled dao mode: " + daoModeStr);
		}
		Config.instance().setProperty(JpaModule.ConfigKeys.JPA_MODE_PARAM.getKey(), jpaMode.toString());
		Config.instance().setProperty(DaoModule.ConfigKeys.DAO_MODE_PARAM.getKey(), daoMode.toString());
		log.debug("DaoMode: " + daoMode + " (JpaMode: " + jpaMode + ")");

		// handle security mode
		SecurityMode securityMode = EnumUtil.fromString(SecurityMode.class, securityModeStr);
		Config.instance().setProperty(SecurityModule.ConfigKeys.SECURITY_MODE_PARAM.getKey(), securityMode.toString());
		log.debug("SecurityMode: " + securityMode);
	}

	private ServletContext getMockServletContext() {
		MockServletContext context = new MockServletContext();
		context.addInitParameter(Bootstrapper.DEPENDENCY_MODULE_CLASS_NAMES, 
				"com.tll.di.VelocityModule \r\n"
				+ "com.tll.di.MailModule \r\n" 
				+ "com.tll.di.RefDataModule \r\n"
				+ "com.tll.di.ModelModule \r\n"
				+ "com.tll.di.MockEntityFactoryModule \r\n" 
				+ "com.tll.di.JpaModule \r\n" 
				+ "com.tll.di.DaoModule \r\n"
				+ "com.tll.di.EntityServiceModule \r\n" 
				+ "com.tll.di.EntityServiceFactoryModule \r\n" 
				+ "com.tll.di.SecurityModule \r\n");
		
		context.addInitParameter(Bootstrapper.DEPENDENCY_HANDLER_CLASS_NAMES, 
				"com.tll.server.AppContextHandler \r\n"
				+ "com.tll.server.SecurityContextHandler \r\n");

		return context;
	}

	@Test
	public void test() throws Exception {
		ServletContext context = getMockServletContext();
		ServletContextEvent event = new ServletContextEvent(context);
		Bootstrapper bootstraper = new Bootstrapper();
		bootstraper.contextInitialized(event);
		IAppContext ac = (IAppContext) context.getAttribute(IAppContext.SERVLET_CONTEXT_KEY);
		ISecurityContext sc = (ISecurityContext) context.getAttribute(ISecurityContext.SERVLET_CONTEXT_KEY);
		Assert.assertNotNull(ac);
		Assert.assertNotNull(sc);
	}
}
