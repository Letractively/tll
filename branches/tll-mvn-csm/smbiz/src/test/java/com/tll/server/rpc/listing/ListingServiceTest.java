/**
 * The Logic Lab
 * @author jpk
 * Jun 20, 2008
 */
package com.tll.server.rpc.listing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.common.data.ListingOp;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.model.IntPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.search.ISearch;
import com.tll.common.search.impl.AccountSearch;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.SelectNamedQueries;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.dao.Sorting;
import com.tll.di.DaoModule;
import com.tll.di.EntityServiceFactoryModule;
import com.tll.di.MailModule;
import com.tll.di.MockEntityFactoryModule;
import com.tll.di.RefDataModule;
import com.tll.di.VelocityModule;
import com.tll.listhandler.ListHandlerType;
import com.tll.model.EntityType;
import com.tll.model.IEntity;
import com.tll.server.Bootstrapper;
import com.tll.server.RequestContext;
import com.tll.util.EnumUtil;

/**
 * ListingServiceTest - Tests the {@link ListingService}.
 * @author jpk
 */
@Test(groups = "server")
public class ListingServiceTest extends DbTest {

	static final IListingService<ISearch, Model> theListingService = new IListingService<ISearch, Model>() {

		private final ListingService<IEntity, ISearch> svc = new ListingService<IEntity, ISearch>();

		@Override
		public ListingPayload<Model> process(ListingRequest<ISearch> listingRequest) {
			return svc.process(listingRequest);
		}

	};

	/**
	 * MockHttpInterceptor - Needed to provide the manually created
	 * {@link RequestContext}.
	 * @author jpk
	 */
	final class MockHttpInterceptor implements InvocationHandler {

		@SuppressWarnings("synthetic-access")
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if(method.getName().equals("getRequestContext")) {
				assert injector != null;
				MockServletContext servletContext = new MockServletContext();
				MockHttpServletRequest request = new MockHttpServletRequest();
				// MockHttpServletResponse response = new MockHttpServletResponse();
				return new RequestContext(Bootstrapper.generateAppContext(servletContext), request);
			}
			return method.invoke(theListingService, args);
		}
	}

	protected DaoMode daoMode;

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		assert getJpaMode() != null && daoMode != null;
		modules.add(new VelocityModule());
		modules.add(new MailModule());
		modules.add(new RefDataModule());
		modules.add(new MockEntityFactoryModule());
		modules.add(new DaoModule(daoMode));
		modules.add(new EntityServiceFactoryModule());
	}

	@BeforeClass(alwaysRun = true)
	@Parameters(value = "daoMode")
	public final void onBeforeClass(String daoModeStr) {
		daoMode = EnumUtil.fromString(DaoMode.class, daoModeStr);
		JpaMode jpaMode;
		if(daoMode == DaoMode.MOCK) {
			jpaMode = JpaMode.NONE;
		}
		else {
			jpaMode = JpaMode.SPRING;
		}
		setJpaMode(jpaMode);
		beforeClass();
	}

	@Override
	protected void beforeClass() {
		super.beforeClass();

	}

	@SuppressWarnings("unchecked")
	public void test() throws Exception {
		final AccountSearch search = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, EntityType.MERCHANT);
		search.setNamedQuery(SelectNamedQueries.MERCHANT_LISTING.getQueryName());
		search.setQueryParam(new IntPropertyValue("ispId", 1));

		// proxy the listing service
		final IListingService<AccountSearch, Model> listingService =
				(IListingService) Proxy.newProxyInstance(IListingService.class.getClassLoader(),
						new Class[] { IListingService.class }, new MockHttpInterceptor());

		Sorting initialSorting = new Sorting("name", "m");
		RemoteListingDefinition<AccountSearch> rld =
				new RemoteListingDefinition<AccountSearch>(ListHandlerType.PAGE, search, null, 2, initialSorting);

		ListingRequest<AccountSearch> listingRequest =
				new ListingRequest<AccountSearch>("TEST_LISTING", rld, ListingOp.REFRESH, 0, initialSorting);

		ListingPayload<Model> payload = listingService.process(listingRequest);
		assert payload != null;
	}
}
