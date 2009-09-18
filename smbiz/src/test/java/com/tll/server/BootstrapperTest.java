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
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.tll.server.rpc.entity.PersistContext;

/**
 * BootstrapperTest
 * @author jpk
 */
@Test(groups = { "server", "bootstrap" })
public class BootstrapperTest {

	private static final Log log = LogFactory.getLog(BootstrapperTest.class);

	private String daoImpl;
	private boolean employSecurity;

	@BeforeTest(alwaysRun = true)
	@Parameters(value = {"daoImpl", "employSecurity" })
	public void beforeTest(@Optional String daoImplStr, @Optional String securityImplStr) {

		// handle the dao mode
		this.daoImpl = daoImplStr == null ? "db4o" : daoImplStr;
		log.debug("Dao Impl: " + daoImpl);

		// handle security mode
		this.employSecurity = securityImplStr == null ? false : securityImplStr.equals("acegi");
		log.debug("Employ security: " + this.employSecurity);
	}

	private ServletContext getMockServletContext() {
		final StringBuilder sb = new StringBuilder();
		sb.append("com.tll.di.VelocityModule\r\n");
		sb.append("com.tll.di.MailModule\r\n");
		sb.append("com.tll.di.RefDataModule\r\n");
		sb.append("com.tll.di.SmbizModelModule\r\n");
		sb.append("com.tll.di.SmbizEGraphModule\r\n");
		sb.append("com.tll.di.Db4oDaoModule\r\n");
		sb.append("com.tll.di.EntityServiceFactoryModule\r\n");
		if(employSecurity) sb.append("com.tll.di.SmbizAcegiModule\r\n");
		sb.append("com.tll.di.LogExceptionHandlerModule\r\n");
		sb.append("com.tll.di.SmbizMarshalModule\r\n");
		sb.append("com.tll.di.SmbizClientPersistModule\r\n");
		sb.append("com.tll.di.SmbizListingModule\r\n");
		sb.append("com.tll.di.AppModule\r\n");

		final MockServletContext context = new MockServletContext();
		context.addInitParameter(Bootstrapper.DEPENDENCY_MODULE_CLASS_NAMES, sb.toString());

		sb.setLength(0);
		sb.append("com.tll.server.rpc.entity.PersistContextBootstrapper\r\n");
		sb.append("com.tll.server.rpc.listing.ListingContextBootstrapper\r\n");
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
		final PersistContext mec = (PersistContext) context.getAttribute(PersistContext.KEY);
		final SecurityContext sc = (SecurityContext) context.getAttribute(SecurityContext.KEY);
		final AppContext ac = (AppContext) context.getAttribute(AppContext.KEY);
		Assert.assertNotNull(ac);
		Assert.assertNotNull(mec);
		if(employSecurity) Assert.assertNotNull(sc);
	}
}
