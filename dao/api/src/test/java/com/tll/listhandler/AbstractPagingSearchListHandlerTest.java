/**
 * The Logic Lab
 * @author jpk Sep 6, 2007
 */
package com.tll.listhandler;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbTrans;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.SearchResult;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.model.egraph.EGraphModule;
import com.tll.model.egraph.EntityBeanFactory;
import com.tll.model.test.Address;
import com.tll.model.test.TestPersistenceUnitEntityGraphBuilder;

/**
 * AbstractPagingSearchListHandlerTest
 * @author jpk
 */
public abstract class AbstractPagingSearchListHandlerTest extends AbstractDbAwareTest {

	/**
	 * TestDataProvider
	 * @author jpk
	 */
	static final class TestDataProvider implements IListingDataProvider<Address> {

		final IEntityDao dao;
		final IDbTrans trans;

		/**
		 * Constructor
		 * @param dao
		 * @param trans
		 */
		@Inject
		public TestDataProvider(IEntityDao dao, IDbTrans trans) {
			super();
			this.dao = dao;
			this.trans = trans;
		}

		@Override
		public List<SearchResult> find(Criteria<Address> criteria, Sorting sorting) throws InvalidCriteriaException {
			trans.startTrans();
			try {
				return dao.find(criteria, sorting);
			}
			finally {
				trans.endTrans();
			}
		}

		@Override
		public List<Address> getEntitiesFromIds(Class<Address> entityClass, Collection<Long> pks, Sorting sorting) {
			trans.startTrans();
			try {
				return dao.findByPrimaryKeys(Address.class, pks, sorting);
			}
			finally {
				trans.endTrans();
			}
		}

		@Override
		public IPageResult<SearchResult> getPage(Criteria<Address> criteria, Sorting sorting, int offset, int pageSize)
				throws InvalidCriteriaException {
			trans.startTrans();
			try {
				return dao.getPage(criteria, sorting, offset, pageSize);
			}
			finally {
				trans.endTrans();
			}
		}

		@Override
		public List<Long> getPrimaryKeys(Criteria<Address> criteria, Sorting sorting) throws InvalidCriteriaException {
			trans.startTrans();
			try {
				return dao.getPrimaryKeys(criteria, sorting);
			}
			finally {
				trans.endTrans();
			}
		}
	} // TestDataProvider

	/**
	 * The number of listing elements for which to test.
	 */
	private static final int NUM_LIST_ELEMENTS = 100;

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new EGraphModule(TestPersistenceUnitEntityGraphBuilder.class, null));
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(new TypeLiteral<IListingDataProvider<Address>>() {}).to(TestDataProvider.class)
						.in(Scopes.SINGLETON);
			}
		});
	}

	@BeforeClass(alwaysRun = true)
	public void onBeforeClass() {
		beforeClass();
	}

	@AfterClass(alwaysRun = true)
	public void onAfterClass() {
		afterClass();
	}

	protected final IEntityDao getEntityDao() {
		return injector.getInstance(IEntityDao.class);
	}

	protected final IListingDataProvider<Address> getTestEntityService() {
		return injector.getInstance(Key.get(new TypeLiteral<IListingDataProvider<Address>>() {}));
	}

	protected final EntityBeanFactory getEntityBeanFactory() {
		return injector.getInstance(EntityBeanFactory.class);
	}

	protected final void stubListElements() {
		// stub the list elements
		getDbTrans().startTrans();
		final Set<Address> elements = getEntityBeanFactory().getNEntityCopies(Address.class, NUM_LIST_ELEMENTS, true);
		getEntityDao().persistAll(elements);
		getDbTrans().setComplete();
		getDbTrans().endTrans();
	}

	@Test
	public void test() throws Exception {
		stubListElements();

		getDbTrans().startTrans();
		final List<? extends Address> elements = getEntityDao().loadAll(Address.class);
		getDbTrans().endTrans();
		assert elements != null && elements.size() > 0 : "No elements exist - test can't run";
		assert elements.size() >= 10 : "At least 10 list elements must be present for this test";
		final int pageSize = 3;

		final IListingDataProvider<Address> dataProvider = getTestEntityService();
		final Criteria<Address> criteria = new Criteria<Address>(Address.class);
		final Sorting sorting = new Sorting(new SortColumn("emailAddress"));
		final IListHandler<SearchResult> listHandler =
				ListHandlerFactory.create(criteria, sorting, ListHandlerType.PAGE, dataProvider);

		List<SearchResult> list;

		list = listHandler.getElements(0, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize * 2, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		final List<SearchResult> alist = listHandler.getElements(0, elements.size(), sorting);
		assert alist.size() == elements.size();

		for(int i = 0; i < elements.size(); i++) {
			final Address element = (Address) alist.get(i).getElement();
			assert element != null : "Empty element in list";
		}
	}

}
