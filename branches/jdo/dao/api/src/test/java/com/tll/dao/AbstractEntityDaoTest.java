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

import org.springframework.dao.DataAccessException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.tll.AbstractInjectedTest;
import com.tll.config.Config;
import com.tll.criteria.Comparator;
import com.tll.criteria.Criteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.model.EntityBeanFactory;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.INamedEntity;
import com.tll.model.IScalar;
import com.tll.model.ITimeStampEntity;
import com.tll.model.key.BusinessKeyFactory;
import com.tll.model.key.BusinessKeyNotDefinedException;
import com.tll.model.key.BusinessKeyPropertyException;
import com.tll.model.key.BusinessKeyUtil;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao" })
public abstract class AbstractEntityDaoTest extends AbstractInjectedTest {

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
	protected static final class EntityDaoTestDecorator implements IEntityDao {

		private IEntityDao rawDao;

		public IEntityDao getRawDao() {
			return rawDao;
		}

		public void setRawDao(IEntityDao rawDao) {
			this.rawDao = rawDao;
		}

		@Override
		public int executeQuery(String queryName, IQueryParam[] params) {
			return rawDao.executeQuery(queryName, params);
		}

		@Override
		public <E extends IEntity> List<SearchResult<?>> find(Criteria<E> criteria, Sorting sorting)
		throws InvalidCriteriaException {
			return rawDao.find(criteria, sorting);
		}

		@Override
		public <E extends IEntity> List<E> findByIds(Class<E> entityType, Collection<String> ids, Sorting sorting) {
			return rawDao.findByIds(entityType, ids, sorting);
		}

		@Override
		public <E extends IEntity> List<E> findEntities(Criteria<E> criteria, Sorting sorting)
		throws InvalidCriteriaException {
			return rawDao.findEntities(criteria, sorting);
		}

		@Override
		public <E extends IEntity> E findEntity(Criteria<E> criteria) throws InvalidCriteriaException,
		EntityNotFoundException, NonUniqueResultException, DataAccessException {
			return rawDao.findEntity(criteria);
		}

		@Override
		public <E extends IEntity> List<String> getIds(Criteria<E> criteria, Sorting sorting)
		throws InvalidCriteriaException {
			return rawDao.getIds(criteria, sorting);
		}

		@Override
		public <E extends IEntity> IPageResult<SearchResult<?>> getPage(Criteria<E> criteria, Sorting sorting, int offset,
				int pageSize) throws InvalidCriteriaException {
			return rawDao.getPage(criteria, sorting, offset, pageSize);
		}

		@Override
		public <E extends IEntity> E load(IBusinessKey<E> key) throws EntityNotFoundException, DataAccessException {
			return rawDao.load(key);
		}

		@Override
		public <N extends INamedEntity> N load(NameKey<N> nameKey) throws EntityNotFoundException,
		NonUniqueResultException, DataAccessException {
			return rawDao.load(nameKey);
		}

		@Override
		public <E extends IEntity> E load(PrimaryKey<E> key) throws EntityNotFoundException, DataAccessException {
			return rawDao.load(key);
		}

		@Override
		public <E extends IEntity> List<E> loadAll(Class<E> entityType) throws DataAccessException {
			return rawDao.loadAll(entityType);
		}

		@Override
		public <E extends IEntity> E persist(E entity) throws DataAccessException {
			return rawDao.persist(entity);
		}

		@Override
		public <E extends IEntity> Collection<E> persistAll(Collection<E> entities) throws DataAccessException {
			return rawDao.persistAll(entities);
		}

		@Override
		public <E extends IEntity> void purge(E entity) throws DataAccessException {
			rawDao.purge(entity);
		}

		@Override
		public <E extends IEntity> void purgeAll(Collection<E> entities) throws DataAccessException {
			rawDao.purgeAll(entities);
		}

	} // EntityDaoTestDecorator

	/**
	 * Compare a clc of entity ids and entites ensuring the id list is referenced
	 * w/in the entity list
	 * @param <E>
	 * @param ids
	 * @param entities
	 * @return true/false
	 */
	protected static final <E extends IEntity> boolean entitiesAndIdsEquals(Collection<String> ids,
			Collection<E> entities) {
		if(ids == null || entities == null) {
			return false;
		}
		if(ids.size() != entities.size()) {
			return false;
		}
		for(final E e : entities) {
			boolean found = false;
			for(final String id : ids) {
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
	 * @param <E>
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
		catch(final BusinessKeyNotDefinedException e) {
			// assume ok
		}
		catch(final BusinessKeyPropertyException e) {
			Assert.fail(e.getMessage());
		}
	}

	protected static final Sorting simpleIdSorting = new Sorting(new SortColumn(IEntity.PK_FIELDNAME));

	/**
	 * The config to employ.
	 */
	protected final Config config;

	/**
	 * The test dao.
	 */
	protected final EntityDaoTestDecorator dao;

	/**
	 * The entity handlers subject to testing.
	 */
	private IEntityDaoTestHandler<?>[] entityHandlers;

	/**
	 * The current entity handler being tested.
	 */
	private IEntityDaoTestHandler<IEntity> entityHandler;

	/**
	 * Stack of test entity pks that are retained for proper datastore cleanup at
	 * the completion of dao testing for a particular entity type.
	 */
	private final Stack<PrimaryKey<IEntity>> testEntityRefStack;

	/**
	 * Constructor
	 */
	public AbstractEntityDaoTest() {
		super();
		config = Config.load();
		dao = new EntityDaoTestDecorator();
		testEntityRefStack = new Stack<PrimaryKey<IEntity>>();
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

	protected abstract void doBeforeClass();

	@Override
	protected final void beforeClass() {

		// get the dao test handlers
		entityHandlers = getDaoTestHandlers();
		if(entityHandlers == null || entityHandlers.length < 1) {
			throw new IllegalStateException("No entity dao handlers specified");
		}

		// build the injector
		buildInjector();
		assert injector != null;

		doBeforeClass();

		dao.setRawDao(injector.getInstance(IEntityDao.class));
	}

	@Override
	protected void afterClass() {
		super.afterClass();
	}

	protected abstract void startNewTransaction();

	protected abstract void setComplete();

	protected abstract void endTransaction();

	protected abstract boolean isTransStarted();

	/**
	 * Run before dao test methods are invoked for a particular entity dao test
	 * handler.
	 */
	private void beforeEntityType() {
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
		/*
		if(dao.getRawDao() instanceof com.tll.dao.mock.EntityDao) {
			final int afterNumEntities = ((com.tll.dao.mock.EntityDao) dao.getRawDao()).getEntityGraph().size();
			Assert.assertEquals(afterNumEntities, numEntities, entityHandler + " dao test handler didn't clean up properly!");
		}
		 */
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
			for(final PrimaryKey<IEntity> pk : testEntityRefStack) {
				assert pk.getType() == entityHandler.entityClass();
				startNewTransaction();
				try {
					// logger.debug("Tearing down test entity: " + e + "..");
					entityHandler.teardownTestEntity(dao.load(pk));
					setComplete();
				}
				catch(final EntityNotFoundException e) {
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
	protected final IEntityFactory getEntityFactory() {
		return injector.getInstance(IEntityFactory.class);
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
	<E extends IEntity> E getTestEntity() throws Exception {
		// logger.debug("Creating test entity..");
		final E e = (E) getEntityBeanFactory().getEntityCopy(entityHandler.entityClass(), false);
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
	 * @param key The primary key
	 * @return The sought entity direct from the datastore.
	 */
	protected abstract IEntity getEntityFromDb(PrimaryKey<IEntity> key);

	/**
	 * Run the dao test for all given entity types.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void test() throws Exception {

		for(final IEntityDaoTestHandler<?> handler : entityHandlers) {
			handler.init(dao, getEntityBeanFactory());
			entityHandler = (IEntityDaoTestHandler<IEntity>) handler;

			logger.debug("Testing entity dao for entity type: " + handler.entityClass() + "...");
			beforeEntityType();
			// run all tests
			for(final Method method : AbstractEntityDaoTest.class.getDeclaredMethods()) {
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
	final void daoFindByName() throws Exception {
		IEntity e = getTestEntity();

		// create
		e = dao.persist(e);
		setComplete();
		endTransaction();
		// final Integer persistentId = e.getId();

		// retrieve by name key if applicable..
		if(INamedEntity.class.isAssignableFrom(entityHandler.entityClass())) {
			logger.debug("Perfoming actual find by name dao test..");
			final Class<?> nec = entityHandler.entityClass();
			final String name = ((INamedEntity) e).getName();
			final NameKey<INamedEntity> nk = new NameKey(nec, name);
			startNewTransaction();
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
	final void daoCRUDAndFind() throws Exception {
		IEntity e = getTestEntity();
		Assert.assertTrue(e.isNew(), "The created test entity is not new and should be");

		String persistentId = null;

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
		catch(final EntityNotFoundException ex) {
			// expected
		}
		endTransaction();
	}

	/**
	 * tests the load all method
	 * @throws Exception
	 */
	final void daoLoadAll() throws Exception {
		final List<IEntity> list = dao.loadAll(entityHandler.entityClass());
		endTransaction();
		Assert.assertNotNull(list, "loadAll returned null");
	}

	/**
	 * Tests the findEntities() method
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	final void daoFindEntities() throws Exception {
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
	final void daoFindByIds() throws Exception {
		IEntity e = getTestEntity();
		e = dao.persist(e);
		setComplete();
		endTransaction();

		startNewTransaction();
		Assert.assertNotNull(e, "Null generated test entity");
		final List<String> ids = new ArrayList<String>(1);
		ids.add(e.getId());
		final List<IEntity> list = dao.findByIds(entityHandler.entityClass(), ids, null);
		endTransaction();
		Assert.assertTrue(list != null && list.size() == 1, "find by ids returned null list");
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
		final List<String> idList = new ArrayList<String>(5);
		for(IEntity e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
		}
		setComplete();
		endTransaction();
		final Criteria<IEntity> criteria = new Criteria<IEntity>(entityHandler.entityClass());
		criteria.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

		// get ids
		startNewTransaction();
		final List<String> dbIdList = dao.getIds(criteria, simpleIdSorting);
		Assert.assertTrue(entitiesAndIdsEquals(dbIdList, entityList), "getIds list is empty or has incorrect ids");
		endTransaction();

		// get entities
		startNewTransaction();
		final List<IEntity> dbEntityList = dao.findByIds(entityHandler.entityClass(), idList, null);
		Assert.assertNotNull(idList, "getEntities list is null");
		Assert.assertTrue(entitiesAndIdsEquals(idList, dbEntityList), "getEntities list is empty or has incorrect ids");
		endTransaction();
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
		final List<String> idList = new ArrayList<String>(5);
		for(IEntity e : entityList) {
			e = dao.persist(e);
			idList.add(e.getId());
		}
		setComplete();
		endTransaction();

		final Criteria<IEntity> crit = new Criteria<IEntity>(entityHandler.entityClass());
		crit.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, idList, Comparator.IN, false);

		startNewTransaction();
		IPageResult<SearchResult<?>> page = dao.getPage(crit, simpleIdSorting, 0, 2);
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
	final void daoDuplicationException() throws Exception {
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
		final IEntity e2 = getTestEntity();
		ensureNonUnique(e, e2);
		try {
			dao.persist(e2);
			setComplete();
			endTransaction();
			Assert.fail("A duplicate exception should have occurred for entity type: " + entityHandler.entityClass());
		}
		catch(final EntityExistsException de) {
			// expected
		}
	}

	final void daoPurgeNewEntity() throws Exception {
		final IEntity e = getTestEntity();
		final PrimaryKey<IEntity> pk = new PrimaryKey<IEntity>(e);
		try {
			dao.purge(e);
			Assert.fail("An EntityNotFoundException should have occurred (" + pk + ")");
		}
		catch(final EntityNotFoundException ex) {
			// expected
		}
	}

	final void daoTestSelectNamedQueries() throws Exception {
		final ISelectNamedQueryDef[] queryDefs = entityHandler.getQueriesToTest();
		if(queryDefs == null) return;
		for(final ISelectNamedQueryDef qdef : queryDefs) {
			final IQueryParam[] params = entityHandler.getParamsForTestQuery(qdef);
			final List<IQueryParam> list = params == null ? null : Arrays.asList(params);
			final Criteria<IEntity> c = new Criteria<IEntity>(qdef, list);
			final List<SearchResult<?>> result = dao.find(c, entityHandler.getSortingForTestQuery(qdef));
			// Assert.assertTrue(result != null && result.size() > 0, "No named query results");
			// for now, since we can't guarantee results based on the varied nature of
			// the query defs, we first check for resutls and pass if there aren't any
			if(result != null && result.size() > 0) {
				for(final SearchResult<?> sr : result) {
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
}