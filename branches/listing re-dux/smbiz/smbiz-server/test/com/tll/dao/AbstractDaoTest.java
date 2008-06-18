/*
 * Created on - May 19, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 * 
 */
package com.tll.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.criteria.Comparator;
import com.tll.criteria.CriteriaFactory;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.guice.DaoModule;
import com.tll.guice.JpaModule;
import com.tll.listhandler.IPageResult;
import com.tll.listhandler.SearchResult;
import com.tll.listhandler.Sorting;
import com.tll.model.IEntity;
import com.tll.model.ITimeStampEntity;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;
import com.tll.util.EnumUtil;

/**
 * AbstractDaoTest
 * @author jpk
 */
public abstract class AbstractDaoTest<E extends IEntity> extends DbTest {

	protected final Class<E> entityClass;

	protected final Class<? extends IEntityDao<? super E>> daoClass;

	protected DaoMode daoMode;

	protected IEntityDao<E> rawDao;

	protected final EntityDao dao = new EntityDao();

	protected List<E> dbRemove = new ArrayList<E>();

	/**
	 * Flag used to test {@link IIdListSupport} and {@link IPageSupport} related
	 * test methods.
	 */
	private final boolean testPagingRelated;

	/**
	 * Constructor
	 * @param entityClass
	 * @param daoClass
	 */
	protected AbstractDaoTest(Class<E> entityClass, Class<? extends IEntityDao<? super E>> daoClass) {
		this(entityClass, daoClass, true);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param daoClass
	 * @param testPagingRelated
	 */
	protected AbstractDaoTest(Class<E> entityClass, Class<? extends IEntityDao<? super E>> daoClass,
			boolean testPagingRelated) {
		super();
		this.entityClass = entityClass;
		this.daoClass = daoClass;
		this.testPagingRelated = testPagingRelated;
	}

	@Override
	protected final void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new JpaModule(jpaMode));
		modules.add(new DaoModule(daoMode));
	}

	@BeforeClass(alwaysRun = true)
	@Parameters(value = "daoMode")
	public final void onBeforeClass(String daoModeStr) {
		this.daoMode = EnumUtil.fromString(DaoMode.class, daoModeStr);
		beforeClass();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void beforeClass() {

		// for dao impl tests, the jpa mode is soley dependent on the dao
		// mode
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
		assert jpaMode != null : "The JPA mode is un-resolvable";

		// build the injector
		buildInjector();

		this.rawDao = (IEntityDao<E>) getDao(daoClass);
		logger.debug("Starting DAO Test: " + this.getClass().getSimpleName() + ", dao mode: " + daoMode.toString());

		if(daoMode == DaoMode.ORM) {
			// ensure test db is created and cleared
			getDbShell().create();
			getDbShell().clear();
		}
	}

	@BeforeMethod(alwaysRun = true)
	@Override
	protected final void beforeMethod() {
		super.beforeMethod();
		beforeMethodHook();
		startNewTransaction();
	}

	@AfterMethod(alwaysRun = true)
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
	 * @param i
	 */
	protected void uniquify(E e, int i) {
		makeUnique(e, i);
	}

	/**
	 * Provide a fresh entity instance of the ascribed type.
	 * @param entityClass
	 * @return New entity instance
	 * @throws Exception
	 */
	protected final E getTestEntity() throws Exception {
		final E e = getMockEntityProvider().getEntityCopy(entityClass);
		assembleTestEntity(e);
		uniquify(e, 1);
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
			uniquify(e, i);
			list.add(e);
		}
		return list;
	}

	protected final <D extends IEntityDao<? extends IEntity>> D getDao(Class<D> type) {
		return injector.getInstance(type);
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
	 * Decorates {@link IEntityDao} to keep the dao tests from degrading as the
	 * code base naturally changes over time
	 * @author jpk
	 */
	protected final class EntityDao implements IEntityDao<E> {

		public Class<E> getEntityClass() {
			return rawDao.getEntityClass();
		}

		public E persist(E entity) {
			return rawDao.persist(entity);
		}

		public E load(IPrimaryKey<? extends E> key) {
			return rawDao.load(key);
		}

		public E load(IBusinessKey<? extends E> key) {
			return rawDao.load(key);
		}

		public void purge(E entity) {
			rawDao.purge(entity);
		}

		public List<E> loadAll() {
			return rawDao.loadAll();
		}

		public void purgeAll(Collection<E> entities) {
			rawDao.purgeAll(entities);
		}

		public Collection<E> persistAll(Collection<E> entities) {
			return rawDao.persistAll(entities);
		}

		public List<E> findEntities(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException {
			return rawDao.findEntities(criteria, sorting);
		}

		public E findEntity(ICriteria<? extends E> criteria) throws InvalidCriteriaException {
			return rawDao.findEntity(criteria);
		}

		public List<E> findByIds(List<Integer> ids, Sorting sorting) {
			return rawDao.findByIds(ids, sorting);
		}

		public List<Integer> getIds(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException {
			return rawDao.getIds(criteria, sorting);
		}

		public List<E> getEntitiesFromIds(Class<? extends E> entityClass, Collection<Integer> ids, Sorting sorting) {
			return rawDao.getEntitiesFromIds(entityClass, ids, sorting);
		}

		public List<SearchResult<E>> find(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException {
			return rawDao.find(criteria, sorting);
		}

		public IPageResult<SearchResult<E>> getPage(ICriteria<? extends E> criteria, Sorting sorting, int offset,
				int pageSize) throws InvalidCriteriaException {
			return rawDao.getPage(criteria, sorting, offset, pageSize);
		}

		public void clear() {
			rawDao.clear();
		}

		public void flush() {
			rawDao.flush();
		}

	}

	/**
	 * Alters the entity as prep for update via the dao.
	 * @param e the entity
	 */
	protected abstract void alterEntity(E e);

	/**
	 * Verifies the alteration(s) made to the entity after dao update
	 * @param e the entity
	 * @throws Exception if alteration(s) don't remain
	 */
	protected abstract void verifyEntityAlteration(E e) throws Exception;

	/**
	 * Verifies the state of the entity is ok based Entity property and
	 * collections should be verified here.
	 * @param e the entity
	 * @throws Exception if entity can't be retrieved
	 */
	protected abstract void verifyLoadedEntityState(E e) throws Exception;

	protected final E getEntityFromDb(IPrimaryKey<E> key) {
		return DbTest.getEntityFromDb(dao, key);
	}

	// /////////////// TESTS

	/**
	 * Test CRUD and find ops
	 * @throws Exception
	 */
	@Test(groups = "dao")
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
		e = dao.load(KeyFactory.getPrimaryKey(entityClass, persistentId));
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
		e = getEntityFromDb(KeyFactory.getPrimaryKey(e));
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
		e = getEntityFromDb(KeyFactory.getPrimaryKey(e));
		endTransaction();
		Assert.assertNull(e, "The entity was not purged");
	}

	/**
	 * tests the load all method
	 * @throws Exception
	 */
	@Test(groups = "dao")
	public final void testLoadAll() throws Exception {
		final List<E> list = dao.loadAll();
		endTransaction();
		Assert.assertNotNull(list, "loadAll returned null");
	}

	/**
	 * Tests the findEntities() method
	 * @throws Exception
	 */
	@Test(groups = "dao")
	public final void testFindEntities() throws Exception {
		E e = getTestEntity();
		e = dao.persist(e);
		setComplete();
		endTransaction();
		dbRemove.add(e);

		startNewTransaction();
		final ICriteria<? extends E> criteria = CriteriaFactory.buildEntityCriteria(KeyFactory.getPrimaryKey(e));
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
		final List<E> list = dao.findByIds(ids, null);
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
		final ICriteria<? extends E> crit =
				CriteriaFactory.buildEntityCriteria(entityClass, IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

		// get ids
		startNewTransaction();
		final List<Integer> dbIdList = dao.getIds(crit, simpleIdSorting);
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

		final ICriteria<? extends E> crit =
				CriteriaFactory.buildEntityCriteria(entityClass, IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

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
		if(e2 instanceof ITimeStampEntity) {
			((ITimeStampEntity) e2).setDateCreated(((ITimeStampEntity) e2).getDateCreated());
			((ITimeStampEntity) e2).setDateModified(((ITimeStampEntity) e2).getDateModified());
		}
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
