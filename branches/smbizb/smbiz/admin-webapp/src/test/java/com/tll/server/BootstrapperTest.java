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

import com.tll.server.rpc.entity.MEntityContext;

/**
 * BootstrapperTest
 * @author jpk
 */
@Test(groups = {
	"server",
	"bootstrap" })
public class BootstrapperTest {
	
	private static final Log log = LogFactory.getLog(BootstrapperTest.class);
	
	private String daoMode;
	private boolean employSecurity;
	
	@BeforeTest(alwaysRun = true)
	@Parameters(value = {
		"daoMode", "employSecurity" })
	public void beforeTest(String daoModeStr, String employSecurity) {

		// handle the dao mode
		this.daoMode = daoModeStr;
		log.debug("DaoMode: " + daoMode);

		// handle security mode
		this.employSecurity = Boolean.valueOf(employSecurity).booleanValue();
		log.debug("Employ security: " + this.employSecurity);
	}

	private ServletContext getMockServletContext() {
		assert daoMode != null;

		StringBuilder sb = new StringBuilder();
		sb.append("com.tll.di.VelocityModule\r\n");
		sb.append("com.tll.di.MailModule\r\n");
		sb.append("com.tll.di.RefDataModule\r\n");
		sb.append("com.tll.di.ModelModule\r\n");
		sb.append("com.tll.di.MockEntityFactoryModule\r\n");
		sb.append("ORM".equals(daoMode) ? "com.tll.di.OrmDaoModule\r\n" : "com.tll.di.MockDaoModule\r\n");
		sb.append("com.tll.di.EntityAssemblerModule\r\n");
		sb.append("com.tll.di.EntityServiceFactoryModule\r\n");
		if(employSecurity) sb.append("com.tll.di.AcegiModule\r\n");

		final MockServletContext context = new MockServletContext();
		context.addInitParameter(Bootstrapper.DEPENDENCY_MODULE_CLASS_NAMES, sb.toString());
		
		sb.setLength(0);
		sb.append("com.tll.server.rpc.entity.MEntityServiceBootstrapper\r\n");
		if(employSecurity) sb.append("com.tll.server.SecurityContextBootstrapper\r\n");
		sb.append("com.tll.server.AppContextBootstrapper\r\n");
		context.addInitParameter(Bootstrapper.DEPENDENCY_HANDLER_CLASS_NAMES, sb.toString());

		return context;
	}

	@Test
	public void test() throws Exception {
		final ServletContext context = getMockServletContext();
		final ServletContextEvent event = new ServletContextEvent(context);
		final Bootstrapper bootstraper = new Bootstrapper();
		bootstraper.contextInitialized(event);
		final MEntityContext mec = (MEntityContext) context.getAttribute(MEntityContext.KEY);
		final SecurityContext sc = (SecurityContext) context.getAttribute(SecurityContext.KEY);
		final AppContext ac = (AppContext) context.getAttribute(AppContext.KEY);
		Assert.assertNotNull(mec);
		Assert.assertNotNull(ac);
		if(employSecurity) Assert.assertNotNull(sc);
	}
}
