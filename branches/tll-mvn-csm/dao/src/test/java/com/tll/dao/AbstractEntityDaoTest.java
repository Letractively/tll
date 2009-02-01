/*
 * Created on - May 19, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 * 
 */
package com.tll.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityExistsException;

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
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.DaoModule;
import com.tll.di.DbDialectModule;
import com.tll.di.DbShellModule;
import com.tll.di.MockEntityFactoryModule;
import com.tll.di.ModelModule;
import com.tll.model.BusinessKeyFactory;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.INamedEntity;
import com.tll.model.ITimeStampEntity;
import com.tll.model.key.BusinessKey;
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
			BusinessKeyFactory.apply(e2, BusinessKeyFactory.create(e1));
		}
		catch(BusinessKeyNotDefinedException e) {
			// assume ok
		}
	}

	protected static final Sorting simpleIdSorting = new Sorting(new SortColumn(IEntity.PK_FIELDNAME));

	/**
	 * The entity handler subject to testing.
	 */
	private Collection<IEntityDaoTestHandler<IEntity>> entityHandlers;

	/**
	 * The current entity handler under testing.
	 */
	private IEntityDaoTestHandler<IEntity> entityHandler;

	private DaoMode daoMode;

	private IEntityDao rawDao;
	
	private final IEntityDao dao = new EntityDao();

	private final List<IEntity> dbRemove = new ArrayList<IEntity>();

	/**
	 * Flag used to test {@link IIdListSupport} and {@link IPageSupport} related
	 * test methods.
	 */
	private boolean testPagingRelated;

	/**
	 * Constructor
	 */
	public AbstractEntityDaoTest() {
		super();
	}
	
	/**
	 * @return The dao test handlers subject to testing.
	 */
	protected abstract Collection<IEntityDaoTestHandler<IEntity>> getDaoTestHandlers();

	/**
	 * @param testPagingRelated the testPagingRelated to set
	 */
	protected void setTestPagingRelated(boolean testPagingRelated) {
		this.testPagingRelated = testPagingRelated;
	}

	@Override
	protected final void addModules(List<Module> modules) {
		modules.add(new ModelModule());
		modules.add(new MockEntityFactoryModule());
		super.addModules(modules);
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
		
		// get the dao test handlers
		entityHandlers = getDaoTestHandlers();
		if(entityHandlers == null || entityHandlers.size() < 1) {
			throw new IllegalStateException("No entity dao handlers specified");
		}

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
			Injector i = Guice.createInjector(Stage.DEVELOPMENT, new DbDialectModule(), new DbShellModule());
			DbShell dbShell = i.getInstance(DbShell.class);
			dbShell.create();
			dbShell.clear();
		}

		// build the injector
		buildInjector();

		this.rawDao = injector.getInstance(IEntityDao.class);
	}

	@AfterClass(alwaysRun = true)
	public final void onAfterClass() {
		afterClass();
	}

	@Override
	protected final void beforeMethod() {
		super.beforeMethod();
		// ensure entity class is set
		startNewTransaction();
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
			for(final IEntity e : dbRemove) {
				dao.purge(e);
				entityHandler.teardownTestEntity(e);
			}
			dbRemove.clear();
			endTransaction();
		}
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
	 * @return New entity instance
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected final <E extends IEntity> E getTestEntity() throws Exception {
		final E e = (E) getMockEntityFactory().getEntityCopy(entityHandler.entityClass(), false);
		entityHandler.assembleTestEntity(e);
		entityHandler.makeUnique(e);
		return e;
	}

	/**
	 * Used for testing dao methods involving entity collections.
	 * @param entityClass
	 * @param n
	 * @return a list of unique and generated test entities
	 */
	protected final List<IEntity> getNUniqueTestEntities(Class<IEntity> entityClass, int n) throws Exception {
		Assert.assertTrue(n > 0 && n <= 50, "Invalid number for unique entity generation: " + n
				+ ".  Specify a number between 1 and 50.");
		final List<IEntity> list = new ArrayList<IEntity>(n);
		for(int i = 1; i <= n; i++) {
			final IEntity e = getTestEntity();
			entityHandler.makeUnique(e);
			list.add(e);
		}
		return list;
	}

	protected final IEntity getEntityFromDb(PrimaryKey<IEntity> key) {
		return DbTest.getEntityFromDb(dao, key);
	}

	/**
	 * Run the dao test for all given entity types.
	 * @throws Exception
	 */
	@Test
	public final void test() throws Exception {

		for(IEntityDaoTestHandler<IEntity> handler : entityHandlers) {
			handler.init(getEntityDao(), getEntityFactory(), getMockEntityFactory());
			this.entityHandler = handler;
			logger.debug("Testing entity dao for entity type: " + handler.entityClass() + "...");
			// run all tests
			for(Method method : AbstractEntityDaoTest.class.getDeclaredMethods()) {
				if(method.getName().startsWith("dao")) {
					beforeMethod();
					logger.debug("Testing " + method.getName() + " for entity type: " + handler.entityClass() + "...");
					method.invoke(this, (Object[]) null);
					afterMethod();
				}
			}
		}
	}

	// ***TESTS***

	/**
	 * Test CRUD and find ops
	 * @throws Exception
	 */
	private void daoCRUDAndFind() throws Exception {

		IEntity e = getTestEntity();
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

		// delete
		startNewTransaction();
		dao.purge(e);
		setComplete();
		endTransaction();
		dbRemove.clear();
		startNewTransaction();
		e = getEntityFromDb(new PrimaryKey<IEntity>(e));
		endTransaction();
		Assert.assertNull(e, "The entity was not purged");
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
		dbRemove.add(e);

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
		dbRemove.add(e);

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
		if(!testPagingRelated) {
			logger.info("Not testing Id List Handler support test method.");
			return;
		}

		final List<IEntity> entityList = getNUniqueTestEntities(entityHandler.entityClass(), 5);
		final List<Integer> idList = new ArrayList<Integer>(5);
		for(IEntity e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
			dbRemove.add(e);
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
		if(!testPagingRelated) {
			logger.info("Not testing DetachedCriteriaPage List Handler support test method.");
			return;
		}

		final List<IEntity> entityList = getNUniqueTestEntities(entityHandler.entityClass(), 5);
		final List<Integer> idList = new ArrayList<Integer>(5);
		for(IEntity e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
			dbRemove.add(e);
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
		IEntity e = getTestEntity();
		e = dao.persist(e);
		setComplete();
		endTransaction();
		dbRemove.add(e);

		startNewTransaction();
		IEntity e2 = getTestEntity();
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

	private void daoPurgeNewEntity() throws Exception {
		final IEntity e = getTestEntity();
		dao.purge(e);
	}
}
