/**
 * The Logic Lab
 * @author jpk
 * Mar 8, 2008
 */
package com.tll.service.entity;

import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.criteria.CriteriaFactory;
import com.tll.criteria.ICriteria;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.guice.DaoModule;
import com.tll.guice.EntityServiceModule;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.IListHandlerDataProvider;
import com.tll.listhandler.ListHandlerFactory;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.SearchResult;
import com.tll.listhandler.Sorting;
import com.tll.model.IEntity;
import com.tll.model.IScalar;
import com.tll.model.impl.Interface;
import com.tll.util.EnumUtil;

/**
 * ListHandlerDataProviderTest
 * @author jpk
 */
@Test(groups = {
	"listhandler.dataprovider",
	"entity.service" })
public class ListHandlerDataProviderTest extends DbTest {

	static final int PAGE_SIZE = 2;

	protected DaoMode daoMode;

	/**
	 * Constructor
	 */
	public ListHandlerDataProviderTest() {
		super();
	}

	@BeforeClass
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

		if(daoMode.isJpa()) {
			getDbShell().create();
			getDbShell().clear();
			getDbShell().stub();
		}
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);

		final DaoModule dm = new DaoModule(daoMode);
		modules.add(dm);

		final EntityServiceModule esm = new EntityServiceModule();
		modules.add(esm);
	}

	/**
	 * @return The {@link IListHandlerDataProvider} subject to testing.
	 */
	protected <E extends IEntity> IListHandlerDataProvider<E> getListHandlerDataProvider(Class<E> entityClass) {
		return injector.getInstance(IEntityServiceFactory.class).instanceByEntityType(entityClass);
	}

	protected static <E extends IEntity> void validateEntityListHandler(IListHandler<SearchResult<E>> listHandler)
			throws Exception {
		assert listHandler != null && listHandler.size() > 0;
		final List<SearchResult<E>> list = listHandler.getElements(0, PAGE_SIZE);
		for(final SearchResult<E> elem : list) {
			assert elem != null;
			final E intf = elem.getEntity();
			assert intf.getId() != null;
		}
	}

	protected static void validateScalarListHandler(IListHandler<SearchResult<Interface>> listHandler) throws Exception {
		assert listHandler != null && listHandler.size() > 0;
		final List<SearchResult<Interface>> list = listHandler.getElements(0, PAGE_SIZE);
		for(final SearchResult<Interface> elem : list) {
			final IScalar scalar = elem.getScalar();
			final Map<String, Object> tupleMap = scalar.getTupleMap();
			assert tupleMap != null && tupleMap.size() > 0;
		}
	}

	/**
	 * Tests the data providing for entity named queries.
	 * @throws Exception
	 */
	@Test
	public void testEntityNamedQuery() throws Exception {
		final IListHandlerDataProvider<Interface> dataProvider = getListHandlerDataProvider(Interface.class);

		final ICriteria<? extends Interface> criteria =
				CriteriaFactory.buildEntityQueryCriteria(Interface.class, "interface.test");
		criteria.setSorting(new Sorting("code"));

		final List<SearchResult<Interface>> list = dataProvider.find(criteria);
		assert list != null && list.size() > 0 : "No entity results found";
	}

	/**
	 * Tests entity collection scalar list handling.
	 * @throws Exception
	 */
	@Test
	public void testCollectionEntityNamedQueryListHandling() throws Exception {
		final IListHandlerDataProvider<Interface> dataProvider = getListHandlerDataProvider(Interface.class);
		final ICriteria<? extends Interface> criteria =
				CriteriaFactory.buildEntityQueryCriteria(Interface.class, "interface.test");
		criteria.setSorting(new Sorting("code"));
		criteria.setPageSize(PAGE_SIZE);
		final IListHandler<SearchResult<Interface>> listHandler =
				ListHandlerFactory.create(criteria, ListHandlerType.COLLECTION, dataProvider);
		validateEntityListHandler(listHandler);
	}

	/**
	 * Tests entity paging scalar list handling.
	 * @throws Exception
	 */
	@Test
	public void testPagingEntityNamedQueryListHandling() throws Exception {
		final IListHandlerDataProvider<Interface> dataProvider = getListHandlerDataProvider(Interface.class);
		final ICriteria<? extends Interface> criteria =
				CriteriaFactory.buildEntityQueryCriteria(Interface.class, "interface.test");
		criteria.setSorting(new Sorting("code"));
		criteria.setPageSize(PAGE_SIZE);
		final IListHandler<SearchResult<Interface>> listHandler =
				ListHandlerFactory.create(criteria, ListHandlerType.PAGE, dataProvider);
		validateEntityListHandler(listHandler);
	}

	/**
	 * Tests the data providing for scalar named queries.
	 * @throws Exception
	 */
	@Test
	public void testScalarNamedQuery() throws Exception {
		final IListHandlerDataProvider<Interface> dataProvider = getListHandlerDataProvider(Interface.class);

		final ICriteria<? extends Interface> criteria =
				CriteriaFactory.buildScalarQueryCriteria(Interface.class, "interface.summaryList");
		criteria.setSorting(new Sorting("intf.code"));

		final List<SearchResult<Interface>> list = dataProvider.find(criteria);
		assert list != null && list.size() > 0 : "No scalar results found";
	}

	/**
	 * Tests collection scalar list handling.
	 * @throws Exception
	 */
	@Test
	public void testCollectionScalarNamedQueryListHandling() throws Exception {
		final IListHandlerDataProvider<Interface> dataProvider = getListHandlerDataProvider(Interface.class);
		final ICriteria<? extends Interface> criteria =
				CriteriaFactory.buildScalarQueryCriteria(Interface.class, "interface.summaryList");
		criteria.setPageSize(PAGE_SIZE);
		criteria.setSorting(new Sorting("code"));
		final IListHandler<SearchResult<Interface>> listHandler =
				ListHandlerFactory.create(criteria, ListHandlerType.COLLECTION, dataProvider);
		validateScalarListHandler(listHandler);
	}

	/**
	 * Tests paging scalar list handling.
	 * @throws Exception
	 */
	@Test
	public void testPagingScalarNamedQueryListHandling() throws Exception {
		final IListHandlerDataProvider<Interface> dataProvider = getListHandlerDataProvider(Interface.class);
		final ICriteria<? extends Interface> criteria =
				CriteriaFactory.buildScalarQueryCriteria(Interface.class, "interface.summaryList");
		criteria.setSorting(new Sorting("intf.code"));
		criteria.setPageSize(PAGE_SIZE);
		final IListHandler<SearchResult<Interface>> listHandler =
				ListHandlerFactory.create(criteria, ListHandlerType.PAGE, dataProvider);
		validateScalarListHandler(listHandler);

		// sort it
		listHandler.sort(new Sorting("intf.dateModified"));
		validateScalarListHandler(listHandler);
	}
}
