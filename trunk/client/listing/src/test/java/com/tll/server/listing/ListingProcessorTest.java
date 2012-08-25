/**
 * The Logic Lab
 * @author jpk
 * Jun 20, 2008
 */
package com.tll.server.listing;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.common.data.ListingOp;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.rpc.ListingPayload;
import com.tll.common.data.rpc.ListingRequest;
import com.tll.common.search.test.TestAddressSearch;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.Sorting;
import com.tll.model.test.Address;
import com.tll.server.listing.test.TestListingModule;
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
		modules.add(new TestListingModule());
	}

	@Override
	protected void beforeClass() {
		super.beforeClass();
	}

	/**
	 * @return A new {@link RemoteListingDefinition} instance.
	 */
	RemoteListingDefinition<TestAddressSearch> getListingDef() {
		final RemoteListingDefinition<TestAddressSearch> def =
			new RemoteListingDefinition<TestAddressSearch>(new TestAddressSearch(), null, 2,
					new Sorting("lastName"));
		return def;
	}

	/**
	 * Tests the listing refresh op.
	 * @throws Exception
	 */
	public void testRefresh() throws Exception {
		final ListingContext context = injector.getInstance(ListingContext.class);

		final ListingRequest<TestAddressSearch> request = new ListingRequest<TestAddressSearch>(listingId1, getListingDef(), ListingOp.REFRESH, Integer.valueOf(0), null);

		ListingProcessor processor = new ListingProcessor();

		final ListingPayload<Address> p = processor.process(ssnId1, context, request);

		assert p != null;
	}
}
