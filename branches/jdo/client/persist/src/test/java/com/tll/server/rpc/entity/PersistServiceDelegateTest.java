/**
 * The Logic Lab
 * @author jpk
 * @since Jun 26, 2009
 */
package com.tll.server.rpc.entity;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.AbstractInjectedTest;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.model.CharacterPropertyValue;
import com.tll.common.model.IEntityType;
import com.tll.common.model.IntPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.StringPropertyValue;
import com.tll.common.model.test.TestEntityType;
import com.tll.common.search.PrimaryKeySearch;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.di.ClientPersistModule;
import com.tll.di.EntityAssemblerModule;
import com.tll.di.EntityBeanFactoryModule;
import com.tll.di.EntityServiceFactoryModule;
import com.tll.di.LogExceptionHandlerModule;
import com.tll.di.MailModule;
import com.tll.di.MarshalModule;
import com.tll.di.MockDaoModule;
import com.tll.di.ModelModule;
import com.tll.di.RefDataModule;
import com.tll.di.ValidationModule;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityGraphBuilder;
import com.tll.model.TestPersistenceUnitEntityAssembler;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;
import com.tll.refdata.RefDataType;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.entity.test.TestAddressService;
import com.tll.server.rpc.entity.test.TestEntityTypeResolver;

/**
 * PersistServiceDelegateTest
 * @author jpk
 */
@Test(groups = {
	"server", "client-persist" })
	public class PersistServiceDelegateTest extends AbstractInjectedTest {

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new RefDataModule());

		// hack: create mail module to avoid guice ConfigurationException
		// as it implicitly binds at the MailModule constrctor
		modules.add(new MailModule(Config.load(new ConfigRef("config-mail.properties"))));

		modules.add(new EntityAssemblerModule() {

			@Override
			protected void bindEntityAssembler() {
				bind(IEntityAssembler.class).to(TestPersistenceUnitEntityAssembler.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new ModelModule());
		modules.add(new ValidationModule());
		modules.add(new EntityBeanFactoryModule());
		modules.add(new MockDaoModule() {

			@Override
			protected void bindEntityGraphBuilder() {
				bind(IEntityGraphBuilder.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
			}
		});
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
		modules.add(new ClientPersistModule() {

			@Override
			protected void bindPersistServiceImplResolver() {
				bind(IPersistServiceImplResolver.class).toProvider(new Provider<IPersistServiceImplResolver>() {

					@Override
					public IPersistServiceImplResolver get() {
						return new IPersistServiceImplResolver() {

							@Override
							public Class<? extends IPersistServiceImpl> resolve(IModelRelatedRequest request)
							throws IllegalArgumentException {
								return TestAddressService.class;
							}
						};
					}
				}).in(Scopes.SINGLETON);
			}
		});
	}

	@Override
	@BeforeClass
	protected void beforeClass() {
		super.beforeClass();
	}

	/**
	 * @return A new {@link PersistServiceDelegate} instance.
	 */
	protected final PersistServiceDelegate getDelegate() {
		return new PersistServiceDelegate(injector.getInstance(PersistContext.class), injector
				.getInstance(IPersistServiceImplResolver.class));
	}

	/**
	 * Tests {@link PersistServiceDelegate#load(LoadRequest)}
	 * @throws Exception
	 */
	@Test
	public void testLoad() throws Exception {
		final PersistServiceDelegate delegate = getDelegate();

		final PrimaryKeySearch search = new PrimaryKeySearch(new ModelKey(TestEntityType.ADDRESS, 1, null));
		final LoadRequest<PrimaryKeySearch> request = new LoadRequest<PrimaryKeySearch>(search);
		final ModelPayload p = delegate.load(request);

		assert p != null;
		final Model m = p.getModel();
		assert m != null;
		assert m.getId() != null && m.getId().equals(1) && m.getEntityType() != null
		&& m.getEntityType().equals(TestEntityType.ADDRESS);
	}

	/**
	 * Tests
	 * {@link PersistServiceDelegate#persist(com.tll.common.data.PersistRequest)}
	 * (add)
	 * @throws Exception
	 */
	@Test
	public void testAdd() throws Exception {
		final PersistServiceDelegate delegate = getDelegate();

		Model m = new Model(TestEntityType.ADDRESS, true);
		m.set(new IntPropertyValue(Model.ID_PROPERTY, 10000));
		m.set(new StringPropertyValue("address1", "1 tee street"));
		m.set(new StringPropertyValue("address2", "2 bee"));
		m.set(new StringPropertyValue("city", "the city"));
		m.set(new StringPropertyValue("country", "us"));
		m.set(new StringPropertyValue("emailAddress", "email@schmemail.com"));
		m.set(new StringPropertyValue("fax", "2223334444"));
		m.set(new StringPropertyValue("firstName", "First"));
		m.set(new StringPropertyValue("lastName", "Last"));
		m.set(new CharacterPropertyValue("mi", 'm'));
		m.set(new StringPropertyValue("phone", "1112223333"));
		m.set(new StringPropertyValue("postalCode", "48104"));
		m.set(new StringPropertyValue("province", "MI"));

		final ModelPayload p = delegate.persist(new PersistRequest(m));
		assert p != null;
		m = p.getModel();
		assert m != null;
		assert m.getId() != null && m.getId().equals(10000) && m.getEntityType() != null
		&& m.getEntityType().equals(TestEntityType.ADDRESS);
		final Object ov = m.getProperty("version");
		assert ov != null && ov.equals(0);
	}

	/**
	 * Tests
	 * {@link PersistServiceDelegate#persist(com.tll.common.data.PersistRequest)}
	 * (update)
	 * @throws Exception
	 */
	@Test
	public void testUpdate() throws Exception {
		final PersistServiceDelegate delegate = getDelegate();

		Model m = new Model(TestEntityType.ADDRESS, true);
		m.set(new IntPropertyValue(Model.VERSION_PROPERTY, 0));
		m.set(new IntPropertyValue(Model.ID_PROPERTY, 10000));
		m.set(new StringPropertyValue("address1", "1 tee street"));
		m.set(new StringPropertyValue("address2", "2 bee"));
		m.set(new StringPropertyValue("city", "the city"));
		m.set(new StringPropertyValue("country", "us"));
		m.set(new StringPropertyValue("emailAddress", "email@schmemail.com"));
		m.set(new StringPropertyValue("fax", "2223334444"));
		m.set(new StringPropertyValue("firstName", "First"));
		m.set(new StringPropertyValue("lastName", "Last"));
		m.set(new CharacterPropertyValue("mi", 'm'));
		m.set(new StringPropertyValue("phone", "1112223333"));
		m.set(new StringPropertyValue("postalCode", "48104"));
		m.set(new StringPropertyValue("province", "MI"));

		final ModelPayload p = delegate.persist(new PersistRequest(m));
		assert p != null;
		m = p.getModel();
		assert m != null;
		assert m.getId() != null && m.getId().equals(10000) && m.getEntityType() != null
		&& m.getEntityType().equals(TestEntityType.ADDRESS);
		final Object ov = m.getProperty("version");
		assert ov != null && ov.equals(1);
	}

	/**
	 * Tests
	 * {@link PersistServiceDelegate#purge(com.tll.common.data.PurgeRequest)}
	 * (update)
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		final PersistServiceDelegate delegate = getDelegate();

		final ModelKey origMk = new ModelKey(TestEntityType.ADDRESS, 2, null);
		final ModelPayload p = delegate.purge(new PurgeRequest(origMk));
		assert p != null;
		final ModelKey mk = p.getRef();
		assert mk != null && mk.isSet() && mk.equals(origMk);
	}

	/**
	 * Tests
	 * {@link PersistServiceDelegate#loadAuxData(com.tll.common.data.AuxDataRequest)}
	 * @throws Exception
	 */
	@Test
	public void testLoadAuxData() throws Exception {
		final PersistServiceDelegate delegate = getDelegate();

		final AuxDataRequest adr = new AuxDataRequest();
		adr.requestAppRefData(RefDataType.ISO_COUNTRY_CODES);
		adr.requestEntityList(TestEntityType.ADDRESS);
		adr.requestEntityPrototype(TestEntityType.ADDRESS);
		final AuxDataPayload p = delegate.loadAuxData(adr);
		assert p != null;
		assert p.getEntityMap() != null && p.getEntityMap().size() > 0;
		assert p.getEntityPrototypes() != null && p.getEntityPrototypes().size() == 1;
		assert p.getRefDataMaps() != null && p.getRefDataMaps().size() == 1;
	}
}
