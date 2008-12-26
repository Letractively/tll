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
import com.tll.client.data.ListingOp;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.ListingRequest;
import com.tll.client.data.RemoteListingDefinition;
import com.tll.client.data.rpc.IListingService;
import com.tll.client.model.IntPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.search.ISearch;
import com.tll.client.search.impl.AccountSearch;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.SelectNamedQuery;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.di.AppRefDataModule;
import com.tll.di.DaoModule;
import com.tll.di.EntityServiceModule;
import com.tll.di.JpaModule;
import com.tll.di.MailModule;
import com.tll.di.VelocityModule;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityType;
import com.tll.model.IEntity;
import com.tll.server.RequestContext;
import com.tll.util.EnumUtil;

/**
 * ListingServiceTest - Tests the {@link ListingService}.
 * @author jpk
 */
@Test(groups = "server.rpc")
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
				return new RequestContext(request, servletContext, injector);
			}
			return method.invoke(theListingService, args);
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
		// modules.add(new MockEntitiesModule());
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

	@SuppressWarnings("unchecked")
	public void test() throws Exception {

		final AccountSearch search = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, EntityType.MERCHANT);
		search.setNamedQuery(SelectNamedQuery.MERCHANT_LISTING);
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
