/**
 * The Logic Lab
 * @author jpk
 * Apr 30, 2008
 */
package com.tll.listhandler;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.CacheManager;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.db4o.ObjectContainer;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.criteria.Criteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.criteria.QueryParam;
import com.tll.criteria.SelectNamedQueries;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbShell;
import com.tll.dao.IEntityDao;
import com.tll.dao.SearchResult;
import com.tll.dao.db4o.AbstractDb4oDaoModule;
import com.tll.dao.db4o.SmbizDb4oDaoModule;
import com.tll.dao.db4o.test.Db4oDbShellModule;
import com.tll.model.Asp;
import com.tll.model.IEntity;
import com.tll.model.Isp;
import com.tll.model.PropertyType;
import com.tll.model.SmbizEGraphModule;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.service.entity.SmbizEntityServiceFactoryModule;
import com.tll.service.entity.SmbizEntityServiceFactoryModule.UserCacheAware;
import com.tll.sort.SortColumn;
import com.tll.sort.Sorting;
import com.tll.util.ClassUtil;

/**
 * NamedQueriesTest
 * @author jpk
 */
@Test(groups = { "listhandler", "namedqueries" })
public class NamedQueriesTest extends AbstractDbAwareTest {

	/**
	 * CriteriaAndSorting - Ad hoc encapsulation of {@link Criteria} and {@link Sorting}.
	 * @author jpk
	 */
	static class CriteriaAndSorting {
		final Criteria<IEntity> criteria;
		final Sorting sorting;

		/**
		 * Constructor
		 * @param criteria
		 * @param sorting
		 */
		public CriteriaAndSorting(Criteria<IEntity> criteria, Sorting sorting) {
			super();
			this.criteria = criteria;
			this.sorting = sorting;
		}
	} // CriteriaAndSorting

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}

	@AfterClass(alwaysRun = true)
	public final void onAfterClass() {
		afterClass();
	}

	@Override
	protected void beforeClass() {
		// create the db shell first (before test injector creation) to avoid db4o
		// file lock when objectcontainer is instantiated
		final Config cfg = getConfig();
		cfg.setProperty(AbstractDb4oDaoModule.ConfigKeys.DB_TRANS_BINDTOSPRING.getKey(), Boolean.FALSE);
		final Injector i = buildInjector(new SmbizEGraphModule(), new SmbizDb4oDaoModule(cfg), new Db4oDbShellModule());
		final IDbShell dbs = i.getInstance(IDbShell.class);

		// re-stub db
		dbs.drop();
		dbs.create();
		dbs.addData();

		i.getInstance(ObjectContainer.class).close();

		cfg.setProperty(AbstractDb4oDaoModule.ConfigKeys.DB_TRANS_BINDTOSPRING.getKey(), Boolean.TRUE);
		super.beforeClass();
	}

	@Override
	protected void afterClass() {
		injector.getInstance(ObjectContainer.class).close();
		injector.getInstance(CacheManager.class).shutdown();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);

		// satisfy caching requirement for UserService
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(CacheManager.class).annotatedWith(UserCacheAware.class).toProvider(new Provider<CacheManager>() {

					@Override
					public CacheManager get() {
						return new CacheManager(ClassUtil.getResource("ehcache-smbiz-persist.xml"));
					}
				}).in(Scopes.SINGLETON);
			}
		});

		modules.add(new SmbizDb4oDaoModule(getConfig()));
		modules.add(new SmbizEntityServiceFactoryModule());
	}

	/**
	 * @param <E>
	 * @param entityClass
	 * @return The {@link IListingDataProvider} subject to testing.
	 */
	protected <E extends IEntity> IListingDataProvider getListHandlerDataProvider(Class<E> entityClass) {
		return injector.getInstance(IEntityServiceFactory.class).instanceByEntityType(entityClass);
	}

	@Override
	protected Config doGetConfig() {
		return Config.load(new ConfigRef("db40-config.properties"));
	}

	/**
	 * Does simple validation on the given list handler.
	 * @param <T>
	 * @param listHandler
	 * @param sorting
	 * @throws Exception
	 */
	protected <T> void validateListHandler(IListHandler<T> listHandler, Sorting sorting) throws Exception {
		assert listHandler != null : "The list handler is null";
		assert listHandler.getElements(0, 1, sorting) != null : "Unable to obtain the first list handler element";
		assert listHandler.size() > 0 : "No list handler elements exist";	// NOTE: this must be called *after* .getElements(...)
	}

	private IEntityDao getEntityDao() {
		return injector.getInstance(IEntityDao.class);
	}

	/**
	 * Creates a new {@link Criteria} instance for the given named query def.
	 * @param nq the named query def
	 * @return newly created query params to employ
	 */
	private CriteriaAndSorting createCriteriaAndSorting(SelectNamedQueries nq) {
		final ArrayList<IQueryParam> list = new ArrayList<IQueryParam>();
		Sorting sorting;
		switch(nq) {
		case ISP_LISTING:
			sorting = new Sorting("dateCreated");
			break;
		case MERCHANT_LISTING: {
			sorting = new Sorting("dateCreated");
			// find an isp..
			final Isp anIsp = getEntityDao().loadAll(Isp.class).get(0);
			final QueryParam qp = new QueryParam("ispId", PropertyType.STRING, anIsp.getId());
			list.add(qp);
			break;
		}
		case CUSTOMER_LISTING: {
			sorting = new Sorting(new SortColumn("dateCreated", "c"));

			// get the asp (to serve as the parent account)..
			final Asp asp = getEntityDao().loadAll(Asp.class).get(0);
			final QueryParam qp = new QueryParam("accountId", PropertyType.STRING, asp.getId());
			list.add(qp);
			break;
		}
		case INTERFACE_SUMMARY_LISTING:
			sorting = new Sorting(new SortColumn("code", "intf"));
			break;
		case ACCOUNT_INTERFACE_SUMMARY_LISTING: {
			sorting = new Sorting(new SortColumn("code", "intf"));

			list.add(new QueryParam("", PropertyType.STRING, "asp"));
			break;
		}

		// warn of unhandled defined named queries!
		default:
			throw new IllegalStateException("Unhandled named query: " + nq);
		}
		return new CriteriaAndSorting(new Criteria<IEntity>(nq, list), sorting);
	}

	@SuppressWarnings("unchecked")
	public void test() throws Exception {
		for(final SelectNamedQueries nq : SelectNamedQueries.values()) {
			final IListingDataProvider dataProvider = getListHandlerDataProvider((Class<IEntity>) nq.getEntityType());
			final CriteriaAndSorting cas = createCriteriaAndSorting(nq);
			final Criteria<IEntity> criteria = cas.criteria;
			final Sorting sorting = cas.sorting;

			// test for all list handler types
			for(final ListHandlerType lht : ListHandlerType.values()) {
				IListHandler<SearchResult> listHandler = null;
				logger.debug("Validating '" + nq.toString() + "' query with " + lht.toString() + " list handling...");
				switch(lht) {
				case IN_MEMORY:
					listHandler = ListHandlerFactory.create(criteria, sorting, lht, dataProvider);
					break;
				case IDLIST:
					try {
						listHandler = ListHandlerFactory.create(criteria, sorting, lht, dataProvider);
						Assert.fail("Able to create id list based list handler for a scalar named query!");
					}
					catch(final InvalidCriteriaException e) {
						// expected
					}
					break;
				case PAGE:
					listHandler = ListHandlerFactory.create(criteria, sorting, lht, dataProvider);
					break;
				default:
					throw new Error("Unhandled list handler type: " + lht.toString());
				}
				if(listHandler != null) {
					validateListHandler(listHandler, sorting);
				}
			}
			logger.debug("Validation complete");
		}
	}
}
