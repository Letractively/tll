/**
 * The Logic Lab
 * @author jpk Sep 6, 2007
 */
package com.tll.listhandler;

import java.util.List;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.tll.AbstractDbTest;
import com.tll.criteria.Criteria;
import com.tll.dao.DaoMode;
import com.tll.dao.IEntityDao;
import com.tll.dao.SearchResult;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.DbDialectModule;
import com.tll.di.DbShellModule;
import com.tll.di.EntityAssemblerModule;
import com.tll.di.OrmDaoModule;
import com.tll.di.MockEntityFactoryModule;
import com.tll.di.ModelModule;
import com.tll.di.TransactionModule;
import com.tll.model.Address;
import com.tll.model.IEntityAssembler;
import com.tll.model.MockEntityFactory;
import com.tll.service.entity.EntityService;
import com.tll.service.entity.IEntityService;

/**
 * PagingSearchListHandlerTest
 * @author jpk
 */
@Test(groups = "listhandler")
public class PagingSearchListHandlerTest extends AbstractDbTest {

	/**
	 * TestEntityService
	 * @author jpk
	 */
	static final class TestEntityService extends EntityService<Address> {

		/**
		 * Constructor
		 * @param dao
		 * @param entityAssembler
		 */
		@Inject
		public TestEntityService(IEntityDao dao, IEntityAssembler entityAssembler) {
			super(dao, entityAssembler);
		}

		@Override
		public Class<Address> getEntityClass() {
			return Address.class;
		}
	}

	/**
	 * The number of listing elements for which to test.
	 */
	private static final int NUM_LIST_ELEMENTS = 100;

	private DbShell db;

	/**
	 * Constructor
	 */
	public PagingSearchListHandlerTest() {
		super(DaoMode.ORM, true, false);
	}

	@BeforeClass(alwaysRun = true)
	public void onBeforeClass() {
		beforeClass();
	}

	@AfterClass(alwaysRun = true)
	public void onAfterClass() {
		afterClass();
	}

	@Override
	protected void beforeClass() {
		// create the db
		db = Guice.createInjector(Stage.DEVELOPMENT, new DbDialectModule(), new DbShellModule()).getInstance(DbShell.class);
		db.create();
		db.clear();

		super.beforeClass();
	}

	@Override
	protected void afterClass() {
		super.afterClass();

		// drop the db
		db.delete();
		db = null;
	}

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new ModelModule());
		modules.add(new MockEntityFactoryModule());
		super.addModules(modules);
		//Config.instance().setProperty(DaoModule.ConfigKeys.DAO_MODE_PARAM.getKey(), DaoMode.ORM.toString());
		modules.add(new OrmDaoModule());
		modules.add(new TransactionModule());
		modules.add(new EntityAssemblerModule());
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(new TypeLiteral<IEntityService<Address>>() {}).to(TestEntityService.class).in(Scopes.SINGLETON);
			}
		});
	}

	protected final IEntityDao getEntityDao() {
		return injector.getInstance(IEntityDao.class);
	}

	protected final IEntityService<Address> getTestEntityService() {
		return injector.getInstance(Key.get(new TypeLiteral<IEntityService<Address>>() {}));
	}

	protected final MockEntityFactory getMockEntityFactory() {
		return injector.getInstance(MockEntityFactory.class);
	}

	protected final void stubListElements() {
		// stub the list elements
		startNewTransaction();
		final Set<Address> elements = getMockEntityFactory().getNEntityCopies(Address.class, NUM_LIST_ELEMENTS, true);
		getEntityDao().persistAll(elements);
		setComplete();
		endTransaction();
	}

	@Test
	public void test() throws Exception {
		stubListElements();

		final IEntityService<Address> dataProvider = getTestEntityService();

		final List<Address> elements = dataProvider.loadAll();
		assert elements != null && elements.size() > 0 : "No elements exist - test can't run";
		assert elements.size() >= 10 : "At least 10 list elements must be present for this test";
		final int pageSize = 3;

		final Criteria<Address> criteria = new Criteria<Address>(Address.class);
		final Sorting sorting = new Sorting(new SortColumn("emailAddress"));
		final IListHandler<SearchResult<Address>> listHandler =
				ListHandlerFactory.create(criteria, sorting, ListHandlerType.PAGE, dataProvider);

		List<SearchResult<Address>> list;

		list = listHandler.getElements(0, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize * 2, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		final List<SearchResult<Address>> alist = listHandler.getElements(0, elements.size(), sorting);
		assert alist.size() == elements.size();

		for(int i = 0; i < elements.size(); i++) {
			final Address element = alist.get(i).getEntity();
			assert element != null : "Empty element in list";
		}
	}

}
