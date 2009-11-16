/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.dao.jdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.apache.commons.lang.math.NumberRange;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jdo.JdoCallback;
import org.springframework.orm.jdo.JdoObjectRetrievalFailureException;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.google.inject.Inject;
import com.tll.criteria.Criteria;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.Criterion;
import com.tll.criteria.CriterionGroup;
import com.tll.criteria.ICriterion;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.EntityNotFoundException;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.NonUniqueResultException;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.util.CollectionUtil;
import com.tll.util.DateRange;

/**
 * JdoEntityDao
 * @author jpk
 */
public class JdoEntityDao extends JdoDaoSupport implements IEntityDao {

	private static final String Q_AND = " && ";

	private static final String Q_OR = " || ";

	/**
	 * Constructor
	 * @param pmf
	 */
	@Inject
	public JdoEntityDao(PersistenceManagerFactory pmf) {
		super();
		setPersistenceManagerFactory(pmf);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> E load(PrimaryKey<E> key) {
		if(logger.isDebugEnabled()) logger.debug("Loading by PK: " + key);
		try {
			final E e = (E) getJdoTemplate().getObjectById(key.getType(), key.getId());
			if(logger.isDebugEnabled()) logger.debug(e + " loaded by PK");
			return e;
		}
		catch(final JdoObjectRetrievalFailureException ex) {
			throw new EntityNotFoundException("Entity of primary key: '" + key + "' not found.", ex);
		}
	}

	@Override
	public <E extends IEntity> E load(IBusinessKey<E> key) {
		if(logger.isDebugEnabled()) logger.debug("Loading by BK: " + key);
		try {
			final E e = findEntity(new Criteria<E>(key.getType()));
			if(logger.isDebugEnabled()) logger.debug(e + " loaded by BK");
			return e;
		}
		catch(final InvalidCriteriaException e) {
			throw new ObjectRetrievalFailureException(key.getType(), key);
		}
	}

	@Override
	public <N extends INamedEntity> N load(NameKey<N> nameKey) {
		if(logger.isDebugEnabled()) logger.debug("Loading by NameKey: " + nameKey);
		try {
			final Criteria<N> nc = new Criteria<N>(nameKey.getType());
			nc.getPrimaryGroup().addCriterion(nameKey, false);
			final N e = findEntity(nc);
			if(logger.isDebugEnabled()) logger.debug(e + " loaded by NameKey");
			return e;
		}
		catch(final InvalidCriteriaException e) {
			throw new ObjectRetrievalFailureException(nameKey.getType(), nameKey);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> loadAll(Class<E> entityType) {
		return (List<E>) getJdoTemplate().find(entityType);
	}

	@Override
	public <E extends IEntity> E persist(E entity) {
		return persistInternal(entity, true);
	}

	@Override
	public <E extends IEntity> Collection<E> persistAll(Collection<E> entities) {
		if(entities == null) return null;
		final Collection<E> merged = new HashSet<E>(entities.size());
		for(final E e : entities) {
			merged.add(persistInternal(e, false));
		}
		getJdoTemplate().flush();
		return merged;
	}

	@SuppressWarnings("unchecked")
	private <E extends IEntity> E persistInternal(E entity, boolean flush) {
		if(logger.isDebugEnabled()) logger.debug("Persisting '" + entity + "'..");
		final E saved = (E) getJdoTemplate().makePersistent(entity);
		if(flush) {
			if(logger.isDebugEnabled()) logger.debug("Flushing PersistenceManager after persist");
			getJdoTemplate().flush();
		}
		if(logger.isDebugEnabled()) logger.debug(saved + " persisted");
		return saved;
	}

	@Override
	public <E extends IEntity> void purge(E entity) throws EntityNotFoundException, DataAccessException {
		purgeInternal(entity, true);
	}

	@Override
	public <E extends IEntity> void purge(PrimaryKey<E> key) throws EntityNotFoundException, DataAccessException {
		final E e = load(key);
		purge(e);
	}

	@Override
	public <E extends IEntity> void purgeAll(Collection<E> entities) {
		if(!CollectionUtil.isEmpty(entities)) {
			try {
				for(final E e : entities) {
					purgeInternal(e, false);
				}
			}
			finally {
				getJdoTemplate().flush();
			}
		}
	}

	private <E extends IEntity> void purgeInternal(final E entity, boolean flush) {
		if(logger.isDebugEnabled()) logger.debug("Purging " + entity);
		getJdoTemplate().deletePersistent(entity);
		if(flush) {
			if(logger.isDebugEnabled()) logger.debug("Flushing PersistenceManager after purge");
			getJdoTemplate().flush();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> findEntities(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null || criteria.getCriteriaType().isScalar()) {
			throw new InvalidCriteriaException("A criteria type must be specified and be non-scalar.");
		}
		return (List<E>) processCriteria(criteria, sorting);
	}

	@Override
	public <E extends IEntity> E findEntity(Criteria<E> criteria) throws InvalidCriteriaException,
	EntityNotFoundException, NonUniqueResultException {
		final List<E> list = findEntities(criteria, null);
		if(list == null || list.size() < 1) {
			throw new EntityNotFoundException("No matching entity found.", null);
		}
		else if(list.size() > 1) {
			throw new NonUniqueResultException("More than one matching entity found.");
		}
		assert list.size() == 1;
		return list.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<SearchResult<?>> find(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null) {
			throw new InvalidCriteriaException("A criteria type must be specified.");
		}
		return (List<SearchResult<?>>) processCriteria(criteria, sorting);
	}

	@SuppressWarnings("unchecked")
	private <E extends IEntity> List<SearchResult<E>> findByNamedQuery(final Criteria<E> criteria, final Sorting sorting)
	throws DataAccessException {
		final Class<E> entityClass = criteria.getEntityClass();
		final ISelectNamedQueryDef qd = criteria.getNamedQueryDefinition();
		final String qname = qd.getQueryName();
		final HashMap<String, Object> values = new HashMap<String, Object>();
		final Collection<IQueryParam> qps = criteria.getQueryParams();
		if(qps != null) {
			for(final IQueryParam qp : criteria.getQueryParams()) {
				values.put(qp.getPropertyName(), qp.getValue());
			}
		}
		final Collection<E> clc = (Collection<E>) getJdoTemplate().execute(new JdoCallback() {

			public Object doInJdo(PersistenceManager pm) throws JDOException {
				final Query query = pm.newNamedQuery(entityClass, qname);
				if(sorting != null) {
					query.setOrdering(sorting.getJdoOrderingClause());
				}
				return query.executeWithMap(values);
			}
		}, true);

		if(clc == null) {
			return null;
		}

		final ArrayList<SearchResult<E>> rval = new ArrayList<SearchResult<E>>();
		for(final E e : clc) {
			rval.add(new SearchResult<E>(e));
		}
		return rval;
	}

	/**
	 * Process criteria instances providing a distinct list of matching results
	 * whose elements are either {@link SearchResult} or {@link IEntity} instances
	 * depending on the specified {@link CriteriaType}.
	 * @param <E>
	 * @param criteria Assumed non-<code>null</code>.
	 * @param sorting The optional sorting directive
	 * @return List of distinct matching results
	 * @throws InvalidCriteriaException When the criteria type is invalid or
	 *         otherwise.
	 */
	private <E extends IEntity> List<?> processCriteria(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		assert criteria != null;
		if(logger.isDebugEnabled()) logger.debug("Processing criteria: " + criteria);

		if(criteria.getCriteriaType().isQuery()) {
			return findByNamedQuery(criteria, sorting);
		}

		// translate to a jdo query
		final Query q = getPersistenceManager().newQuery(criteria.getEntityClass());
		final ArrayList<Object> params = new ArrayList<Object>();
		final String filter = toFilter(criteria, params);
		q.setFilter(filter);
		if(sorting != null) {
			q.setOrdering(sorting.getJdoOrderingClause());
		}

		return (List<?>) getJdoTemplate().executeFind(new JdoCallback() {

			@Override
			public Object doInJdo(PersistenceManager pm) throws JDOException {
				try {
					final List<?> list = (List<?>) q.executeWithArray(params.toArray(new Object[] {}));
					return getJdoTemplate().detachCopyAll(list);
				}
				finally {
					q.closeAll();
				}
			}
		});
	}

	/**
	 * Translates native criteria to a new jdo query instance.
	 * @param entityClass
	 * @param queryName
	 * @param queryParams
	 * @param sorting
	 * @param cacheable Is this query cacheable?
	 * @return New Query instance
	 * @throws InvalidCriteriaException When no query name is specified in the
	 *         given criteria.
	 */
	private Query assembleQuery(Class<? extends IEntity> entityClass, String queryName,
			Collection<IQueryParam> queryParams, Sorting sorting, boolean cacheable) throws InvalidCriteriaException {
		if(queryName == null) {
			throw new InvalidCriteriaException("No query name specified.");
		}

		final Query q = getPersistenceManager().newNamedQuery(entityClass, queryName);

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
			q.setOrdering(sorting.getJdoOrderingClause());
		}

		return q;
	}

	private <E extends IEntity> String toFilter(Criteria<E> criteria, final ArrayList<Object> params) {
		final StringBuilder sb = new StringBuilder();
		if(criteria.isSet()) {
			final CriterionGroup pg = criteria.getPrimaryGroup();
			for(final ICriterion crit : pg) {
				toFilter(sb, crit, Boolean.valueOf(pg.isConjunction()), params);
			}
		}
		if(sb.indexOf(Q_AND) == 0 || sb.indexOf(Q_OR) == 0) sb.delete(0, 4);
		return sb.toString();
	}

	/**
	 * @param <E>
	 * @param sb the string buffer which is appended with the translated criterion
	 * @param ctn the criterion
	 * @param conjunction <code>true</code> ==> (... 'AND' ...),
	 *        <code>false</code> ==> (... 'OR' ..), <code>null</code> ==> 'AND'
	 * @param params the ordered list of implicit params needed in some cases
	 */
	private <E extends IEntity> void toFilter(StringBuilder sb, ICriterion ctn, Boolean conjunction, final ArrayList<Object> params) {
		if(!ctn.isSet()) return;
		if(ctn.isGroup()) {
			final CriterionGroup g = (CriterionGroup) ctn;
			sb.append(" (");
			for(final ICriterion c : g) {
				if(sb.indexOf(Q_AND) == 0 || sb.indexOf(Q_OR) == 0) sb.delete(0, 4);
				toFilter(sb, c, conjunction, params);
			}
			sb.append(')');
			return;
		}

		sb.append((conjunction == null || conjunction == Boolean.TRUE) ? Q_AND : Q_OR);

		// single (non-group) criterion
		assert ctn instanceof Criterion;
		final Criterion crtn = (Criterion) ctn;
		final String name = crtn.getField();
		final Object value = crtn.getValue();
		switch(crtn.getComparator()) {
		case BETWEEN: {
			Object min, max;
			if(value instanceof NumberRange) {
				final NumberRange range = (NumberRange) value;
				min = range.getMinimumNumber();
				max = range.getMaximumNumber();
			}
			else if(value instanceof DateRange) {
				final DateRange range = (DateRange) value;
				min = range.getStartDate();
				max = range.getEndDate();
			}
			else {
				// presume an object array
				final Object[] oarr = (Object[]) value;
				min = oarr[0];
				max = oarr[1];
			}
			if(min != null && max != null) {
				sb.append(name);
				sb.append(" >= ");
				sb.append(min);
				sb.append(" && ");
				sb.append(name);
				sb.append(" <= ");
				sb.append(max);
			}
			break;
		}
		case CONTAINS:
			if(value instanceof String) {
				// NOTE: case sensitive clause
				sb.append(name);
				sb.append(".contains(\"");
				sb.append(value);
				sb.append("\")");
			}
			break;
		case ENDS_WITH:
			if(value instanceof String) {
				// NOTE: case sensitive clause
				sb.append(name);
				sb.append(".endsWith(\"");
				sb.append(value);
				sb.append("\")");
			}
			break;
		case EQUALS:
			if(value instanceof String) {
				sb.append(name);
				sb.append(" == \"");
				sb.append(value);
				sb.append("\"");
			}
			else {
				sb.append(name);
				sb.append(" == ");
				sb.append(value);
			}
			break;
		case GREATER_THAN:
			sb.append(name);
			sb.append(" > ");
			sb.append(value);
			break;
		case GREATER_THAN_EQUALS:
			sb.append(name);
			sb.append(" >= ");
			sb.append(value);
			break;
		case IN:
			if(value instanceof Collection<?>) {
				sb.append(":");
				// NOTE[TODO]: may need to ensure param name uniqueness in the query param namespace
				sb.append(name);
				sb.append(".contains(");
				sb.append(name);
				sb.append(")");
				params.add(value);
			}
			break;
		case IS:
			// not supported
			break;
		case LESS_THAN:
			sb.append(name);
			sb.append(" < ");
			sb.append(value);
			break;
		case LESS_THAN_EQUALS:
			sb.append(name);
			sb.append(" <= ");
			sb.append(value);
			break;
		case LIKE:
			// not supported
			break;
		case NOT_EQUALS:
			if(value instanceof String) {
				// NOTE: case sensitive clause
				sb.append(name);
				sb.append(" != \"");
				sb.append(value);
				sb.append("\"");
			}
			else {
				sb.append(name);
				sb.append(" == ");
				sb.append(value);
			}
			break;
		case STARTS_WITH:
			if(value instanceof String) {
				// NOTE: case sensitive clause
				sb.append(name);
				sb.append(".startsWith(\"");
				sb.append(value);
				sb.append("\"");
			}
			break;
		}
		if(sb.length() < 1) {
			throw new UnsupportedOperationException("Un-supported criterion state: " + crtn);
		}
		if(logger.isDebugEnabled())
			logger.debug("Criterion (" + ctn + ") translated to JDO query fragment: " + sb.toString());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> findByIds(Class<E> entityType, final Collection<Long> ids, Sorting sorting) {
		final List<E> rval = (List<E>) getJdoTemplate().find(entityType, ":ids.contains(this.id)", null, new Object[] {
			ids
		}, sorting == null ? null : sorting.getJdoOrderingClause());
		return rval;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<Long> getIds(Criteria<E> criteria, Sorting sorting) throws InvalidCriteriaException {
		if(criteria.getCriteriaType().isQuery()) {
			throw new InvalidCriteriaException("Ids are not supplied for direct queries!");
		}
		final PersistenceManager pm = getPersistenceManager();
		final Query q = pm.newQuery(criteria.getEntityClass());
		final ArrayList<Object> params = new ArrayList<Object>();
		final String filter = toFilter(criteria, params);
		q.setFilter(filter);
		if(sorting != null) q.setOrdering(sorting.getJdoOrderingClause());
		q.setResult(IEntity.PK_FIELDNAME);
		final List<Long> rval = (List<Long>) getJdoTemplate().executeFind(new JdoCallback() {

			@Override
			public Object doInJdo(PersistenceManager thePm) throws JDOException {
				try {
					final List<Long> list = (List<Long>) q.executeWithArray(params.toArray(new Object[] {}));
					// we need to "detach" the result from the query else JDO bitches
					// TODO: make more efficient
					final ArrayList<Long> rlist = new ArrayList<Long>(list.size());
					for(int i = 0; i < list.size(); i++) {
						rlist.add(list.get(i));
					}
					return rlist;
				}
				finally {
					q.closeAll();
				}
			}
		});
		return rval;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> IPageResult<SearchResult<?>> getPage(Criteria<E> criteria, Sorting sorting, int offset,
			int pageSize) throws InvalidCriteriaException {
		if(criteria == null || criteria.getCriteriaType() == null) {
			throw new InvalidCriteriaException("Empty or invalid criteria instance.");
		}
		List<SearchResult<E>> rlist = null;
		int totalCount = -1;
		switch(criteria.getCriteriaType()) {

		case ENTITY: {
			if(sorting == null) {
				// sorting is necessary in the case of IPage results due to
				// necessary
				// in memory manipulation to provide a distinct list of results
				throw new InvalidCriteriaException("Paged results require a sorting directive");
			}
			// count query
			Query cq = null;
			try {
				cq = getPersistenceManager().newQuery(criteria.getEntityClass());
				final ArrayList<Object> params = new ArrayList<Object>();
				cq.setFilter(toFilter(criteria, params));
				cq.setResult("count(id)");
				final Long rowCount = (Long) cq.executeWithArray(params.toArray(new Object[] {}));
				assert rowCount != null && rowCount.longValue() <= Integer.MAX_VALUE;
				totalCount = rowCount.intValue();
			}
			finally {
				if(cq != null) cq.closeAll();
			}
			// data (page) query
			Query q = null;
			try {
				q = getPersistenceManager().newQuery(criteria.getEntityClass());
				final ArrayList<Object> params = new ArrayList<Object>();
				q.setFilter(toFilter(criteria, params));
				q.setRange(offset, offset + pageSize);
				final List<E> list = (List<E>) q.executeWithArray(params.toArray(new Object[] {}));
				rlist = SearchResult.create(list);
			}
			finally {
				if(q != null) q.closeAll();
			}
			break;
		}

		case ENTITY_NAMED_QUERY: {
			// get the count by convention looking for a couter-part named query
			// w/
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
				cq = assembleQuery(criteria.getEntityClass(), countQueryName, null, null, false);
				cq.setUnique(true);
				final Collection clc = getJdoTemplate().find(Query.JDOQL, cq);
				final Long count = (Long) clc.iterator().next();
				assert count != null;
				totalCount = count.intValue();
			}
			finally {
				if(cq != null) cq.closeAll();
			}
			// data (page) query
			Query q = null;
			try {
				q = assembleQuery(criteria.getEntityClass(), queryName, null, null, false);
				q.setRange(offset, offset + pageSize);
				q.setResultClass(SearchResult.class);
				rlist = (List<SearchResult<E>>) getJdoTemplate().find(Query.JDOQL, q);
			}
			finally {
				if(q != null) q.closeAll();
			}
			break;
		}

		case SCALAR_NAMED_QUERY: {
			// get the count by convention looking for a couter-part named query
			// w/
			// same name and additional suffix of .count
			final ISelectNamedQueryDef snq = criteria.getNamedQueryDefinition();
			final String queryName = snq.getQueryName();
			final String countQueryName = snq.getQueryName() + ".count";
			// count query
			Query cq = null;
			try {
				cq = assembleQuery(criteria.getEntityClass(), countQueryName, criteria.getQueryParams(), null, false);
				cq.setUnique(true);
				final Collection clc = getJdoTemplate().find(Query.JDOQL, cq);
				final Long count = (Long) clc.iterator().next();
				assert count != null;
				totalCount = count.intValue();
			}
			finally {
				if(cq != null) cq.closeAll();
			}
			// data (page) query
			Query q = null;
			try {
				q = assembleQuery(criteria.getEntityClass(), queryName, null, null, false);
				q.setRange(offset, offset + pageSize);
				q.setResultClass(SearchResult.class);
				final List<SearchResult<E>> list = (List<SearchResult<E>>) getJdoTemplate().find(Query.JDOQL, q);
				rlist = (List<SearchResult<E>>) getPersistenceManager().detachCopyAll(list);
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

	@Override
	public int executeQuery(String queryName, IQueryParam[] params) throws DataAccessException {
		throw new UnsupportedOperationException();
	}
}
