/**
 * The Logic Lab
 * @author jpk
 * Apr 30, 2008
 */
package com.tll.listhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.criteria.Criteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.criteria.QueryParam;
import com.tll.criteria.SelectNamedQueries;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbShell;
import com.tll.dao.SearchResult;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.di.Db4oDaoModule;
import com.tll.di.EntityServiceFactoryModule;
import com.tll.di.SmbizDb4oDaoModule;
import com.tll.di.SmbizEGraphModule;
import com.tll.di.SmbizModelModule;
import com.tll.di.test.Db4oDbShellModule;
import com.tll.model.IEntity;
import com.tll.model.schema.PropertyType;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * NamedQueriesTest
 * @author jpk
 */
@Test(groups = { "listhandler", "namedqueries" })
public class NamedQueriesTest extends AbstractDbAwareTest {

	private static final Map<SelectNamedQueries, SortColumn> querySortBindings =
		new HashMap<SelectNamedQueries, SortColumn>();

	private static final Map<SelectNamedQueries, List<IQueryParam>> queryParamsBindings =
		new HashMap<SelectNamedQueries, List<IQueryParam>>();

	static {
		for(final SelectNamedQueries nq : SelectNamedQueries.values()) {
			switch(nq) {
				case ISP_LISTING:
					querySortBindings.put(nq, new SortColumn("dateCreated"));
					queryParamsBindings.put(nq, null);
					break;
				case MERCHANT_LISTING: {
					querySortBindings.put(nq, new SortColumn("dateCreated"));
					final List<IQueryParam> list = new ArrayList<IQueryParam>();
					list.add(new QueryParam("ispId", PropertyType.INT, Integer.valueOf(2)));
					queryParamsBindings.put(nq, list);
					break;
				}
				case CUSTOMER_LISTING: {
					querySortBindings.put(nq, new SortColumn("dateCreated", "c"));
					final List<IQueryParam> list = new ArrayList<IQueryParam>();
					list.add(new QueryParam("merchantId", PropertyType.INT, Integer.valueOf(2)));
					queryParamsBindings.put(nq, list);
					break;
				}
				case INTERFACE_SUMMARY_LISTING:
				case INTERFACES:
					querySortBindings.put(nq, new SortColumn("code", "intf"));
					queryParamsBindings.put(nq, null);
					break;

					// warn of unhandled defined named queries!
				default:
					throw new IllegalStateException("Unhandled named query: " + nq);
			}
		}
	}

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}

	@Override
	protected void beforeClass() {
		// create the db shell first (before test injector creation) to avoid db4o
		// file lock when objectcontainer is instantiated
		final Config cfg = getConfig();
		cfg.setProperty(Db4oDaoModule.ConfigKeys.DB4O_EMPLOY_SPRING_TRANSACTIONS.getKey(), false);
		final Injector i = buildInjector(new SmbizDb4oDaoModule(cfg), new Db4oDbShellModule(), new SmbizModelModule(), new SmbizEGraphModule());
		final IDbShell dbs = i.getInstance(IDbShell.class);
		dbs.restub();

		cfg.setProperty(Db4oDaoModule.ConfigKeys.DB4O_EMPLOY_SPRING_TRANSACTIONS.getKey(), true);
		super.beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new SmbizModelModule());
		modules.add(new SmbizEGraphModule());
		modules.add(new SmbizDb4oDaoModule(getConfig()));
		modules.add(new Db4oDbShellModule());
		modules.add(new EntityServiceFactoryModule());
	}

	/**
	 * @param <E>
	 * @param entityClass
	 * @return The {@link IListingDataProvider} subject to testing.
	 */
	protected <E extends IEntity> IListingDataProvider getListHandlerDataProvider(Class<E> entityClass) {
		return injector.getInstance(IEntityServiceFactory.class).instanceByEntityType(entityClass);
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
		assert listHandler.size() > 0 : "No list handler elements exist";
	}

	@SuppressWarnings("unchecked")
	public void test() throws Exception {

		IListingDataProvider dataProvider;
		Criteria<IEntity> criteria;

		// iterator through all defined select named queries
		for(final SelectNamedQueries nq : querySortBindings.keySet()) {
			dataProvider = getListHandlerDataProvider((Class<IEntity>) nq.getEntityType());
			criteria = new Criteria<IEntity>(nq, queryParamsBindings.get(nq));
			final Sorting sorting = new Sorting(querySortBindings.get(nq));

			// test for all list handler types
			for(final ListHandlerType lht : ListHandlerType.values()) {
				IListHandler<SearchResult<?>> listHandler = null;
				logger.debug("Validating '" + nq.toString() + "' query with " + lht.toString() + " list handling...");
				switch(lht) {
					case COLLECTION:
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
