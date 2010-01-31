/**
 * The Logic Lab
 * @author jpk
 * @since Jun 26, 2009
 */
package com.tll.server.rpc.entity;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.model.CharacterPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.StringPropertyValue;
import com.tll.common.model.test.TestEntityType;
import com.tll.common.search.PrimaryKeySearch;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbShell;
import com.tll.dao.IDbTrans;
import com.tll.dao.IEntityDao;
import com.tll.dao.db4o.test.Db4oTrans;
import com.tll.di.AbstractDb4oDaoModule;
import com.tll.di.LogExceptionHandlerModule;
import com.tll.di.MailModule;
import com.tll.di.RefDataModule;
import com.tll.di.TestEntityServiceFactoryModule;
import com.tll.di.TestMarshalModule;
import com.tll.di.test.Db4oDbShellModule;
import com.tll.di.test.TestClientPersistModule;
import com.tll.di.test.TestDb4oDaoModule;
import com.tll.di.test.TestPersistenceUnitModule;
import com.tll.model.EntityBeanFactory;
import com.tll.model.IEntity;
import com.tll.model.test.Address;
import com.tll.model.test.TestEntityFactory;
import com.tll.refdata.RefDataType;

/**
 * PersistServiceDelegateTest
 * @author jpk
 */
@Test(groups = { "server", "client-persist" })
public class PersistServiceDelegateTest extends AbstractDbAwareTest {

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new RefDataModule());

		// hack: create mail module to avoid guice ConfigurationException
		// as it implicitly binds at the MailModule constrctor
		modules.add(new MailModule(Config.load(new ConfigRef("config-mail.properties"))));

		modules.add(new TestPersistenceUnitModule(null, TestEntityFactory.class));
		modules.add(new TestDb4oDaoModule(getConfig()));
		modules.add(new Db4oDbShellModule());
		modules.add(new TestEntityServiceFactoryModule());
		modules.add(new LogExceptionHandlerModule());
		modules.add(new TestMarshalModule());
		modules.add(new TestClientPersistModule());
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(IDbTrans.class).to(Db4oTrans.class).in(Scopes.SINGLETON);
			}
		});
	}

	@Override
	@BeforeClass
	protected void beforeClass() {
		// create the db shell first (before test injector creation) to avoid db4o
		// file lock when objectcontainer is instantiated
		final Config cfg = Config.load();
		cfg.setProperty(AbstractDb4oDaoModule.ConfigKeys.DB_TRANS_BINDTOSPRING.getKey(), Boolean.FALSE);
		final Injector i = buildInjector(new TestDb4oDaoModule(cfg), new Db4oDbShellModule() );
		final IDbShell dbs = i.getInstance(IDbShell.class);
		dbs.drop();
		dbs.create();

		super.beforeClass();
	}

	/**
	 * @return A new {@link PersistServiceDelegate} instance.
	 */
	protected final PersistServiceDelegate getDelegate() {
		return new PersistServiceDelegate(injector.getInstance(PersistContext.class), injector
				.getInstance(IPersistServiceImplResolver.class));
	}

	private EntityBeanFactory getEntityBeanFactory() {
		return injector.getInstance(EntityBeanFactory.class);
	}

	private void addEntityToDb(IEntity entity) {
		// stub address first
		startNewTransaction();
		try {
			injector.getInstance(IEntityDao.class).persist(entity);
			setComplete();
		}
		finally {
			endTransaction();
		}
	}

	/**
	 * Tests {@link PersistServiceDelegate#load(LoadRequest)}
	 * @throws Exception
	 */
	@Test
	public void testLoad() throws Exception {

		// add test entity to db first
		final Address a = getEntityBeanFactory().getEntityCopy(Address.class, true);
		final Long id = a.getId();
		addEntityToDb(a);

		final PersistServiceDelegate delegate = getDelegate();
		final PrimaryKeySearch search = new PrimaryKeySearch(new ModelKey(TestEntityType.ADDRESS, id.toString(), null));
		final LoadRequest<PrimaryKeySearch> request = new LoadRequest<PrimaryKeySearch>(search);
		final ModelPayload p = delegate.load(request);

		assert p != null;
		final Model m = p.getModel();
		assert m != null;
		assert m.getId() != null && m.getId().equals(id.toString()) && m.getEntityType() != null
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

		Model m = new Model(TestEntityType.ADDRESS);
		m.set(new StringPropertyValue(Model.ID_PROPERTY, "10000"));
		m.set(new StringPropertyValue("address1", "1 tee streetU"));
		m.set(new StringPropertyValue("address2", "2 bee"));
		m.set(new StringPropertyValue("city", "the city"));
		m.set(new StringPropertyValue("country", "us"));
		m.set(new StringPropertyValue("emailAddress", "email@schmemail.com"));
		m.set(new StringPropertyValue("fax", "2223334444"));
		m.set(new StringPropertyValue("firstName", "First"));
		m.set(new StringPropertyValue("lastName", "Last"));
		m.set(new CharacterPropertyValue("mi", Character.valueOf('m')));
		m.set(new StringPropertyValue("phone", "1112223333"));
		m.set(new StringPropertyValue("postalCode", "48104"));
		m.set(new StringPropertyValue("province", "MI"));

		final ModelPayload p = delegate.persist(new PersistRequest(m, false));
		assert p != null;
		m = p.getModel();
		assert m != null;
		assert m.getId() != null && m.getId().equals("10000") && m.getEntityType() != null
		&& m.getEntityType().equals(TestEntityType.ADDRESS);
		final String sov = m.getVersion();
		assert sov != null && sov.equals("0");
	}

	/**
	 * Tests
	 * {@link PersistServiceDelegate#persist(com.tll.common.data.PersistRequest)}
	 * (update)
	 * @throws Exception
	 */
	@Test
	public void testUpdate() throws Exception {
		// add test entity to db first
		final Address a = getEntityBeanFactory().getEntityCopy(Address.class, true);
		final Long id = a.getId();
		addEntityToDb(a);

		final PersistServiceDelegate delegate = getDelegate();

		Model m = new Model(TestEntityType.ADDRESS);
		m.set(new StringPropertyValue(Model.VERSION_PROPERTY, "0"));
		m.set(new StringPropertyValue(Model.ID_PROPERTY, id.toString()));
		m.set(new StringPropertyValue("address1", "1 changed street"));
		m.set(new StringPropertyValue("address2", "2 beechange"));
		m.set(new StringPropertyValue("city", "change city"));

		final ModelPayload p = delegate.persist(new PersistRequest(m, true));
		assert p != null;
		m = p.getModel();
		assert m != null;
		assert m.getId() != null && m.getId().equals(id.toString()) && m.getEntityType() != null
		&& m.getEntityType().equals(TestEntityType.ADDRESS);
		final String ov = m.getVersion();
		assert ov != null && ov.equals("1");
	}

	/**
	 * Tests
	 * {@link PersistServiceDelegate#purge(com.tll.common.data.PurgeRequest)}
	 * (update)
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		// add test entity to db first
		final Address a = getEntityBeanFactory().getEntityCopy(Address.class, true);
		final Long id = a.getId();
		addEntityToDb(a);

		final PersistServiceDelegate delegate = getDelegate();
		final ModelKey origMk = new ModelKey(TestEntityType.ADDRESS, id.toString(), null);
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
