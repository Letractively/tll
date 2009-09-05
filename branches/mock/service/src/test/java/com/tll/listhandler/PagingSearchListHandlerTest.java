/**
 * The Logic Lab
 * @author jpk Sep 6, 2007
 */
package com.tll.listhandler;

import java.util.List;
import java.util.Set;

import javax.validation.ValidatorFactory;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.tll.AbstractInjectedTest;
import com.tll.config.Config;
import com.tll.criteria.Criteria;
import com.tll.dao.IDbShell;
import com.tll.dao.IEntityDao;
import com.tll.dao.SearchResult;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.di.EGraphModule;
import com.tll.di.MockDaoModule;
import com.tll.di.MockDbShellModule;
import com.tll.di.ModelModule;
import com.tll.model.Address;
import com.tll.model.EntityBeanFactory;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.TestPersistenceUnitEntityAssembler;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;
import com.tll.service.entity.EntityService;
import com.tll.service.entity.IEntityService;

/**
 * PagingSearchListHandlerTest
 * @author jpk
 */
@Test(groups = "listhandler")
public class PagingSearchListHandlerTest extends AbstractInjectedTest {

	/**
	 * TestEntityService
	 * @author jpk
	 */
	static final class TestEntityService extends EntityService<Address> {

		/**
		 * Constructor
		 * @param dao
		 * @param entityAssembler
		 * @param vfactory
		 */
		@Inject
		public TestEntityService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
			super(dao, entityAssembler, vfactory);
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

	private final Config config;
	
	private IDbShell dbShell;

	/**
	 * Constructor
	 */
	public PagingSearchListHandlerTest() {
		super();
		config = Config.load();
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
		super.beforeClass();

		// create the db shell
		dbShell = injector.getInstance(IDbShell.class);
		dbShell.create();
		dbShell.clear();

	}

	@Override
	protected void afterClass() {
		super.afterClass();
		// drop the db
		dbShell.delete();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new ModelModule() {

			@Override
			protected void bindEntityAssembler() {
				bind(IEntityAssembler.class).to(TestPersistenceUnitEntityAssembler.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new EGraphModule() {

			@Override
			protected void bindEntityGraphBuilder() {
				bind(IEntityGraphPopulator.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new MockDaoModule());
		modules.add(new MockDbShellModule(config));
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

	protected final EntityBeanFactory getEntityBeanFactory() {
		return injector.getInstance(EntityBeanFactory.class);
	}

	protected final void stubListElements() {
		// stub the list elements
		//dbSupport.startNewTransaction();
		final Set<Address> elements = getEntityBeanFactory().getNEntityCopies(Address.class, NUM_LIST_ELEMENTS, true);
		getEntityDao().persistAll(elements);
		//dbSupport.setComplete();
		//dbSupport.endTransaction();
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
		final IListHandler<SearchResult<?>> listHandler =
			ListHandlerFactory.create(criteria, sorting, ListHandlerType.PAGE, dataProvider);

		List<SearchResult<?>> list;

		list = listHandler.getElements(0, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize * 2, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		final List<SearchResult<?>> alist = listHandler.getElements(0, elements.size(), sorting);
		assert alist.size() == elements.size();

		for(int i = 0; i < elements.size(); i++) {
			final Address element = (Address) alist.get(i).getElement();
			assert element != null : "Empty element in list";
		}
	}

}
