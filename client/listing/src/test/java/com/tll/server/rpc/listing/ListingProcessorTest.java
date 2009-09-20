/**
 * The Logic Lab
 * @author jpk
 * Jun 20, 2008
 */
package com.tll.server.rpc.listing;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.common.data.ListingOp;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.model.Model;
import com.tll.common.search.test.TestAddressSearch;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.Sorting;
import com.tll.di.Db4oDaoModule;
import com.tll.di.Db4oDbShellModule;
import com.tll.di.EntityServiceFactoryModule;
import com.tll.di.LogExceptionHandlerModule;
import com.tll.di.MailModule;
import com.tll.di.RefDataModule;
import com.tll.di.TestListingModule;
import com.tll.di.TestMarshalModule;
import com.tll.di.TestPersistenceUnitModelModule;
import com.tll.listhandler.ListHandlerType;

/**
 * ListingProcessorTest - Tests the {@link ListingProcessor}.
 * @author jpk
 */
@Test(groups = { "server", "listing" })
public class ListingProcessorTest extends AbstractDbAwareTest {

	static final String ssnId1 = "1";
	static final String ssnId2 = "2";

	static final String listingName1 = "listing1";
	static final String listingName2 = "listing2";

	private final ListingProcessor processor = new ListingProcessor();

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new RefDataModule());

		// hack: create mail module to avoid guice ConfigurationException
		// as it implicitly binds at the MailModule constrctor
		modules.add(new MailModule(Config.load(new ConfigRef("config-mail.properties"))));

		modules.add(new TestPersistenceUnitModelModule());
		modules.add(new Db4oDaoModule(getConfig()));
		modules.add(new Db4oDbShellModule());
		modules.add(new EntityServiceFactoryModule());
		modules.add(new LogExceptionHandlerModule());
		modules.add(new TestMarshalModule());
		modules.add(new TestListingModule());
	}

	@Override
	@BeforeClass()
	protected void beforeClass() {
		super.beforeClass();
	}

	/**
	 * @return A new {@link ListingContext} instance.
	 */
	ListingContext getListingContext() {
		return injector.getInstance(ListingContext.class);
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
		final ListingContext context = getListingContext();

		final ListingRequest request = new ListingRequest(listingName1, getListingDef(), ListingOp.REFRESH, 0, null);

		final ListingPayload<Model> p = processor.process(ssnId1, context, request);

		assert p != null;
	}
}
