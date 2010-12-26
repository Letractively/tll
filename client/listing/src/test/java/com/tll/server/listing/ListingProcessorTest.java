/**
 * The Logic Lab
 * @author jpk
 * Jun 20, 2008
 */
package com.tll.server.listing;

import java.net.URL;
import java.util.List;

import net.sf.ehcache.CacheManager;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.tll.common.data.ListingOp;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.rpc.ListingPayload;
import com.tll.common.data.rpc.ListingRequest;
import com.tll.common.search.test.TestAddressSearch;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.Sorting;
import com.tll.di.LogExceptionHandlerModule;
import com.tll.listhandler.ListHandlerType;
import com.tll.model.test.Address;
import com.tll.server.listing.ListingCache.ListingCacheAware;
import com.tll.server.listing.test.TestRowListHandlerProvider;
/**
 * ListingProcessorTest - Tests the {@link ListingProcessor}.
 * @author jpk
 */
@Test(groups = { "server", "listing" })
public class ListingProcessorTest extends AbstractDbAwareTest {

	static final String ssnId1 = "1";
	static final String ssnId2 = "2";

	static final String listingId1 = "listing1";
	static final String listingId2 = "listing2";

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new LogExceptionHandlerModule());
	
		// satisfy caching requirement for ListingCache
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(CacheManager.class).annotatedWith(ListingCacheAware.class).toProvider(new Provider<CacheManager>() {

					@Override
					public CacheManager get() {
						final URL url = Thread.currentThread().getContextClassLoader().getResource("ehcache.xml");
						return new CacheManager(url);
					}
				}).in(Scopes.SINGLETON);
				
				binder.bind(IRowListHandlerProvider.class).to(TestRowListHandlerProvider.class).in(Scopes.SINGLETON);
			}
		});

		//modules.add(new TestListingModule());
	}

	@Override
	@BeforeClass()
	protected void beforeClass() {
		super.beforeClass();
	}

	/**
	 * @return A new {@link RemoteListingDefinition} instance.
	 */
	RemoteListingDefinition<TestAddressSearch> getListingDef() {
		final RemoteListingDefinition<TestAddressSearch> def =
			new RemoteListingDefinition<TestAddressSearch>(ListHandlerType.PAGE, new TestAddressSearch(), null, 2,
					new Sorting("lastName"));
		return def;
	}

	/**
	 * Tests the listing refresh op.
	 * @throws Exception
	 */
	public void testRefresh() throws Exception {
		final ListingContext<TestAddressSearch, Address> context = injector.getInstance(Key.get(new TypeLiteral<ListingContext<TestAddressSearch, Address>>() {}));

		final ListingRequest<TestAddressSearch> request = new ListingRequest<TestAddressSearch>(listingId1, getListingDef(), ListingOp.REFRESH, Integer.valueOf(0), null);

		ListingProcessor processor = new ListingProcessor();

		final ListingPayload<Address> p = processor.process(ssnId1, context, request);

		assert p != null;
	}
}
