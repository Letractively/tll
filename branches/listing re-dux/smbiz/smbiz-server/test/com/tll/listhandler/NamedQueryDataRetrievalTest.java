/**
 * The Logic Lab
 * @author jpk
 * Apr 30, 2008
 */
package com.tll.listhandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.criteria.CriteriaFactory;
import com.tll.criteria.ICriteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.criteria.SelectNamedQuery;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.guice.DaoModule;
import com.tll.guice.EntityServiceModule;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.schema.PropertyType;
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

	private static final Map<SelectNamedQuery, Set<IQueryParam>> queryParamsBindings =
			new HashMap<SelectNamedQuery, Set<IQueryParam>>();

	private static class QueryParam implements IQueryParam {

		private String propertyName;
		private PropertyType type;
		private Object value;

		/**
		 * Constructor
		 * @param propertyName
		 * @param type
		 * @param value
		 */
		public QueryParam(String propertyName, PropertyType type, Object value) {
			super();
			this.propertyName = propertyName;
			this.type = type;
			this.value = value;
		}

		public String descriptor() {
			return "Query Param";
		}

		public String getPropertyName() {
			return propertyName;
		}

		public PropertyType getType() {
			return type;
		}

		public Object getValue() {
			return value;
		}
	}

	static {
		for(SelectNamedQuery nq : SelectNamedQuery.values()) {
			switch(nq) {
				case ISP_LISTING:
					querySortBindings.put(nq, new SortColumn("dateCreated", "i"));
					queryParamsBindings.put(nq, null);
					break;
				case MERCHANT_LISTING: {
					querySortBindings.put(nq, new SortColumn("dateCreated", "m"));
					Set<IQueryParam> set = new HashSet<IQueryParam>();
					set.add(new QueryParam("ispId", PropertyType.INT, new Integer(2)));
					queryParamsBindings.put(nq, set);
					break;
				}
				case CUSTOMER_LISTING: {
					querySortBindings.put(nq, new SortColumn("dateCreated", "ca"));
					Set<IQueryParam> set = new HashSet<IQueryParam>();
					set.add(new QueryParam("merchantId", PropertyType.INT, new Integer(2)));
					queryParamsBindings.put(nq, set);
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

	@BeforeClass(alwaysRun = true)
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
		assert sorting != null && listHandler.getSorting() != null && listHandler.getSorting().equals(sorting) : "List handler sorting differs";
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
			for(ListHandlerType lht : ListHandlerType.values()) {
				IListHandler<SearchResult<IEntity>> listHandler = null;
				logger.debug("Validating '" + nq.toString() + "' query with " + lht.toString() + " list handling...");
				switch(lht) {
					case COLLECTION:
						listHandler = ListHandlerFactory.create(criteria, sorting, lht, pageSize, dataProvider);
						break;
					case IDLIST:
						try {
							listHandler = ListHandlerFactory.create(criteria, sorting, lht, pageSize, dataProvider);
							Assert.fail("Able to create id list based list handler for a scalar named query!");
						}
						catch(InvalidCriteriaException e) {
							// expected
						}
						break;
					case PAGE:
						listHandler = ListHandlerFactory.create(criteria, sorting, lht, pageSize, dataProvider);
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