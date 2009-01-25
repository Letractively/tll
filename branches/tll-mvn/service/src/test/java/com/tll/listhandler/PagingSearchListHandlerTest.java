/**
 * The Logic Lab
 * @author jpk
 * Sep 6, 2007
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
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.tll.DbTest;
import com.tll.criteria.Criteria;
import com.tll.dao.IEntityDao;
import com.tll.dao.JpaMode;
import com.tll.dao.SearchResult;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.DaoModule;
import com.tll.di.DbDialectModule;
import com.tll.di.DbShellModule;
import com.tll.di.MockEntityFactoryModule;
import com.tll.di.ModelModule;
import com.tll.model.Account;
import com.tll.model.IEntityAssembler;
import com.tll.model.INamedEntity;
import com.tll.model.MockEntityFactory;
import com.tll.service.entity.INamedEntityService;
import com.tll.service.entity.NamedEntityService;

/**
 * PagingSearchListHandlerTest
 * @author jpk
 */
@Test(groups = "listhandler")
public class PagingSearchListHandlerTest extends DbTest {

	/**
	 * The number of listing elements for which to test.
	 */
	private static final int NUM_LIST_ELEMENTS = 100;

	/**
	 * ListHandlerDataProvider - The test list handler data provider.
	 * @author jpk
	 */
	private static final class TestEntityService extends NamedEntityService<Account> {

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
		public Class<Account> getEntityClass() {
			return Account.class;
		}
	}

	private DbShell db;
	
	/**
	 * Constructor
	 */
	public PagingSearchListHandlerTest() {
		super(JpaMode.SPRING, true);
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
		Injector i = Guice.createInjector(Stage.DEVELOPMENT, new DbDialectModule(), new DbShellModule());
		db = i.getInstance(DbShell.class);
		db.create();

		super.beforeClass();
		
		// stub the list elements
		Set<Account> accounts = getMockEntityFactory().getNEntityCopies(Account.class, NUM_LIST_ELEMENTS, true);
		startNewTransaction();
		getEntityDao().persistAll(accounts);
		setComplete();
		endTransaction();
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
		modules.add(new DaoModule());
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(new TypeLiteral<INamedEntityService<Account>>() {}).to(TestEntityService.class)
						.in(
						Scopes.SINGLETON);
			}
		});
	}

	protected final IEntityDao getEntityDao() {
		return injector.getInstance(IEntityDao.class);
	}

	protected final INamedEntityService<Account> getTestEntityService() {
		return injector.getInstance(Key.get(new TypeLiteral<INamedEntityService<Account>>() {}));
	}
	
	protected final MockEntityFactory getMockEntityFactory() {
		return injector.getInstance(MockEntityFactory.class);
	}

	@Test
	public void test() throws Exception {

		final INamedEntityService<Account> dataProvider = getTestEntityService();

		List<Account> allAccounts = dataProvider.loadAll();
		assert allAccounts != null && allAccounts.size() > 0 : "No accounts exist - test can't run";
		assert allAccounts.size() >= 10 : "At least 10 list elements must be present for this test";
		final int pageSize = 3;

		Criteria<Account> criteria = new Criteria<Account>(Account.class);
		Sorting sorting = new Sorting(new SortColumn(INamedEntity.NAME));
		IListHandler<SearchResult<Account>> listHandler =
				ListHandlerFactory.create(criteria, sorting, ListHandlerType.PAGE, dataProvider);

		List<SearchResult<Account>> list;

		list = listHandler.getElements(0, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize * 2, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		List<SearchResult<Account>> alist = listHandler.getElements(0, allAccounts.size(), sorting);
		assert alist.size() == allAccounts.size();

		for(int i = 0; i < allAccounts.size(); i++) {
			Account account = alist.get(i).getEntity();
			assert account != null : "Empty account in list";
		}
	}

}
