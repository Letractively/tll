/*
 * Created on - May 19, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 * 
 */
package com.tll.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.tll.criteria.Criteria;
import com.tll.dao.test.EntityDaoTestDecorator;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.INamedEntity;
import com.tll.model.ITimeStampEntity;
import com.tll.model.NameKey;
import com.tll.model.bk.BusinessKeyFactory;
import com.tll.model.bk.BusinessKeyNotDefinedException;
import com.tll.model.bk.IBusinessKey;
import com.tll.model.bk.IBusinessKeyDefinition;
import com.tll.model.egraph.EntityBeanFactory;
import com.tll.util.Comparator;

/**
 * AbstractEntityDaoTest
 * @param <R> the raw dao type
 * @param <D> the dao decorator type.
 * @author jpk
 */
@Test(groups = { "dao" })
public abstract class AbstractEntityDaoTest<R extends IEntityDao, D extends EntityDaoTestDecorator<R>> extends AbstractDbAwareTest {
	
	/**
	 * PkAndType
	 * @author jpk
	 */
	protected static final class PkAndType {
		public final Object pk;
		public final Class<? extends IEntity> entityType;
		
		/**
		 * Constructor
		 * @param entityType
		 * @param pk
		 */
		public PkAndType(Class<? extends IEntity> entityType, Object pk) {
			super();
			this.pk = pk;
			this.entityType = entityType;
		}
	} // PkAndType

	protected static final Sorting simpleIdSorting = new Sorting(new SortColumn(IEntity.PK_FIELDNAME));

	/**
	 * The test dao.
	 */
	protected final D dao;

	/**
	 * The entity handlers subject to testing.
	 */
	private IEntityDaoTestHandler<?>[] entityHandlers;

	/**
	 * The current entity handler being tested.
	 */
	protected IEntityDaoTestHandler<IEntity> entityHandler;

	/**
	 * Stack of test entity pks that are retained for proper datastore cleanup at
	 * the completion of dao testing for a particular entity type.
	 */
	private final Stack<PkAndType> testEntityRefStack;
	
	/**
	 * Constructor
	 * @param daoType
	 */
	public AbstractEntityDaoTest(Class<D> daoType) {
		super();
		try {
			dao = daoType.newInstance();
		}
		catch(final Exception e) {
			throw new IllegalArgumentException(e);
		}
		testEntityRefStack = new Stack<PkAndType>();
	}

	/**
	 * @return The dao test handlers subject to testing.
	 */
	protected abstract IEntityDaoTestHandler<?>[] getDaoTestHandlers();

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}

	@AfterClass(alwaysRun = true)
	public final void onAfterClass() {
		afterClass();
	}

	/**
	 * Called before any tests are run ensuring the db is in a test-ready state.
	 * Extended classes may override to tweak the behavior.
	 */
	protected void resetDb() {
		IDbShell dbShell = getDbShell();
		if(dbShell != null) {
			getDbShell().create();
			getDbShell().clearData();
		}
	}

	/**
	 * Responsible for setting the test db in a test-ready state and for building
	 * the test dependency injector. Extended classes may need to tweak this
	 * behavior and therefore it is overrideable.
	 */
	protected void prepare() {
		// reset the db
		resetDb();

		// build the test injector
		buildTestInjector();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final void beforeClass() {
		if(testEnv != null) {
			testEnv.setupTestEnvironment();
		}

		// get the dao test handlers
		entityHandlers = getDaoTestHandlers();
		if(entityHandlers == null || entityHandlers.length < 1) {
			throw new IllegalStateException("No entity dao handlers specified");
		}

		// prep the test db and build the test injector..
		prepare();

		dao.setRawDao((R) injector.getInstance(IEntityDao.class));
	}

	/**
	 * Run before dao test methods are invoked for a particular entity dao test
	 * handler.
	 */
	private void beforeEntityType() {
		// stub dependent entities for test entities of the current entity type
		entityHandler.persistDependentEntities();
	}

	/**
	 * Run <em>after</em> dao test methods are invoked for a particular entity dao
	 * test handler.
	 */
	private void afterEntityType() {
		// un-stub dependent entities for test entities of the current entity type
		entityHandler.purgeDependentEntities();
	}

	@Override
	protected void beforeMethod() {
		super.beforeMethod();

		// ensure a clean slate
		if(testEntityRefStack.size() > 0) {
			Assert.fail("Test entity ref stack is NOT empty!");
		}

		// start a new transaction for convenience and brevity
		getDbTrans().startTrans();
	}

	@Override
	protected void afterMethod() {
		super.afterMethod();

		// kill an open transaction
		if(getDbTrans().isTransStarted()) {
			getDbTrans().endTrans();
		}

		// teardown test entities..
		final boolean globalTransactions = getDbTrans().isGlobalTrans();
		if(testEntityRefStack.size() > 0) {
			for(final PkAndType n : testEntityRefStack) {
				assert n.entityType == entityHandler.entityClass();
				if(globalTransactions) getDbTrans().startTrans();
				try {
					final IEntity e = dao.load(n.entityType, n.pk);
					logger.debug("Tearing down test entity: " + e + "..");
					entityHandler.teardownTestEntity(e);
					if(globalTransactions) getDbTrans().setComplete();
				}
				catch(final EntityNotFoundException e) {
					// ok
				}
				finally {
					if(globalTransactions) getDbTrans().endTrans();
				}
			}
			testEntityRefStack.clear();
		}
	}

	/**
	 * @return The injected {@link IEntityFactory}
	 */
	protected final IEntityFactory<?> getEntityFactory() {
		return injector.getInstance(Key.get(new TypeLiteral<IEntityFactory<?>>() {}));
	}

	/**
	 * <strong>NOTE: </strong>The {@link EntityBeanFactory} is not available by
	 * default. It must be bound in a given module which is added via
	 * {@link #addModules(List)}.
	 * @return The injected {@link EntityBeanFactory}
	 */
	protected final EntityBeanFactory getEntityBeanFactory() {
		return injector.getInstance(EntityBeanFactory.class);
	}

	/**
	 * Provide a fresh entity instance of the ascribed type.
	 * @param <E>
	 * @return New entity instance
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <E extends IEntity> E getTestEntity() throws Exception {
		logger.debug("Creating test entity..");
		final E e = (E) getEntityBeanFactory().getEntityCopy(entityHandler.entityClass(), false);
		entityHandler.assembleTestEntity(e);
		if(BusinessKeyFactory.hasBusinessKeys(entityHandler.entityClass())) {
			entityHandler.makeUnique(e);
		}
		testEntityRefStack.add(new PkAndType(e.entityClass(), e.getId()));
		logger.debug("Test entity created: " + e);
		return e;
	}

	/**
	 * Used for testing dao methods involving entity collections.
	 * @param entityClass
	 * @param n
	 * @return a list of unique and generated test entities
	 * @throws Exception
	 */
	private List<IEntity> getNUniqueTestEntities(Class<IEntity> entityClass, int n) throws Exception {
		Assert.assertTrue(n > 0 && n <= 50, "Invalid number for unique entity generation: " + n
				+ ".  Specify a number between 1 and 50.");
		final List<IEntity> list = new ArrayList<IEntity>(n);
		for(int i = 1; i <= n; i++) {
			list.add(getTestEntity());
		}
		return list;
	}

	/**
	 * Retrieves an entity from the datastore ensuring a db hit as opposed to
	 * potentially retrieving it from the loaded persistence context.
	 * @param entityType the entity type
	 * @param pk The primary key
	 * @return The sought entity direct from the datastore.
	 */
	protected abstract <E extends IEntity> E getEntityFromDb(Class<E> entityType, Object pk);

	/**
	 * Run the dao test for all given entity types.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void test() throws Exception {

		for(final IEntityDaoTestHandler<?> handler : entityHandlers) {
			handler.init(dao, getEntityBeanFactory(), getDbTrans());
			entityHandler = (IEntityDaoTestHandler<IEntity>) handler;

			logger.debug("\n\n ====== Testing entity dao for entity type: " + handler.entityClass() + "...");
			beforeEntityType();

			// discover the dao test methods
			final ArrayList<Method> testMethods = new ArrayList<Method>();
			Class<?> clz = getClass();
			do {
				final Method[] dms = clz.getDeclaredMethods();
				if(dms != null) {
					for(final Method m : dms) {
						if(m.getName().startsWith("dao")) {
							testMethods.add(m);
						}
					}
				}
				clz = clz.getSuperclass();
			} while(clz != null);

			// run the dao test methods
			for(final Method method : testMethods) {
				beforeMethod();
				logger.debug("\n ** Testing " + method.getName() + " for entity type: " + handler.entityClass() + "...");
				method.setAccessible(true);
				method.invoke(this, (Object[]) null);
				afterMethod();
			}
			afterEntityType();
		}
	}

	// ***TESTS***

	/**
	 * Tests the dao impls transaction integrity.
	 * @throws Exception
	 */
	final void daoTransactionIntegrity() throws Exception {
		IEntity e = getTestEntity();

		e = dao.persist(e);
		getDbTrans().endTrans(); // rollback

		getDbTrans().startTrans();
		try {
			e = dao.load(e.entityClass(), e.getId());
			Assert.fail("Loaded entity that should not have been committed into the db due to trans rollback");
		}
		catch(final EntityNotFoundException ex) {
			// expected
		}
	}

	/**
	 * Test CRUD and find ops
	 * @throws Exception
	 */
	final void daoCRUDAndFind() throws Exception {
		IEntity e = getTestEntity();
		Assert.assertTrue(e.isNew(), "The created test entity is not new and should be");

		Object pk = null;

		// create
		e = dao.persist(e);
		getDbTrans().setComplete();
		getDbTrans().endTrans();
		pk = e.getId();
		Assert.assertNotNull(pk, "The created entities' primary key is null");
		Assert.assertTrue(!e.isNew(), "The created entity is new and shouldn't be");

		if(e instanceof ITimeStampEntity) {
			// verify time stamp
			Assert.assertNotNull(((ITimeStampEntity) e).getDateCreated(),
					"Created time stamp entity does not have a create date");
			Assert.assertNotNull(((ITimeStampEntity) e).getDateModified(),
					"Created time stamp entity does not have a modify date");
		}

		// retrieve
		getDbTrans().startTrans();
		e = dao.load(e.entityClass(), e.getId());
		Assert.assertNotNull(e, "The loaded entity is null");
		entityHandler.verifyLoadedEntityState(e);
		getDbTrans().setComplete(); // we need to do this for JDO in order to ensure a detached copy is made
		getDbTrans().endTrans();

		// update
		getDbTrans().startTrans();
		entityHandler.alterTestEntity(e);
		e = dao.persist(e);
		getDbTrans().setComplete();
		getDbTrans().endTrans();

		// find (update verify)
		getDbTrans().startTrans();
		e = getEntityFromDb(e.entityClass(), e.getId());
		Assert.assertNotNull(e, "The retrieved entity for update check is null");
		getDbTrans().setComplete();
		getDbTrans().endTrans();
		entityHandler.verifyEntityAlteration(e);

		if(e instanceof ITimeStampEntity) {
			// verify modify date
			final ITimeStampEntity tse = (ITimeStampEntity) e;
			Assert.assertTrue(tse.getDateModified() != null && tse.getDateCreated() != null
					&& tse.getDateModified().getTime() >= tse.getDateCreated().getTime(),
					"Updated time stamp entity does not an updated modify date");
		}

		// purge (delete)
		getDbTrans().startTrans();
		dao.purge(e);
		getDbTrans().setComplete();
		getDbTrans().endTrans();

		// verify purge
		getDbTrans().startTrans();
		getDbTrans().setComplete();
		try {
			e = getEntityFromDb(e.entityClass(), e.getId());
			Assert.assertNull(e, "The entity was not purged");
		}
		catch(final EntityNotFoundException ex) {
			// expected
		}
		finally {
			getDbTrans().endTrans();
		}
	}

	/**
	 * tests the load all method
	 * @throws Exception
	 */
	final void daoLoadAll() throws Exception {
		final List<? extends IEntity> list = dao.loadAll(entityHandler.entityClass());
		getDbTrans().endTrans();
		Assert.assertNotNull(list, "loadAll returned null");
	}

	@SuppressWarnings("unchecked")
	final void daoFindByName() throws Exception {
		IEntity e = getTestEntity();

		// create
		e = dao.persist(e);
		getDbTrans().setComplete();
		getDbTrans().endTrans();

		// retrieve by name key if applicable..
		if(INamedEntity.class.isAssignableFrom(entityHandler.entityClass())) {
			logger.debug("Perfoming actual find by name dao test..");
			final Class<INamedEntity> nec = (Class) entityHandler.entityClass();
			final String name = ((INamedEntity) e).getName();
			final NameKey<INamedEntity> nk = new NameKey<INamedEntity>(nec, name);
			getDbTrans().startTrans();
			try {
				e = dao.load(nk);
				// NOTE: as there may be other like enties having the same name, we only
				// test when we know there is no ambiguity
				Assert.assertNotNull(e, "Unable to load named entity by NameKey for entity type: "
						+ entityHandler.entityClass());
				entityHandler.verifyLoadedEntityState(e);
			}
			catch(final NonUniqueResultException ex) {
				// ok
			}
			catch(final/*QueryException*/Exception ex) {
				// ok - this means the INamedEntity doesn't have the getName() method
				// mapped to "name" which is possible in some cases where we impl
				// INamedEntity but map INamedEntity.getName() to another ORM property
				// and declare annotatively INamedEntity.getName() as @Transient
			}
			getDbTrans().endTrans();
		}
		else {
			logger.debug("Not performing daoFindByName() as the test entity type is not a named entity");
		}
	}

	/**
	 * Tests the find by ids method
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	final void daoFindByIds() throws Exception {
		IEntity e = getTestEntity();
		e = dao.persist(e);
		getDbTrans().setComplete();
		getDbTrans().endTrans();

		getDbTrans().startTrans();
		Assert.assertNotNull(e, "Null generated test entity");
		final ArrayList<Object> ids = new ArrayList<Object>(1);
		ids.add(e.getId());
		final List<IEntity> list = dao.findByPrimaryKeys(entityHandler.entityClass(), ids, null);
		getDbTrans().endTrans();
		Assert.assertTrue(list != null, "find by ids returned null list");
		Assert.assertTrue(list.size() == 1, "find by ids returned list of size: " + list.size());
	}

	/**
	 * Tests id-based list handler related methods
	 * @throws Exception
	 */
	final void daoGetIdsAndEntities() throws Exception {
		if(!entityHandler.supportsPaging()) {
			logger.info("Not testing Id List Handler support for entity type: " + entityHandler.entityClass());
			return;
		}

		final List<IEntity> entityList = getNUniqueTestEntities(entityHandler.entityClass(), 5);
		final ArrayList<Object> idList = new ArrayList<Object>(5);
		for(IEntity e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
		}
		getDbTrans().setComplete();
		getDbTrans().endTrans();
		final Criteria<IEntity> criteria = new Criteria<IEntity>(entityHandler.entityClass());
		criteria.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

		// get ids
		getDbTrans().startTrans();
		final List<?> dbIdList = dao.getPrimaryKeys(criteria, simpleIdSorting);
		Assert.assertTrue(EntityUtil.entitiesAndIdsEquals(dbIdList, entityList), "getIds list is empty or has incorrect ids");
		getDbTrans().endTrans();

		// get entities
		getDbTrans().startTrans();
		final List<IEntity> dbEntityList = dao.findByPrimaryKeys(entityHandler.entityClass(), idList, null);
		Assert.assertNotNull(idList, "getEntities list is null");
		Assert.assertTrue(EntityUtil.entitiesAndIdsEquals(idList, dbEntityList), "getEntities list is empty or has incorrect ids");
		getDbTrans().endTrans();
	}

	/**
	 * Tests page-based list handler related methods
	 * @throws Exception
	 */
	final void daoPage() throws Exception {
		if(!entityHandler.supportsPaging()) {
			logger.info("Not testing daoPage for: " + entityHandler.entityClass());
			return;
		}

		final List<IEntity> entityList = getNUniqueTestEntities(entityHandler.entityClass(), 5);
		final ArrayList<Object> idList = new ArrayList<Object>(5);
		for(IEntity e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
		}
		getDbTrans().setComplete();
		getDbTrans().endTrans();

		final Criteria<IEntity> crit = new Criteria<IEntity>(entityHandler.entityClass());
		crit.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

		getDbTrans().startTrans();
		IPageResult<SearchResult> page = dao.getPage(crit, simpleIdSorting, 0, 2);
		Assert.assertTrue(page != null && page.getPageList() != null && page.getPageList().size() == 2,
				"Empty or invalid number of initial page elements");
		getDbTrans().endTrans();

		getDbTrans().startTrans();
		page = dao.getPage(crit, simpleIdSorting, 2, 2);
		Assert.assertTrue(page != null && page.getPageList() != null && page.getPageList().size() == 2,
				"Empty or invalid number of subsequent page elements");
		getDbTrans().endTrans();

		getDbTrans().startTrans();
		page = dao.getPage(crit, simpleIdSorting, 4, 2);
		Assert.assertTrue(page != null && page.getPageList() != null && page.getPageList().size() == 1,
				"Empty or invalid number of last page elements");
		getDbTrans().endTrans();
	}

	/**
	 * Tests for the proper throwing of {@link EntityExistsException} in the dao.
	 * @throws Exception
	 */
	final void daoDuplicationException() throws Exception {
		// IMPT: don't test run this dao test if there are no business keys defined
		// for the current entity type!!
		if(!BusinessKeyFactory.hasBusinessKeys(entityHandler.entityClass())) {
			return;
		}

		IEntity e = getTestEntity();
		e = dao.persist(e);
		getDbTrans().setComplete();
		getDbTrans().endTrans();

		getDbTrans().startTrans();
		final IEntity e2 = getTestEntity();
		EntityUtil.ensureNonUnique(e, e2);
		try {
			dao.persist(e2);
			getDbTrans().setComplete();
			getDbTrans().endTrans();
			Assert.fail("A duplicate exception should have occurred for entity type: " + entityHandler.entityClass());
		}
		catch(final EntityExistsException de) {
			// expected
			logger.debug("Expected exception: " + de.getMessage());
		}
	}

	final void daoPurgeNonExistantEntity() throws Exception {
		IEntity e = getTestEntity();
		final Object pk = e.getId();

		// save it first
		e = dao.persist(e);

		getDbTrans().setComplete();
		getDbTrans().endTrans();
		getDbTrans().startTrans();

		// delete it (with key)
		dao.purge(e.entityClass(), pk);

		getDbTrans().setComplete();
		getDbTrans().endTrans();
		getDbTrans().startTrans();

		try {
			dao.purge(e);
			Assert.fail("An EntityNotFoundException should have occurred (" + pk + ")");
		}
		catch(final EntityNotFoundException ex) {
			logger.debug("Expected exception: " + ex.getMessage());
		}
	}

	final void daoFindEntityByPrimaryKeyCriteria() throws Exception {
		// persist the target test entity
		IEntity e = getTestEntity();
		e = dao.persist(e);
		getDbTrans().setComplete();
		getDbTrans().endTrans();

		final Criteria<IEntity> c = new Criteria<IEntity>(entityHandler.entityClass());
		c.getPrimaryGroup().addCriterion(e.entityClass(), e.getId());
		getDbTrans().startTrans();
		final IEntity re = dao.findEntity(c);
		Assert.assertTrue(re != null);
		Assert.assertEquals(re, e);
	}

	final void daoFindEntityByBusinessKeyCriteria() throws Exception {
		try {
			final IBusinessKeyDefinition<IEntity>[] bkdefs = BusinessKeyFactory.definitions(entityHandler.entityClass());

			// persist the target test entity
			IEntity e = getTestEntity();
			e = dao.persist(e);
			getDbTrans().setComplete();
			getDbTrans().endTrans();

			getDbTrans().startTrans();
			final Criteria<IEntity> c = new Criteria<IEntity>(entityHandler.entityClass());
			for(final IBusinessKeyDefinition<IEntity> bkdef : bkdefs) {
				final IBusinessKey<IEntity> bk = BusinessKeyFactory.create(e, bkdef);
				c.getPrimaryGroup().addCriterion(bk, true);
			}
			final IEntity re = dao.findEntity(c);
			Assert.assertTrue(re != null);
			Assert.assertEquals(re, e);
		}
		catch(final BusinessKeyNotDefinedException e) {
			// ok skip
		}
	}

	@SuppressWarnings("unchecked")
	final void daoFindEntityByName() throws Exception {
		if(INamedEntity.class.isAssignableFrom(entityHandler.entityClass())) {
			// persist the target test entity
			INamedEntity e = getTestEntity();
			e = dao.persist(e);
			getDbTrans().setComplete();
			getDbTrans().endTrans();

			getDbTrans().startTrans();
			final Criteria<IEntity> c = new Criteria<IEntity>(entityHandler.entityClass());
			c.getPrimaryGroup().addCriterion(
					new NameKey(e.entityClass(), e.getName(), entityHandler.getActualNameProperty()), true);
			final IEntity re = dao.findEntity(c);
			Assert.assertTrue(re != null);
			if(re != null) Assert.assertEquals(re, e);
		}
	}

	@SuppressWarnings("unchecked")
	final void daoFindEntityByCriteria() throws Exception {
		final Criteria c = entityHandler.getTestCriteria();
		if(c == null) return; // ok

		// persist the target test entity
		IEntity e = getTestEntity();
		e = dao.persist(e);
		getDbTrans().setComplete();
		getDbTrans().endTrans();

		getDbTrans().startTrans();
		// NOTE: we only verify that the method executes w/o exceptions
		dao.findEntities(c, entityHandler.getTestSorting());
		getDbTrans().endTrans();
	}

	// TODO do we want to support named queries now in light of the fact that we
	// offer an object db dao impl now?
	/*
	final void daoTestSelectNamedQueries() throws Exception {
		final ISelectNamedQueryDef[] queryDefs = entityHandler.getQueriesToTest();
		if(queryDefs == null) return;
		for(final ISelectNamedQueryDef qdef : queryDefs) {
			final IQueryParam[] params = entityHandler.getParamsForTestQuery(qdef);
			final List<IQueryParam> list = params == null ? null : Arrays.asList(params);
			final Criteria<IEntity> c = new Criteria<IEntity>(qdef, list);
			final List<SearchResult> result = dao.find(c, entityHandler.getSortingForTestQuery(qdef));
			// Assert.assertTrue(result != null && result.size() > 0, "No named query results");
			// for now, since we can't guarantee results based on the varied nature of
			// the query defs, we first check for resutls and pass if there aren't any
			if(result != null && result.size() > 0) {
				for(final SearchResult sr : result) {
					if(qdef.isScalar()) {
						Assert.assertTrue(sr.getElement() instanceof IScalar, "No scalar in scalar search result");
					}
					else {
						Assert.assertTrue(sr.getElement() instanceof IEntity, "No entity in entity search result");
					}
				}
			}
		}
	}
	 */
}
