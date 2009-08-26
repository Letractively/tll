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
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.AbstractInjectedTest;
import com.tll.common.data.ListingOp;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.search.test.TestAddressSearch;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.Sorting;
import com.tll.di.EGraphModule;
import com.tll.di.EntityServiceFactoryModule;
import com.tll.di.ListingModule;
import com.tll.di.LogExceptionHandlerModule;
import com.tll.di.MailModule;
import com.tll.di.MarshalModule;
import com.tll.di.MockDaoModule;
import com.tll.di.ModelModule;
import com.tll.di.RefDataModule;
import com.tll.listhandler.ListHandlerType;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityGraphBuilder;
import com.tll.model.TestPersistenceUnitEntityAssembler;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.IMarshalOptionsResolver;
import com.tll.server.rpc.entity.test.TestEntityTypeResolver;
import com.tll.server.rpc.listing.test.TestListingDataProviderResolver;
import com.tll.server.rpc.listing.test.TestListingSearchTranslator;
import com.tll.server.rpc.listing.test.TestNamedQueryResolver;

/**
 * ListingProcessorTest - Tests the {@link ListingProcessor}.
 * @author jpk
 */
@Test(groups = {
	"server", "listing" })
	public class ListingProcessorTest extends AbstractInjectedTest {

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

		modules.add(new ModelModule() {

			@Override
			protected void bindPrimaryKeyGenerator() {
				// TODO
			}

			@Override
			protected void bindEntityAssembler() {
				bind(IEntityAssembler.class).to(TestPersistenceUnitEntityAssembler.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new EGraphModule() {

			@Override
			protected void bindEntityGraphBuilder() {
				bind(IEntityGraphBuilder.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new MockDaoModule());
		modules.add(new EntityServiceFactoryModule());
		modules.add(new LogExceptionHandlerModule());
		modules.add(new MarshalModule() {

			@Override
			protected void bindMarshalOptionsResolver() {
				bind(IMarshalOptionsResolver.class).toProvider(new Provider<IMarshalOptionsResolver>() {

					@Override
					public IMarshalOptionsResolver get() {
						return new IMarshalOptionsResolver() {

							@Override
							public MarshalOptions resolve(IEntityType entityType) throws IllegalArgumentException {
								return MarshalOptions.NO_REFERENCES;
							}
						};
					}
				}).in(Scopes.SINGLETON);
			}

			@Override
			protected void bindEntityTypeResolver() {
				bind(IEntityTypeResolver.class).to(TestEntityTypeResolver.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new ListingModule() {

			@Override
			protected void bindNamedQueryResolver() {
				bind(INamedQueryResolver.class).to(TestNamedQueryResolver.class).in(Scopes.SINGLETON);
			}

			@Override
			protected void bindListingSearchTranslator() {
				bind(IListingSearchTranslator.class).to(TestListingSearchTranslator.class).in(Scopes.SINGLETON);
			}

			@Override
			protected void bindListingDataProviderResolver() {
				bind(IListingDataProviderResolver.class).to(TestListingDataProviderResolver.class).in(Scopes.SINGLETON);
			}
		});
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
