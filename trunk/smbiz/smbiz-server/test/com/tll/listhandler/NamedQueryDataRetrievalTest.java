/**
 * The Logic Lab
 * @author jpk
 * Apr 30, 2008
 */
package com.tll.listhandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.criteria.CriteriaFactory;
import com.tll.criteria.ICriteria;
import com.tll.criteria.SelectNamedQuery;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.guice.DaoModule;
import com.tll.guice.EntityServiceModule;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.util.EnumUtil;

/**
 * NamedQueryDataRetrievalTest
 * @author jpk
 */
@Test(groups = "listhandler")
public class NamedQueryDataRetrievalTest extends DbTest {

	private static final Map<SelectNamedQuery, SortColumn> querySortBindings =
			new HashMap<SelectNamedQuery, SortColumn>();

	private static final Map<SelectNamedQuery, Map<String, String>> queryParamsBindings =
			new HashMap<SelectNamedQuery, Map<String, String>>();

	static {
		for(SelectNamedQuery nq : SelectNamedQuery.values()) {
			switch(nq) {
				case ISP_LISTING:
					querySortBindings.put(nq, new SortColumn("dateCreated", "i"));
					queryParamsBindings.put(nq, null);
					break;
				case MERCHANT_LISTING: {
					querySortBindings.put(nq, new SortColumn("dateCreated", "m"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("ispId", "1");
					queryParamsBindings.put(nq, map);
					break;
				}
				case CUSTOMER_LISTING: {
					querySortBindings.put(nq, new SortColumn("dateCreated", "ca"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("merchantId", "1");
					queryParamsBindings.put(nq, map);
					break;
				}
				case INTERFACE_SUMMARY_LISTING:
				case INTERFACES:
					querySortBindings.put(nq, new SortColumn("code", "intf"));
					queryParamsBindings.put(nq, null);
					break;
			}
		}
	}

	protected DaoMode daoMode;

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

		if(daoMode == DaoMode.ORM) {
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

	/**
	 * Does simple validation on the given list handler.
	 * @param <T>
	 * @param listHandler
	 * @param sorting
	 * @throws Exception
	 */
	protected <T> void validateListHandler(IListHandler<T> listHandler, Sorting sorting) throws Exception {
		assert listHandler != null : "The list handler is null";
		assert listHandler.size() > 0 : "No list handler elements exist";
		assert listHandler.getSorting() != null && listHandler.getSorting().equals(sorting) : "List hanler sorting differs";
		assert listHandler.getElement(0) != null : "Unable to obtain the first list handler element";
	}

	public void test() throws Exception {

		final int pageSize = 2;
		IListHandlerDataProvider<IEntity> dataProvider;
		ICriteria<? extends IEntity> criteria;

		// iterator through all defined select named queries
		for(SelectNamedQuery nq : querySortBindings.keySet()) {
			dataProvider = getListHandlerDataProvider(EntityUtil.entityClassFromType(nq.getEntityType()));
			criteria = CriteriaFactory.buildQueryCriteria(nq, queryParamsBindings.get(nq));
			Sorting sorting = new Sorting(querySortBindings.get(nq));

			// test for all list handler types
			IListHandler<SearchResult<IEntity>> listHandler;
			for(ListHandlerType lht : ListHandlerType.values()) {
				listHandler = ListHandlerFactory.create(criteria, sorting, lht, pageSize, dataProvider);
				validateListHandler(listHandler, sorting);
			}
		}
	}
}
