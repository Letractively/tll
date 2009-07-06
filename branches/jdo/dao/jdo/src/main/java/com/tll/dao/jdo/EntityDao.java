package com.tll.dao.jdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.jdo.FetchGroup;
import javax.jdo.JDODataStoreException;
import javax.jdo.JDOException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.inject.Inject;
import com.tll.criteria.Comparator;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.CriterionGroup;
import com.tll.criteria.ICriterion;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.db.IDbDialectHandler;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.util.CollectionUtil;

/**
 * EntityDao - JDO dao implementation.
 * @author jpk
 */
public class EntityDao implements IEntityDao {

	/**
	 * Applies sorting objects to the hibernate criteria object. This method takes
	 * a <code>DetachedCriteria</code> implementation simply because there is no
	 * common interface for <code>DetachedCriteria</code> and
	 * <code>Criteria</code>.
	 * @param dc the hibernate criteria object
	 * @param sorting sorting object
	 * @throws InvalidCriteriaException
	 */
	/*
	private static void applySorting(DetachedCriteria dc, Sorting sorting) throws InvalidCriteriaException {
		if(sorting != null) {
			final SortColumn[] columns = sorting.getColumns();
			for(final SortColumn element : columns) {
				// final SortDir dir = element.getDirection() == null ? SortDir.ASC :
				// element.getDirection();
				final String column = element.getPropertyName();
				final Boolean ignoreCase = element.getIgnoreCase();
				applyAliasIfNecessary(dc, column);
				final Order order = (element.getDirection() == SortDir.ASC) ? Order.asc(column) : Order.desc(column);
				if(Boolean.TRUE.equals(ignoreCase)) {
					order.ignoreCase();
				}
				dc.addOrder(order);
			}
		}
	}
	 */

	/**
	 * The db dialect handler.
	 * <p>
	 * <strong>NOTE: </strong>To retain thread safety, do <pm>not</pm> publish
	 * this member.
	 */
	private final IDbDialectHandler dbDialectHandler;

	/**
	 * The comparator translator.
	 * <p>
	 * <strong>NOTE: </strong>To retain thread safety, do <pm>not</pm> publish
	 * this member.
	 */
	private final JdoQueryComparatorTranslator comparatorTranslator;
	private final PersistenceManagerFactory pmf;

	/**
	 * Constructor
	 * @param pmf
	 * @param dbDialectHandler
	 */
	@Inject
	public EntityDao(PersistenceManagerFactory pmf, IDbDialectHandler dbDialectHandler) {
		this.pmf = pmf;
		this.dbDialectHandler = dbDialectHandler;
		this.comparatorTranslator = new JdoQueryComparatorTranslator();
	}

	protected final PersistenceManager getPersistenceManager() {
		return getPersistenceManager();
	}

	public <E extends IEntity> E load(PrimaryKey<E> key) {
		final PersistenceManager pm = getPersistenceManager();
		final E e = pm.getObjectById(key.getType(), key.getId());
		return e;
	}

	public <E extends IEntity> E load(IBusinessKey<E> key) {
		try {
			return findEntity(new com.tll.criteria.Criteria<E>(key.getType()));
		}
		catch(final InvalidCriteriaException e) {
			throw new JDOException("Unable to load entity from business key: " + e.getMessage(), e);
		}
	}

	public <N extends INamedEntity> N load(NameKey<N> nameKey) {
		try {
			final com.tll.criteria.Criteria<N> nc = new com.tll.criteria.Criteria<N>(nameKey.getType());
			nc.getPrimaryGroup().addCriterion(nameKey, false);
			return findEntity(nc);
		}
		catch(final InvalidCriteriaException e) {
			throw new JDOException("Unable to load entity from name key: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> loadAll(Class<E> entityType) {
		final PersistenceManager pm = getPersistenceManager();
		final Query q = pm.newQuery(entityType);
		try {
			final List<E> list = (List<E>) q.execute();
			if(list.iterator().hasNext()) {
				final ArrayList<E> rval = new ArrayList<E>(list.size());
				for(final E e : list) {
					rval.add(e);
				}
				return rval;
			}
			// no results
			return new ArrayList<E>(0);
		}
		finally {
			q.closeAll();
		}
	}

	public <E extends IEntity> E persist(E entity) {
		final PersistenceManager pm = getPersistenceManager();
		return persistInternal(entity, true, pm);
	}

	public <E extends IEntity> Collection<E> persistAll(Collection<E> entities) {
		if(entities == null) return null;
		final PersistenceManager pm = getPersistenceManager();
		final Collection<E> merged = new HashSet<E>(entities.size());
		for(final E e : entities) {
			merged.add(persistInternal(e, false, pm));
		}
		pm.flush();
		return merged;
	}

	private <E extends IEntity> E persistInternal(E entity, boolean flush, PersistenceManager pm) {
		try {
			final E saved = pm.makePersistent(entity);
			if(flush) pm.flush();
			return saved;
		}
		catch(final RuntimeException re) {
			throw dbDialectHandler.translate(re);
		}
	}

	public <E extends IEntity> void purge(E entity) {
		final PersistenceManager pm = getPersistenceManager();
		purgeInternal(entity, true, pm);
	}

	public <E extends IEntity> void purgeAll(Collection<E> entities) {
		if(!CollectionUtil.isEmpty(entities)) {
			final PersistenceManager pm = getPersistenceManager();
			for(final E e : entities) {
				purgeInternal(e, false, pm);
			}
			pm.flush();
		}
	}

	private <E extends IEntity> void purgeInternal(E entity, boolean flush, PersistenceManager pm) {
		try {
			pm.deletePersistent(entity);
			if(flush) pm.flush();
		}
		catch(final RuntimeException re) {
			throw dbDialectHandler.translate(re);
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> findEntities(com.tll.criteria.Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null || criteria.getCriteriaType().isScalar()) {
			throw new InvalidCriteriaException("A criteria type must be specified and be non-scalar.");
		}
		return (List<E>) processCriteria(criteria, sorting, !criteria.getCriteriaType().isQuery());
	}

	public <E extends IEntity> E findEntity(com.tll.criteria.Criteria<E> criteria) throws InvalidCriteriaException {
		final List<E> list = findEntities(criteria, null);
		if(list == null || list.size() < 1) {
			throw new JDOObjectNotFoundException("No matching entity found.");
		}
		else if(list.size() > 1) {
			throw new JDODataStoreException("More than one matching entity found.");
		}
		assert list.size() == 1;
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<SearchResult<?>> find(com.tll.criteria.Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null) {
			throw new InvalidCriteriaException("A criteria type must be specified.");
		}
		return (List<SearchResult<?>>) processCriteria(criteria, sorting, !criteria.getCriteriaType().isQuery());
	}

	/**
	 * Process criteria instances providing a distinct list of matching results
	 * whose elements are either {@link SearchResult} or {@link IEntity} instances
	 * depending on the specified {@link CriteriaType}.
	 * @param <E>
	 * @param criteria Assumed non-<code>null</code>.
	 * @param sorting The optional sorting directive
	 * @param applySorting Apply the sorting?
	 * @return List of distinct matching results
	 * @throws InvalidCriteriaException When the criteria type is invalid or
	 *         otherwise.
	 */
	private <E extends IEntity> List<?> processCriteria(com.tll.criteria.Criteria<E> criteria, Sorting sorting,
			boolean applySorting) throws InvalidCriteriaException {
		assert criteria != null;
		final PersistenceManager pm = getPersistenceManager();
		if(criteria.getCriteriaType().isQuery()) {
			// presume named query ref
			final ISelectNamedQueryDef nq = criteria.getNamedQueryDefinition();
			if(nq == null || nq.getQueryName() == null) {
				throw new InvalidCriteriaException("No named query specified.");
			}
			final Query q =
				assembleQuery(pm, criteria.getEntityClass(), nq.getQueryName(), criteria.getQueryParams(), sorting, true);
			try {
				final List<?> list = (List<?>) q.execute();
				return (List<?>) pm.detachCopyAll(list);
			}
			finally {
				q.closeAll();
			}
		}
		// translate to a jdo query
		final Query q = pm.newQuery();
		applyCriteria(q, criteria, sorting, applySorting);
		try {
			final List<?> rval = (List<?>) q.execute();
			return (List<?>) pm.detachCopyAll(rval);
		}
		finally {
			q.closeAll();
		}
	}

	/**
	 * Translates native criteria to a new jdo query instance.
	 * @param pm The active entity manager
	 * @param entityClass
	 * @param queryName
	 * @param queryParams
	 * @param sorting
	 * @param cacheable Is this query cacheable?
	 * @return New Query instance
	 * @throws InvalidCriteriaException When no query name is specified in the
	 *         given criteria.
	 */
	private Query assembleQuery(PersistenceManager pm, Class<? extends IEntity> entityClass, String queryName,
			Collection<IQueryParam> queryParams, Sorting sorting, boolean cacheable) throws InvalidCriteriaException {
		if(queryName == null) {
			throw new InvalidCriteriaException("No query name specified.");
		}

		final Query q = pm.newNamedQuery(entityClass, queryName);

		// fill the named params (if any)
		if(queryParams != null && queryParams.size() > 0) {
			final StringBuilder sb = new StringBuilder();
			for(final IQueryParam queryParam : queryParams) {
				sb.append(",");
				sb.append(queryParam.getPropertyName());
			}
			q.declareParameters(sb.substring(1));
		}

		// apply sorting (if specified)
		if(sorting != null) {
			q.setOrdering(sorting.toString());
		}

		return q;
	}

	/**
	 * Translates criterion objects to the hibernate criterion objects. This
	 * method takes a <code>DetachedCriteria</code> implementation simply because
	 * there is no common interface for <code>DetachedCriteria</code> and
	 * <code>Criteria</code>. This method may be overridden by subclasses and thus
	 * is not called from processUniqueCriteria(Criteria).
	 * @param <E>
	 * @param q query
	 * @param criteria Criteria object
	 * @param sorting The optional sorting directive
	 * @param applySorting Apply the sorting?
	 * @throws InvalidCriteriaException
	 */
	private <E extends IEntity> void applyCriteria(Query q, com.tll.criteria.Criteria<E> criteria,
			Sorting sorting,
			boolean applySorting) throws InvalidCriteriaException {
		if(criteria.isSet()) {
			final CriterionGroup pg = criteria.getPrimaryGroup();
			final StringBuilder sb = new StringBuilder();
			for(final ICriterion crit : pg) {
				applyCriterion(sb, crit, criteria, sorting, pg.isConjunction());
			}
			q.setFilter(sb.toString());
		} // else all entities will be retrieved

		if(applySorting) {
			q.setOrdering(sorting.toString());
		}
	}

	/**
	 * @param <E>
	 * @param sb the string buffer which is appended with the translated criterion
	 * @param ctn the criterion
	 * @param criteria the parent native criteria
	 * @param sorting
	 * @param conjunction <code>true</code> ==> (... 'AND' ...),
	 *        <code>false</code> ==> (... 'OR' ..), <code>null</code> ==> 'AND'
	 * @throws InvalidCriteriaException
	 */
	private <E extends IEntity> void applyCriterion(StringBuilder sb, ICriterion ctn,
			com.tll.criteria.Criteria<E> criteria,
			Sorting sorting, Boolean conjunction) throws InvalidCriteriaException {
		if(!ctn.isSet()) return;
		if(ctn.isGroup()) {
			final CriterionGroup g = (CriterionGroup) ctn;
			for(final ICriterion c : g) {
				applyCriterion(sb, c, criteria, sorting, conjunction);
			}
			return;
		}

		// single (non-group) criterion
		final String expression = comparatorTranslator.translate(ctn);
		if(conjunction == null) {
			sb.append(" && ");
			sb.append(expression);
		}
		else {
			sb.append(conjunction ? " && " : " || ");
			sb.append(expression);
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> findByIds(Class<E> entityType, List<Integer> ids, Sorting sorting) {
		final com.tll.criteria.Criteria<E> nativeCriteria = new com.tll.criteria.Criteria<E>(entityType);
		nativeCriteria.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, ids, Comparator.IN, false);
		final PersistenceManager pm = getPersistenceManager();
		final Query q = pm.newQuery();
		try {
			applyCriteria(q, nativeCriteria, sorting, true);
		}
		catch(final InvalidCriteriaException e) {
			throw new JDOException(e.getMessage(), e);
		}
		try {
			final List<E> list = (List<E>) q.execute();
			return (List<E>) pm.detachCopyAll(list);
		}
		finally {
			q.closeAll();
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<Integer> getIds(com.tll.criteria.Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria.getCriteriaType().isQuery()) {
			throw new InvalidCriteriaException("Ids are not supplied for direct queries!");
		}
		final PersistenceManager pm = getPersistenceManager();
		final Query q = pm.newQuery();
		applyCriteria(q, criteria, sorting, true);

		final FetchGroup grp = pmf.getFetchGroup(criteria.getEntityClass(), "fg");
		grp.addMember("id");

		// Add this group to the fetch plan (using its name)
		pm.getFetchPlan().addGroup("fg");

		return (List<Integer>) q.execute();
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> getEntitiesFromIds(Class<E> entityClass, Collection<Integer> ids, Sorting sorting) {
		if(CollectionUtil.isEmpty(ids)) {
			return new ArrayList<E>();
		}
		final PersistenceManager pm = getPersistenceManager();
		final Query q = pm.newQuery(entityClass);
		final StringBuilder sb = new StringBuilder();
		for(final Integer id : ids) {
			sb.append(" or ");
			sb.append("id=");
			sb.append(id);
		}
		q.setFilter(sb.substring(3));
		if(sorting != null) {
			q.setOrdering(sorting.toString());
		}
		List<E> rval = null;
		try {
			final List<E> list = (List<E>) q.execute();
			rval = list;
			// TODO finish or fix
		}
		finally {
			q.closeAll();
		}
		return rval;
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> IPageResult<SearchResult<?>> getPage(com.tll.criteria.Criteria<E> criteria,
			Sorting sorting, int offset, int pageSize) throws InvalidCriteriaException {
		assert criteria != null && criteria.getCriteriaType() != null;
		List<SearchResult<E>> rlist = null;
		int totalCount = -1;
		final PersistenceManager pm = getPersistenceManager();

		switch(criteria.getCriteriaType()) {

			case ENTITY: {
				if(sorting == null) {
					// sorting is necessary in the case of IPage results due to necessary
					// in memory manipulation to provide a distinct list of results
					throw new InvalidCriteriaException("Paged results require a sorting directive");
				}
				// count query
				Query cq = null;
				try {
					cq = pm.newQuery(criteria.getEntityClass());
					applyCriteria(cq, criteria, sorting, false);
					cq.setUnique(true);
					cq.setGrouping("count(id)");
					final Integer rowCount = (Integer) cq.execute();
					assert rowCount != null;
					totalCount = rowCount.intValue();
				}
				finally {
					if(cq != null) cq.closeAll();
				}
				// data (page) query
				Query q = null;
				try {
					q = pm.newQuery(criteria.getEntityClass());
					applyCriteria(q, criteria, sorting, true);
					q.setRange(offset, offset + pageSize);
					q.setResultClass(SearchResult.class);
					final List<SearchResult<E>> list = (List<SearchResult<E>>) q.execute();
					rlist = (List<SearchResult<E>>) pm.detachCopyAll(list);
				}
				finally {
					if(q != null) q.closeAll();
				}
				break;
			}

			case ENTITY_NAMED_QUERY: {
				// get the count by convention looking for a couter-part named query w/
				// same name and additional suffix of .count
				final ISelectNamedQueryDef snq = criteria.getNamedQueryDefinition();
				if(!snq.isSupportsPaging()) {
					throw new InvalidCriteriaException(snq.getQueryName() + " query does not support paging.");
				}
				final String queryName = snq.getQueryName();
				final String countQueryName = snq.getQueryName() + ".count";
				// count query
				Query cq = null;
				try {
					cq = assembleQuery(pm, criteria.getEntityClass(), countQueryName, null, null, false);
					cq.setUnique(true);
					final Long count = (Long) cq.execute();
					assert count != null;
					totalCount = count.intValue();
				}
				finally {
					if(cq != null) cq.closeAll();
				}
				// data (page) query
				Query q = null;
				try {
					q = assembleQuery(pm, criteria.getEntityClass(), queryName, null, null, false);
					q.setRange(offset, offset + pageSize);
					q.setResultClass(SearchResult.class);
					final List<SearchResult<E>> list = (List<SearchResult<E>>) q.execute();
					rlist = (List<SearchResult<E>>) pm.detachCopyAll(list);
				}
				finally {
					if(q != null) q.closeAll();
				}
				break;
			}

			case SCALAR_NAMED_QUERY: {
				// get the count by convention looking for a couter-part named query w/
				// same name and additional suffix of .count
				final ISelectNamedQueryDef snq = criteria.getNamedQueryDefinition();
				final String queryName = snq.getQueryName();
				final String countQueryName = snq.getQueryName() + ".count";
				// count query
				Query cq = null;
				try {
					cq = assembleQuery(pm, criteria.getEntityClass(), countQueryName, criteria.getQueryParams(), null, false);
					cq.setUnique(true);
					final Long count = (Long) cq.execute();
					assert count != null;
					totalCount = count.intValue();
				}
				finally {
					if(cq != null) cq.closeAll();
				}
				// data (page) query
				Query q = null;
				try {
					q = assembleQuery(pm, criteria.getEntityClass(), queryName, null, null, false);
					q.setRange(offset, offset + pageSize);
					q.setResultClass(SearchResult.class);
					final List<SearchResult<E>> list = (List<SearchResult<E>>) q.execute();
					rlist = (List<SearchResult<E>>) pm.detachCopyAll(list);
				}
				finally {
					if(q != null) q.closeAll();
				}
				break;
			}
		}
		if(rlist == null) {
			throw new InvalidCriteriaException("Invalid paging criteria: " + criteria.getCriteriaType().name());
		}

		final List<SearchResult<E>> list = rlist;
		final int count = totalCount;

		return new IPageResult() {

			@Override
			public int getResultCount() {
				return count;
			}

			@Override
			public List getPageList() {
				return list;
			}

		};
	}
}
