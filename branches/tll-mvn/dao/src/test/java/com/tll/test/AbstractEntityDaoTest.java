/*
 * Created on - May 19, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 * 
 */
package com.tll.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.tll.config.Config;
import com.tll.criteria.Comparator;
import com.tll.criteria.Criteria;
import com.tll.criteria.ICriteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.DaoMode;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.JpaMode;
import com.tll.dao.SearchResult;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.DaoModule;
import com.tll.di.DbDialectModule;
import com.tll.di.DbShellModule;
import com.tll.di.JpaModule;
import com.tll.di.MockEntitiesModule;
import com.tll.di.ModelModule;
import com.tll.model.BusinessKeyFactory;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityProvider;
import com.tll.model.INamedEntity;
import com.tll.model.ITimeStampEntity;
import com.tll.model.MockEntityFactory;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.util.EnumUtil;

/**
 * AbstractEntityDaoTest
 * @param <E> entity type
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public abstract class AbstractEntityDaoTest<E extends IEntity> extends DbTest {

	/**
	 * Decorates {@link IEntityDao} to keep the dao tests from degrading as the
	 * code base naturally changes over time
	 * @author jpk
	 */
	private final class EntityDao implements IEntityDao {

		@Override
		public <R extends IEntity> R persist(R entity) {
			return rawDao.persist(entity);
		}

		@Override
		public <R extends IEntity> R load(PrimaryKey<R> key) {
			return rawDao.load(key);
		}

		@Override
		public <R extends IEntity> R load(BusinessKey<R> key) {
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

	protected static final Sorting simpleIdSorting = new Sorting(new SortColumn(IEntity.PK_FIELDNAME));

	protected final Class<E> entityClass;

	private DaoMode daoMode;

	private IEntityDao rawDao;
	
	/**
	 * Employed only when {@link #daoMode} is {@link DaoMode#MOCK}.
	 */
	private final IEntityProvider mockEntityProvider;

	private final EntityDao dao = new EntityDao();

	private final List<E> dbRemove = new ArrayList<E>();

	/**
	 * Flag used to test {@link IIdListSupport} and {@link IPageSupport} related
	 * test methods.
	 */
	private final boolean testPagingRelated;

	/**
	 * Constructor
	 * @param entityClass
	 * @param testPagingRelated
	 * @param mockEntityProvider Used only when the dao mode is
	 *        {@link DaoMode#MOCK}.
	 */
	protected AbstractEntityDaoTest(Class<E> entityClass, boolean testPagingRelated, IEntityProvider mockEntityProvider) {
		super();
		this.entityClass = entityClass;
		this.testPagingRelated = testPagingRelated;
		this.mockEntityProvider = mockEntityProvider;
	}

	@Override
	protected final void addModules(List<Module> modules) {
		modules.add(new ModelModule());
		modules.add(new MockEntitiesModule());
		super.addModules(modules);
		if(daoMode == DaoMode.ORM) {
			modules.add(new DbDialectModule());
		}
		else if(daoMode == DaoMode.MOCK) {
			if(mockEntityProvider == null) {
				throw new IllegalStateException("A mock entity provider must be specified when performing mock dao testing.");
			}
			modules.add(new MockEntitiesModule());
			modules.add(new Module() {

				@Override
				public void configure(Binder binder) {
					binder.bind(IEntityProvider.class).toInstance(mockEntityProvider);
				}
			});
		}
		modules.add(new JpaModule(getJpaMode()));
		modules.add(new DaoModule(daoMode));
	}

	@BeforeClass(alwaysRun = true)
	@Parameters(value = "daoMode")
	public final void onBeforeClass(String daoModeStr) {
		this.daoMode = EnumUtil.fromString(DaoMode.class, daoModeStr);
		beforeClass();
	}

	@Override
	protected void beforeClass() {

		JpaMode jpaMode = null;

		// for dao impl tests, the jpa mode is soley dependent on the dao mode
		switch(daoMode) {
			case ORM:
				jpaMode = JpaMode.LOCAL;
				break;
			case MOCK:
				jpaMode = JpaMode.NONE;
				break;
			default:
				throw new IllegalStateException("Unhandled dao mode: " + daoMode.name());
		}

		setJpaMode(jpaMode);

		if(daoMode == DaoMode.ORM) {
			// create a db shell to ensure db exists and stubbed
			String dbName = Config.instance().getString(DbShellModule.ConfigKeys.DB_NAME.getKey());
			assert dbName != null : "No db name specified in config";
			Injector i = Guice.createInjector(Stage.DEVELOPMENT, new DbDialectModule(), new DbShellModule(dbName));
			DbShell dbShell = i.getInstance(DbShell.class);
			dbShell.create();
			dbShell.clear();
		}

		// build the injector
		buildInjector();

		this.rawDao = injector.getInstance(IEntityDao.class);
		logger.debug("Starting DAO Test: " + this.getClass().getSimpleName() + ", dao mode: " + daoMode.toString());
	}

	@AfterClass(alwaysRun = true)
	public final void onAfterClass() {
		afterClass();
	}

	@BeforeMethod(alwaysRun = true)
	public final void onBeforeMethod() {
		beforeMethod();
	}

	@Override
	protected final void beforeMethod() {
		super.beforeMethod();
		beforeMethodHook();
		startNewTransaction();
	}

	@AfterMethod(alwaysRun = true)
	public final void onAfterMethod() {
		afterMethod();
	}

	@Override
	protected final void afterMethod() {
		super.afterMethod();
		if(isTransStarted()) {
			endTransaction();
		}
		if(dbRemove.size() > 0) {
			startNewTransaction();
			setComplete();
			for(final E e : dbRemove) {
				dao.purge(e);
				teardownTestEntity(e);
			}
			dbRemove.clear();
			endTransaction();
		}
		afterMethodHook();
	}

	/**
	 * @return The injected {@link IEntityDao}
	 */
	protected final IEntityDao getEntityDao() {
		return dao;
	}

	/**
	 * @return The injected {@link IEntityFactory}
	 */
	protected final IEntityFactory getEntityFactory() {
		return injector.getInstance(IEntityFactory.class);
	}

	/**
	 * Concrete sub-classes should override this method to properly wire up the
	 * entity.
	 * <p>
	 * NOTE: this method is called within a transaction.
	 * @param e the generated test entity gotten from the app context.
	 */
	protected abstract void assembleTestEntity(E e) throws Exception;

	/**
	 * Concrete sub-classes should override this method to clean up entity
	 * dependencies in the db.
	 * <p>
	 * NOTE: this method is called within a transaction.
	 * @param e the assembled test entity gotten from
	 *        {@link #assembleTestEntity(IEntity)}
	 */
	protected void teardownTestEntity(E e) {
		// default impl is nothing
	}

	/**
	 * Hook for concrete classes to do set up work before test methods.
	 * <p>
	 * No transaction started.
	 */
	protected void beforeMethodHook() {
		// default impl - do nothing
	}

	/**
	 * Hook for concrete classes to clean up after test methods.
	 * <p>
	 * No transaction started.
	 */
	protected void afterMethodHook() {
		// default impl - do nothing
	}

	/**
	 * Uniquify the entity - may be overridden by sub-classes.
	 * @param e
	 */
	protected void uniquify(E e) {
		makeUnique(e);
	}

	/**
	 * Makes the given entity unique based on the defined {@link BusinessKey}s for
	 * type of the given entity.
	 * @param e The entity to uniquify
	 */
	protected final <ET extends IEntity> void makeUnique(ET e) {
		try {
			getMockEntityFactory().makeBusinessKeyUnique(e);
		}
		catch(final BusinessKeyNotDefinedException e1) {
			// ok
		}
	}

	/**
	 * <strong>NOTE: </strong>The {@link MockEntityFactory} is not available by
	 * default. It must be bound in a given module which is added via
	 * {@link #addModules(List)}.
	 * @return The injected {@link MockEntityFactory}
	 */
	protected final MockEntityFactory getMockEntityFactory() {
		return injector.getInstance(MockEntityFactory.class);
	}

	/**
	 * Provide a fresh entity instance of the ascribed type.
	 * @param entityClass
	 * @return New entity instance
	 * @throws Exception
	 */
	protected final E getTestEntity() throws Exception {
		final E e = getMockEntityFactory().getEntityCopy(entityClass, false);
		assembleTestEntity(e);
		uniquify(e);
		return e;
	}

	/**
	 * Used for testing dao methods involving entity collections.
	 * @param entityClass
	 * @param n
	 * @return a list of unique and generated test entities
	 */
	protected final List<E> getNUniqueTestEntities(Class<E> entityClass, int n) throws Exception {
		Assert.assertTrue(n > 0 && n <= 50, "Invalid number for unique entity generation: " + n
				+ ".  Specify a number between 1 and 50.");
		final List<E> list = new ArrayList<E>(n);
		for(int i = 1; i <= n; i++) {
			final E e = getTestEntity();
			uniquify(e);
			list.add(e);
		}
		return list;
	}

	/**
	 * Disables caching for the current data store session.
	 */
	protected final void disableDataStoreCaching() {
		if(this.daoMode == DaoMode.ORM) {
			((Session) getEntityManager().getDelegate()).setCacheMode(CacheMode.IGNORE);
		}
	}

	/**
	 * Ensures two entities are non-unique by business key.
	 * @param e1
	 * @param e2
	 */
	protected final void ensureNonUnique(E e1, E e2) {
		if(e2 instanceof ITimeStampEntity) {
			((ITimeStampEntity) e2).setDateCreated(((ITimeStampEntity) e2).getDateCreated());
			((ITimeStampEntity) e2).setDateModified(((ITimeStampEntity) e2).getDateModified());
		}
		try {
			BusinessKeyFactory.apply(e2, BusinessKeyFactory.create(e1));
		}
		catch(BusinessKeyNotDefinedException e) {
			// assume ok
		}
	}

	/**
	 * Alters the entity as prep for update via the dao.
	 * @param e the entity
	 */
	protected void alterEntity(E e) {
		if(e instanceof INamedEntity) {
			((INamedEntity) e).setName("altered");
		}
	}

	/**
	 * Verifies the alteration(s) made to the entity after dao update
	 * @param e the entity
	 * @throws Exception if alteration(s) don't remain
	 */
	protected void verifyEntityAlteration(E e) throws Exception {
		if(e instanceof INamedEntity) {
			Assert.assertTrue("altered".equals(((INamedEntity) e).getName()), "Named entity alteration does not match");
		}
	}

	/**
	 * Verifies the state of the entity is ok based Entity property and
	 * collections should be verified here.
	 * @param e the entity
	 * @throws Exception if entity can't be retrieved
	 */
	protected void verifyLoadedEntityState(E e) throws Exception {
		if(e instanceof INamedEntity) {
			Assert.assertNotNull(((INamedEntity) e).getName(), "The name property is null");
		}
	}

	protected final E getEntityFromDb(PrimaryKey<E> key) {
		return DbTest.getEntityFromDb(dao, key);
	}

	// /////////////// TESTS

	/**
	 * Test CRUD and find ops
	 * @throws Exception
	 */
	@Test(groups = {
		"dao", "crud" })
	public final void testCRUDAndFind() throws Exception {

		E e = getTestEntity();
		Integer persistentId = null;

		// create
		e = dao.persist(e);
		setComplete();
		endTransaction();
		dbRemove.add(e);
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
		e = dao.load(new PrimaryKey<E>(entityClass, persistentId));
		Assert.assertNotNull(e, "The loaded entity is null");
		verifyLoadedEntityState(e);
		endTransaction();

		// update
		startNewTransaction();
		alterEntity(e);
		e = dao.persist(e);
		setComplete();
		endTransaction();

		// find (update verify)
		startNewTransaction();
		e = getEntityFromDb(new PrimaryKey<E>(e));
		Assert.assertNotNull(e, "The retrieved entity for update check is null");
		endTransaction();
		verifyEntityAlteration(e);

		if(e instanceof ITimeStampEntity) {
			// verify modify date
			final ITimeStampEntity tse = (ITimeStampEntity) e;
			Assert.assertTrue(tse.getDateModified() != null && tse.getDateCreated() != null
					&& tse.getDateModified().getTime() >= tse.getDateCreated().getTime(),
					"Updated time stamp entity does not an updated modify date");
		}

		// delete
		startNewTransaction();
		dao.purge(e);
		setComplete();
		endTransaction();
		dbRemove.clear();
		startNewTransaction();
		e = getEntityFromDb(new PrimaryKey<E>(e));
		endTransaction();
		Assert.assertNull(e, "The entity was not purged");
	}

	/**
	 * tests the load all method
	 * @throws Exception
	 */
	@Test(groups = "dao")
	public final void testLoadAll() throws Exception {
		final List<E> list = dao.loadAll(entityClass);
		endTransaction();
		Assert.assertNotNull(list, "loadAll returned null");
	}

	/**
	 * Tests the findEntities() method
	 * @throws Exception
	 */
	@Test(groups = "dao")
	@SuppressWarnings("unchecked")
	public final void testFindEntities() throws Exception {
		E e = getTestEntity();
		e = dao.persist(e);
		setComplete();
		endTransaction();
		dbRemove.add(e);

		startNewTransaction();
		final Criteria<E> criteria = new Criteria(e.entityClass());
		criteria.getPrimaryGroup().addCriterion(new PrimaryKey<E>(e));
		final List<E> list = dao.findEntities(criteria, null);
		endTransaction();
		Assert.assertNotNull(list, "findEntities returned null");
		Assert.assertTrue(list.size() == 1, "findEntities returned empty list");
	}

	/**
	 * Tests the find by ids method
	 * @throws Exception
	 */
	@Test(groups = "dao")
	public final void testFindByIds() throws Exception {
		E e = getTestEntity();
		e = dao.persist(e);
		setComplete();
		endTransaction();
		dbRemove.add(e);

		startNewTransaction();
		Assert.assertNotNull(e, "Null generated test entity");
		final List<Integer> ids = new ArrayList<Integer>(1);
		ids.add(e.getId());
		final List<E> list = dao.findByIds(entityClass, ids, null);
		endTransaction();
		Assert.assertTrue(list != null && list.size() == 1, "find by ids returned null list");
	}

	/**
	 * Tests id-based list handler related methods
	 * @throws Exception
	 */
	@Test(groups = "dao")
	public final void testGetIdsAndEntities() throws Exception {
		if(!testPagingRelated) {
			logger.info("Not testing Id List Handler support test method.");
			return;
		}

		final List<E> entityList = getNUniqueTestEntities(entityClass, 5);
		final List<Integer> idList = new ArrayList<Integer>(5);
		for(E e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
			dbRemove.add(e);
		}
		setComplete();
		endTransaction();
		Criteria<? extends E> criteria = new Criteria<E>(entityClass);
		criteria.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

		// get ids
		startNewTransaction();
		final List<Integer> dbIdList = dao.getIds(criteria, simpleIdSorting);
		Assert.assertTrue(entitiesAndIdsEquals(dbIdList, entityList), "getIds list is empty or has incorrect ids");
		endTransaction();

		// get entities
		startNewTransaction();
		final List<E> dbEntityList = dao.getEntitiesFromIds(entityClass, idList, null);
		Assert.assertNotNull(idList, "getEntities list is null");
		Assert.assertTrue(entitiesAndIdsEquals(idList, dbEntityList), "getEntities list is empty or has incorrect ids");
		endTransaction();
	}

	/**
	 * Tests page-based list handler related methods
	 * @throws Exception
	 */
	@Test(groups = "dao")
	public final void testPage() throws Exception {
		if(!testPagingRelated) {
			logger.info("Not testing DetachedCriteriaPage List Handler support test method.");
			return;
		}

		final List<E> entityList = getNUniqueTestEntities(entityClass, 5);
		final List<Integer> idList = new ArrayList<Integer>(5);
		for(E e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
			dbRemove.add(e);
		}
		setComplete();
		endTransaction();

		final Criteria<E> crit = new Criteria<E>(entityClass);
		crit.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

		startNewTransaction();
		IPageResult<SearchResult<E>> page = dao.getPage(crit, simpleIdSorting, 0, 2);
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
	@Test(groups = "dao")
	public final void testDuplicationException() throws Exception {
		E e = getTestEntity();
		e = dao.persist(e);
		setComplete();
		endTransaction();
		dbRemove.add(e);

		startNewTransaction();
		E e2 = getTestEntity();
		ensureNonUnique(e, e2);
		try {
			e2 = dao.persist(e2);
			setComplete();
			endTransaction();
			dbRemove.add(e2);
			Assert.fail("A duplicate exception should have occurred.");
		}
		catch(final EntityExistsException de) {
			// expected
		}
	}

	@Test(groups = "dao")
	public final void testPurgeNewEntity() throws Exception {
		final E e = getTestEntity();
		dao.purge(e);
	}
}
