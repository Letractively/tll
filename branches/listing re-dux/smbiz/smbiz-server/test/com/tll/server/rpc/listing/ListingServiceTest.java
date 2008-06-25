/**
 * The Logic Lab
 * @author jpk
 * Jun 20, 2008
 */
package com.tll.server.rpc.listing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.guice.AppRefDataModule;
import com.tll.guice.DaoModule;
import com.tll.guice.EntityServiceModule;
import com.tll.guice.JpaModule;
import com.tll.guice.MailModule;
import com.tll.guice.MockEntitiesModule;
import com.tll.guice.VelocityModule;
import com.tll.server.RequestContext;
import com.tll.util.EnumUtil;

/**
 * ListingServiceTest - Tests the {@link ListingService}.
 * @author jpk
 */
@Test(groups = "server.rpc")
public class ListingServiceTest extends DbTest {

	final class MockHttpInterceptor implements InvocationHandler {

		@Override
		@SuppressWarnings("unchecked")
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			assert proxy instanceof ListingService;
			assert method.getName().equals("getRequestContext");
			assert injector != null;

			MockServletContext servletContext = new MockServletContext();
			MockHttpServletRequest request = new MockHttpServletRequest();
			// MockHttpServletResponse response = new MockHttpServletResponse();
			return new RequestContext(request, servletContext, injector);
		}

	}

	protected DaoMode daoMode;

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		assert jpaMode != null && daoMode != null;
		modules.add(new VelocityModule());
		modules.add(new MailModule());
		modules.add(new AppRefDataModule());
		modules.add(new MockEntitiesModule());
		modules.add(new JpaModule(jpaMode));
		modules.add(new DaoModule(daoMode));
		modules.add(new EntityServiceModule());
	}

	@BeforeClass(alwaysRun = true)
	@Parameters(value = {
		"jpaMode",
		"daoMode" })
	public final void onBeforeClass(String jpaModeStr, String daoModeStr) {
		this.jpaMode = EnumUtil.fromString(JpaMode.class, jpaModeStr);
		this.daoMode = EnumUtil.fromString(DaoMode.class, daoModeStr);
		beforeClass();
	}

	@Override
	protected void beforeClass() {
		super.beforeClass();

	}

	public void test() throws Exception {
		/*
		ListingService<Account> listingService = new ListingService<Account>();

		RemoteListingDefinition<Account> rld = new RemoteListingDefinition<Account>(ListHandlerType.PAGE);

		ListingRequest<Account> listingRequest = new ListingRequest<Account>();

		listingService.process(listingRequest);
		*/
	}
}
