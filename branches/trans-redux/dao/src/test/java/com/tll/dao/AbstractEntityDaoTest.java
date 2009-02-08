/*
 * Created on - May 19, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 * 
 */
package com.tll.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

import org.hibernate.QueryException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.tll.DbTest;
import com.tll.criteria.Comparator;
import com.tll.criteria.Criteria;
import com.tll.criteria.ICriteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.DaoModule;
import com.tll.di.DbDialectModule;
import com.tll.di.DbShellModule;
import com.tll.di.MockEntityFactoryModule;
import com.tll.di.ModelModule;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.INamedEntity;
import com.tll.model.ITimeStampEntity;
import com.tll.model.key.BusinessKeyFactory;
import com.tll.model.key.BusinessKeyNotDefinedException;
import com.tll.model.key.BusinessKeyPropertyException;
import com.tll.model.key.BusinessKeyUtil;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.model.mock.MockEntityFactory;
import com.tll.util.EnumUtil;

/**
 * AbstractEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao" })
@SuppressWarnings( {
	"synthetic-access", "unused" })
public abstract class AbstractEntityDaoTest extends DbTest {

	/**
	 * EntityDaoTestDecorator - Decorates {@link IEntityDao} to:
	 * <ol>
	 * <li>Manage dao testing life-cycle and cleanup.
	 * <li>Prevent the dao tests from degrading as the code base naturally changes
	 * over time. I.e.: any IEntityDao method signature change is forced upon the
	 * dao testing.
	 * </ol>
	 * @author jpk
	 */
	private final class EntityDaoTestDecorator implements IEntityDao {

		private IEntityDao rawDao;

		public IEntityDao getRawDao() {
			return rawDao;
		}

		public void setRawDao(IEntityDao rawDao) {
			this.rawDao = rawDao;
		}

		@Override
		public <R extends IEntity> R persist(R entity) {
			return rawDao.persist(entity);
		}

		@Override
		public <R extends IEntity> R load(PrimaryKey<R> key) {
			return rawDao.load(key);
		}

		@Override
		public <R extends IEntity> R load(IBusinessKey<R> key) {
			return rawDao.load(key);
		}

		@Override
		public <R extends IEntity> void purge(R entity) {
			rawDao.purge(entity);
		}

		@Override
		public <R extends IEntity> List<R> loadAll(Class<R> entityType) {
			return rawDao.loadAll(entityType);
		}

		@Override
		public <R extends IEntity> void purgeAll(Collection<R> entities) {
			rawDao.purgeAll(entities);
		}

		@Override
		public <R extends IEntity> Collection<R> persistAll(Collection<R> entities) {
			return rawDao.persistAll(entities);
		}

		@Override
		public <R extends IEntity> List<R> findEntities(ICriteria<R> criteria, Sorting sorting)
				throws InvalidCriteriaException {
			return rawDao.findEntities(criteria, sorting);
		}

		@Override
		public <R extends IEntity> R findEntity(ICriteria<R> criteria) throws InvalidCriteriaException {
			return rawDao.findEntity(criteria);
		}

		@Override
		public <R extends IEntity> List<R> findByIds(Class<R> entityType, List<Integer> ids, Sorting sorting) {
			return rawDao.findByIds(entityType, ids, sorting);
		}

		@Override
		public <R extends IEntity> List<Integer> getIds(ICriteria<R> criteria, Sorting sorting)
				throws InvalidCriteriaException {
			return rawDao.getIds(criteria, sorting);
		}

		@Override
		public <R extends IEntity> List<R> getEntitiesFromIds(Class<R> entityClass, Collection<Integer> ids, Sorting sorting) {
			return rawDao.getEntitiesFromIds(entityClass, ids, sorting);
		}

		@Override
		public <R extends IEntity> List<SearchResult<R>> find(ICriteria<R> criteria, Sorting sorting)
				throws InvalidCriteriaException {
			return rawDao.find(criteria, sorting);
		}

		@Override
		public <R extends IEntity> IPageResult<SearchResult<R>> getPage(ICriteria<R> criteria, Sorting sorting, int offset,
				int pageSize) throws InvalidCriteriaException {
			return rawDao.getPage(criteria, sorting, offset, pageSize);
		}

		@Override
		public int executeQuery(String queryName, IQueryParam[] params) {
			return rawDao.executeQuery(queryName, params);
		}

		@Override
		public void clear() {
			rawDao.clear();
		}

		@Override
		public void flush() {
			rawDao.flush();
		}

		@Override
		public <N extends INamedEntity> N load(NameKey<N> nameKey) {
			return rawDao.load(nameKey);
		}
	}

	/**
	 * Compare a clc of entity ids and entites ensuring the id list is referenced
	 * w/in the entity list
	 * @param ids
	 * @param entities
	 * @return
	 */
	protected static final <E extends IEntity> boolean entitiesAndIdsEquals(Collection<Integer> ids,
			Collection<E> entities) {
		if(ids == null || entities == null) {
			return false;
		}
		if(ids.size() != entities.size()) {
			return false;
		}
		for(final E e : entities) {
			boolean found = false;
			for(final Integer id : ids) {
				if(id.equals(e.getId())) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Ensures two entities are non-unique by business key.
	 * @param e1
	 * @param e2
	 */
	protected static final <E extends IEntity> void ensureNonUnique(E e1, E e2) {
		if(e2 instanceof ITimeStampEntity) {
			((ITimeStampEntity) e2).setDateCreated(((ITimeStampEntity) e2).getDateCreated());
			((ITimeStampEntity) e2).setDateModified(((ITimeStampEntity) e2).getDateModified());
		}
		try {
			BusinessKeyUtil.apply(e2, BusinessKeyFactory.create(e1));
		}
		catch(BusinessKeyNotDefinedException e) {
			// assume ok
		}
		catch(BusinessKeyPropertyException e) {
			Assert.fail(e.getMessage());
		}
	}

	protected static final Sorting simpleIdSorting = new Sorting(new SortColumn(IEntity.PK_FIELDNAME));

	/**
	 * The entity handlers subject to testing.
	 */
	private Collection<IEntityDaoTestHandler<IEntity>> entityHandlers;

	/**
	 * The current entity handler being tested.
	 */
	private IEntityDaoTestHandler<IEntity> entityHandler;

	/**
	 * Stack of test entity pks that are retained for proper datastore cleanup at
	 * the completion of dao testing for a particular entity type.
	 */
	private final Stack<PrimaryKey<IEntity>> testEntityRefStack = new Stack<PrimaryKey<IEntity>>();

	private int numEntities;

	/**
	 * The test dao.
	 */
	private final EntityDaoTestDecorator dao = new EntityDaoTestDecorator();

	/**
	 * Constructor
	 */
	public AbstractEntityDaoTest() {
		super(false, true);
	}

	/**
	 * @return The dao test handlers subject to testing.
	 */
	protected abstract Collection<IEntityDaoTestHandler<IEntity>> getDaoTestHandlers();

	@Override
	protected final void addModules(List<Module> modules) {
		modules.add(new ModelModule());
		modules.add(new MockEntityFactoryModule());
		super.addModules(modules);
		modules.add(new DaoModule());
	}

	@BeforeClass(alwaysRun = true)
	@Parameters(value = "daoMode")
	public final void onBeforeClass(String daoModeStr) {
		setDaoMode(EnumUtil.fromString(DaoMode.class, daoModeStr));
		beforeClass();
	}

	@AfterClass(alwaysRun = true)
	public final void onAfterClass() {
		afterClass();
	}

	@Override
	protected void beforeClass() {

		// get the dao test handlers
		entityHandlers = getDaoTestHandlers();
		if(entityHandlers == null || entityHandlers.size() < 1) {
			throw new IllegalStateException("No entity dao handlers specified");
		}

		if(getDaoMode() == DaoMode.ORM) {
			// create a db shell to ensure db exists and stubbed
			Injector i = Guice.createInjector(Stage.DEVELOPMENT, new DbDialectModule(), new DbShellModule());
			DbShell dbShell = i.getInstance(DbShell.class);
			dbShell.create();
			dbShell.clear();
		}

		// build the injector
		buildInjector();
		assert injector != null;

		dao.setRawDao(injector.getInstance(IEntityDao.class));
	}

	@Override
	protected void afterClass() {
		super.afterClass();
	}

	/**
	 * Run before dao test methods are invoked for a particular entity dao test
	 * handler.
	 */
	private void beforeEntityType() {
		// mock dao mode only - retain the number of entities before any testing
		// happens for this entity type
		if(dao.getRawDao() instanceof com.tll.dao.mock.EntityDao) {
			Collection<?> eclc =
					((com.tll.dao.mock.EntityDao) dao.getRawDao()).getEntityGraph()
							.getEntitiesByType(entityHandler.entityClass());
			numEntities = eclc == null ? 0 : eclc.size();
		}
		
		// stub dependent entities for test entities of the current entity type
		startNewTransaction();
		try {
			entityHandler.persistDependentEntities();
			setComplete();
		}
		finally {
			endTransaction();
		}
	}

	/**
	 * Run <em>after</em> dao test methods are invoked for a particular entity dao
	 * test handler.
	 */
	private void afterEntityType() {
		// un-stub dependent entities for test entities of the current entity type
		startNewTransaction();
		try {
			entityHandler.purgeDependentEntities();
			setComplete();
		}
		finally {
			endTransaction();
		}
		
		// mock dao mode only - verify the number of entities in the mock dao's
		// object graph matches the retained number prior to testing for the current
		// entity type
		if(dao.getRawDao() instanceof com.tll.dao.mock.EntityDao) {
			Collection<?> eclc =
					((com.tll.dao.mock.EntityDao) dao.getRawDao()).getEntityGraph()
							.getEntitiesByType(entityHandler.entityClass());
			int afterNumEntities = eclc == null ? 0 : eclc.size();
			Assert.assertEquals(afterNumEntities, numEntities, entityHandler + " dao test handler didn't clean up properly!");
		}
	}

	@Override
	protected final void beforeMethod() {
		super.beforeMethod();
		// start a new transaction for convenience and brevity
		startNewTransaction();
	}

	@Override
	protected final void afterMethod() {
		super.afterMethod();
		
		// kill an open transaction
		if(isTransStarted()) {
			endTransaction();
		}
		
		// teardown test entities..
		if(testEntityRefStack.size() > 0) {
			for(PrimaryKey<IEntity> pk : testEntityRefStack) {
				assert pk.getType() == entityHandler.entityClass();
				startNewTransaction();
				try {
					// logger.debug("Tearing down test entity: " + e + "..");
					entityHandler.teardownTestEntity(dao.load(pk));
					setComplete();
				}
				catch(EntityNotFoundException e) {
					// ok
				}
				finally {
					endTransaction();
				}
			}
			testEntityRefStack.clear();
		}
	}

	/**
	 * @return The injected {@link IEntityFactory}
	 */
	private IEntityFactory getEntityFactory() {
		return injector.getInstance(IEntityFactory.class);
	}

	/**
	 * <strong>NOTE: </strong>The {@link MockEntityFactory} is not available by
	 * default. It must be bound in a given module which is added via
	 * {@link #addModules(List)}.
	 * @return The injected {@link MockEntityFactory}
	 */
	private MockEntityFactory getMockEntityFactory() {
		return injector.getInstance(MockEntityFactory.class);
	}

	/**
	 * Provide a fresh entity instance of the ascribed type.
	 * @return New entity instance
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private <E extends IEntity> E getTestEntity() throws Exception {
		// logger.debug("Creating test entity..");
		final E e = (E) getMockEntityFactory().getEntityCopy(entityHandler.entityClass(), false);
		entityHandler.assembleTestEntity(e);
		if(BusinessKeyFactory.hasBusinessKeys(entityHandler.entityClass())) {
			entityHandler.makeUnique(e);
		}
		testEntityRefStack.add(new PrimaryKey<IEntity>(e));
		return e;
	}

	/**
	 * Used for testing dao methods involving entity collections.
	 * @param entityClass
	 * @param n
	 * @return a list of unique and generated test entities
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
	 * @param key The primary key
	 * @return The sought entity direct from the datastore.
	 */
	private IEntity getEntityFromDb(PrimaryKey<IEntity> key) {
		return DbTest.getEntityFromDb(dao, key);
	}
	
	/**
	 * Run the dao test for all given entity types.
	 * @throws Exception
	 */
	@Test
	public final void test() throws Exception {

		for(IEntityDaoTestHandler<IEntity> handler : entityHandlers) {
			handler.init(dao, getMockEntityFactory());
			entityHandler = handler;
			
			logger.debug("Testing entity dao for entity type: " + handler.entityClass() + "...");
			beforeEntityType();
			// run all tests
			for(Method method : AbstractEntityDaoTest.class.getDeclaredMethods()) {
				if(method.getName().startsWith("dao")) {
					beforeMethod();
					logger.debug("Testing " + method.getName() + " for entity type: " + handler.entityClass() + "...");
					method.invoke(this, (Object[]) null);
					afterMethod();
				}
			}
			afterEntityType();
		}
	}

	// ***TESTS***

	@SuppressWarnings("unchecked")
	private void daoFindByName() throws Exception {
		IEntity e = getTestEntity();

		// create
		e = dao.persist(e);
		setComplete();
		endTransaction();
		Integer persistentId = e.getId();

		// retrieve by name key if applicable..
		if(INamedEntity.class.isAssignableFrom(entityHandler.entityClass())) {
			logger.debug("Perfoming actual find by name dao test..");
			Class<?> nec = entityHandler.entityClass();
			String name = ((INamedEntity) e).getName();
			NameKey<INamedEntity> nk = new NameKey(nec, name);
			startNewTransaction();
			try {
				e = dao.load(nk);
				// NOTE: as there may be other like enties having the same name, we only
				// test when we know there is no ambiguity
				Assert.assertNotNull(e, "Unable to load named entity by NameKey for entity type: "
						+ entityHandler.entityClass());
				entityHandler.verifyLoadedEntityState(e);
			}
			catch(NonUniqueResultException ex) {
				// ok
			}
			catch(QueryException ex) {
				// ok - this means the INamedEntity doesn't have the getName() method
				// mapped to "name" which is possible in some cases where we impl
				// INamedEntity but map INamedEntity.getName() to another ORM property
				// and declare annotatively INamedEntity.getName() as @Transient
			}
			endTransaction();
		}
		else {
			logger.debug("Not performing daoFindByName() as the test entity type is not a named entity");
		}
	}

	/**
	 * Test CRUD and find ops
	 * @throws Exception
	 */
	private void daoCRUDAndFind() throws Exception {
		IEntity e = getTestEntity();
		Assert.assertTrue(e.isNew(), "The created test entity is not new and should be");
		
		Integer persistentId = null;

		// create
		e = dao.persist(e);
		setComplete();
		endTransaction();
		persistentId = e.getId();
		Assert.assertNotNull(e.getId(), "The created entities' id is null");
		Assert.assertTrue(!e.isNew(), "The created entity is new and shouldn't be");

		if(e instanceof ITimeStampEntity) {
			// verify time stamp
			Assert.assertNotNull(((ITimeStampEntity) e).getDateCreated(),
					"Created time stamp entity does not have a create date");
			Assert.assertNotNull(((ITimeStampEntity) e).getDateModified(),
					"Created time stamp entity does not have a modify date");
		}

		// retrieve
		startNewTransaction();
		e = dao.load(new PrimaryKey<IEntity>(entityHandler.entityClass(), persistentId));
		Assert.assertNotNull(e, "The loaded entity is null");
		entityHandler.verifyLoadedEntityState(e);
		endTransaction();

		// update
		startNewTransaction();
		entityHandler.alterTestEntity(e);
		e = dao.persist(e);
		setComplete();
		endTransaction();

		// find (update verify)
		startNewTransaction();
		e = getEntityFromDb(new PrimaryKey<IEntity>(e));
		Assert.assertNotNull(e, "The retrieved entity for update check is null");
		endTransaction();
		entityHandler.verifyEntityAlteration(e);

		if(e instanceof ITimeStampEntity) {
			// verify modify date
			final ITimeStampEntity tse = (ITimeStampEntity) e;
			Assert.assertTrue(tse.getDateModified() != null && tse.getDateCreated() != null
					&& tse.getDateModified().getTime() >= tse.getDateCreated().getTime(),
					"Updated time stamp entity does not an updated modify date");
		}

		// purge (delete)
		startNewTransaction();
		dao.purge(e);
		setComplete();
		endTransaction();
		
		// verify purge
		startNewTransaction();
		try {
			e = getEntityFromDb(new PrimaryKey<IEntity>(e));
			Assert.assertNull(e, "The entity was not purged");
		}
		catch(EntityNotFoundException ex) {
			// expected
		}
		endTransaction();
	}

	/**
	 * tests the load all method
	 * @throws Exception
	 */
	private void daoLoadAll() throws Exception {
		final List<IEntity> list = dao.loadAll(entityHandler.entityClass());
		endTransaction();
		Assert.assertNotNull(list, "loadAll returned null");
	}

	/**
	 * Tests the findEntities() method
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void daoFindEntities() throws Exception {
		IEntity e = getTestEntity();
		e = dao.persist(e);
		setComplete();
		endTransaction();

		startNewTransaction();
		final Criteria<IEntity> criteria = new Criteria(e.entityClass());
		criteria.getPrimaryGroup().addCriterion(new PrimaryKey<IEntity>(e));
		final List<IEntity> list = dao.findEntities(criteria, null);
		endTransaction();
		Assert.assertNotNull(list, "findEntities returned null");
		Assert.assertTrue(list.size() == 1, "findEntities returned empty list");
	}

	/**
	 * Tests the find by ids method
	 * @throws Exception
	 */
	private void daoFindByIds() throws Exception {
		IEntity e = getTestEntity();
		e = dao.persist(e);
		setComplete();
		endTransaction();

		startNewTransaction();
		Assert.assertNotNull(e, "Null generated test entity");
		final List<Integer> ids = new ArrayList<Integer>(1);
		ids.add(e.getId());
		final List<IEntity> list = dao.findByIds(entityHandler.entityClass(), ids, null);
		endTransaction();
		Assert.assertTrue(list != null && list.size() == 1, "find by ids returned null list");
	}

	/**
	 * Tests id-based list handler related methods
	 * @throws Exception
	 */
	private void daoGetIdsAndEntities() throws Exception {
		if(!entityHandler.supportsPaging()) {
			logger.info("Not testing Id List Handler support for entity type: " + entityHandler.entityClass());
			return;
		}

		final List<IEntity> entityList = getNUniqueTestEntities(entityHandler.entityClass(), 5);
		final List<Integer> idList = new ArrayList<Integer>(5);
		for(IEntity e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
		}
		setComplete();
		endTransaction();
		Criteria<IEntity> criteria = new Criteria<IEntity>(entityHandler.entityClass());
		criteria.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

		// get ids
		startNewTransaction();
		final List<Integer> dbIdList = dao.getIds(criteria, simpleIdSorting);
		Assert.assertTrue(entitiesAndIdsEquals(dbIdList, entityList), "getIds list is empty or has incorrect ids");
		endTransaction();

		// get entities
		startNewTransaction();
		final List<IEntity> dbEntityList = dao.getEntitiesFromIds(entityHandler.entityClass(), idList, null);
		Assert.assertNotNull(idList, "getEntities list is null");
		Assert.assertTrue(entitiesAndIdsEquals(idList, dbEntityList), "getEntities list is empty or has incorrect ids");
		endTransaction();
	}

	/**
	 * Tests page-based list handler related methods
	 * @throws Exception
	 */
	private void daoPage() throws Exception {
		if(!entityHandler.supportsPaging()) {
			logger.info("Not testing daoPage for: " + entityHandler.entityClass());
			return;
		}

		final List<IEntity> entityList = getNUniqueTestEntities(entityHandler.entityClass(), 5);
		final List<Integer> idList = new ArrayList<Integer>(5);
		for(IEntity e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
		}
		setComplete();
		endTransaction();

		final Criteria<IEntity> crit = new Criteria<IEntity>(entityHandler.entityClass());
		crit.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

		startNewTransaction();
		IPageResult<SearchResult<IEntity>> page = dao.getPage(crit, simpleIdSorting, 0, 2);
		Assert.assertTrue(page != null && page.getPageList() != null && page.getPageList().size() == 2,
				"Empty or invalid number of initial page elements");
		endTransaction();

		startNewTransaction();
		page = dao.getPage(crit, simpleIdSorting, 2, 2);
		Assert.assertTrue(page != null && page.getPageList() != null && page.getPageList().size() == 2,
				"Empty or invalid number of subsequent page elements");
		endTransaction();

		startNewTransaction();
		page = dao.getPage(crit, simpleIdSorting, 4, 2);
		Assert.assertTrue(page != null && page.getPageList() != null && page.getPageList().size() == 1,
				"Empty or invalid number of last page elements");
		endTransaction();
	}

	/**
	 * Tests for the proper throwing of {@link EntityExistsException} in the dao.
	 * @throws Exception
	 */
	private void daoDuplicationException() throws Exception {
		// IMPT: don't test run this dao test if there are no business keys defined
		// for the current entity type!!
		if(!BusinessKeyFactory.hasBusinessKeys(entityHandler.entityClass())) {
			return;
		}

		IEntity e = getTestEntity();
		e = dao.persist(e);
		setComplete();
		endTransaction();

		startNewTransaction();
		IEntity e2 = getTestEntity();
		ensureNonUnique(e, e2);
		try {
			e2 = dao.persist(e2);
			setComplete();
			endTransaction();
			Assert.fail("A duplicate exception should have occurred for entity type: " + entityHandler.entityClass());
		}
		catch(final EntityExistsException de) {
			// expected
		}
	}

	private void daoPurgeNewEntity() throws Exception {
		IEntity e = getTestEntity();
		PrimaryKey<IEntity> pk = new PrimaryKey<IEntity>(e);
		dao.purge(e);
		try {
			e = dao.load(pk);
			Assert.fail("An EntityNotFoundException should have occurred (" + pk + ")");
		}
		catch(EntityNotFoundException ex) {
			// expected
		}
	}
	
	private void daoTestSelectNamedQueries() throws Exception {
		ISelectNamedQueryDef[] queryDefs = entityHandler.getQueriesToTest();
		if(queryDefs == null) return;
		for(ISelectNamedQueryDef qdef : queryDefs) {
			IQueryParam[] params = entityHandler.getParamsForTestQuery(qdef);
			List<IQueryParam> list = params == null ? null : Arrays.asList(params);
			Criteria<IEntity> c = new Criteria<IEntity>(qdef, list);
			List<SearchResult<IEntity>> result = dao.find(c, entityHandler.getSortingForTestQuery(qdef));
			Assert.assertTrue(result != null && result.size() > 0, "No named query results");
			if(result != null) {
				for(SearchResult<IEntity> sr : result) {
					if(qdef.isScalar()) {
						Assert.assertTrue(sr.getScalar() != null, "No scalar in scalar search result");
					}
					else {
						Assert.assertTrue(sr.getEntity() != null, "No entity in entity search result");
					}
				}
			}
		}
	}
}
